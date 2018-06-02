package plataforma;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.bson.Document;

public class NoteController {

    private AnchorPane mainAnchorPane;
    private ScrollPane scrollPane;

    private VBox vBox;

    private ObservableList<NoteCards> noteCards = FXCollections.observableArrayList();

    private Thread backgroundThread;

    private String collectionName;

    public NoteController(String _collectionName){

        mainAnchorPane = new AnchorPane();
        scrollPane = new ScrollPane();

        mainAnchorPane.getChildren().add(scrollPane);

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 15.0);

        vBox = new VBox();
        vBox.paddingProperty().setValue(new Insets(5.0,0.0,5.0,0.0));
        scrollPane.setContent(vBox);

        collectionName = _collectionName;

    }

    public void initNoteCards() {
        Runnable task = () -> runInitTask();

        backgroundThread = new Thread(task);
        //backgroundThread.setDaemon(true);
        backgroundThread.start();
        backgroundThread.setPriority(10);

    }

    //todo: make on a thread
    public void addNote(int column, int row){
        NoteCards noteCard = new NoteCards(column, row);
        noteCard.isInDatabase = false;
        noteCards.add(noteCard);
        updateNotes();

    }

    public void saveNotesToDatabase(){
        noteCards.forEach((p) -> {
            if(p.isInDatabase == false) {
                addNoteToDatabase(p.columnReference, p.rowReference, p.getTextArea().getText());
                p.isInDatabase = true;
            }
        });
    }

    private void addNoteToDatabase(int column, int row, String text) {

        Runnable task = () -> addTask(column, row, text);

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    public void updateNotes() {
        Runnable task = () -> runUpdateTask();

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

    }

    private void addTask(int column, int row, String text){
        MongoClientURI adminURI = new MongoClientURI("mongodb://marcosfrankowicz:Fiheci-123@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin");
        MongoClient mongoClient = new MongoClient(adminURI);

        MongoDatabase database = mongoClient.getDatabase("diagrama");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        Document newNote = new Document("title", "note")
                .append("column", column)
                .append("row", row)
                .append("text", text);

        collection.insertOne(newNote);
    }

    private void runUpdateTask(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                noteCards.forEach((p) -> {
                            if (!vBox.getChildren().contains(p.getvBoxPaneAnchorPane())) {
                                vBox.getChildren().add(p.getvBoxPaneAnchorPane());
                                p.getHeader().setText("coluna: " + p.columnReference + " / " + "linha: " + p.rowReference + " / " + "db: " + p.isInDatabase ); //todo: String format
                            }
                        }
                );

            }
        });
    }

    private void runInitTask(){

        MongoClientURI adminURI = new MongoClientURI("mongodb://marcosfrankowicz:Fiheci-123@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin");
        MongoClient mongoClient = new MongoClient(adminURI);

        MongoDatabase database = mongoClient.getDatabase("diagrama");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());

                Document note = cursor.next();
                int column = note.getInteger("column");
                int row = note.getInteger("row");
                String text = note.getString("text");

                System.out.println(column +  "---" + row);
                NoteCards newNoteCard = new NoteCards(column, row);
                newNoteCard.isInDatabase = true;
                newNoteCard.getTextArea().setText(text);

                noteCards.add(newNoteCard);

            }
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                noteCards.forEach((noteCards) -> {
                            if (!vBox.getChildren().contains(noteCards.getvBoxPaneAnchorPane())) {
                                vBox.getChildren().add(noteCards.getvBoxPaneAnchorPane());
                                noteCards.getHeader().setText("coluna: " + noteCards.columnReference + " / " + "linha: " + noteCards.rowReference ); //todo: String format
                            }
                        }
                );
            }
        });
    }

    //getters and setters
    public AnchorPane getMainAnchorPane() {
        return mainAnchorPane;
    }

    public void setMainAnchorPane(AnchorPane mainAnchorPane) {
        this.mainAnchorPane = mainAnchorPane;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public ObservableList<NoteCards> getNoteCards() {
        return noteCards;
    }

    public void setNoteCards(ObservableList<NoteCards> noteCards) {
        this.noteCards = noteCards;
    }

    public Thread getBackgroundThread() {
        return backgroundThread;
    }
}
