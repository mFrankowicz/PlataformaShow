package plataforma;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.bson.Document;

// a class that going to manage and controll the translation area of users
// todo: here we want to able do sync data with the database via DatabaseManager
// todo: after populate, needs to sync the credentials in UserManager so the user can edit only his tab
public class TranslationController {

    private final DatabaseManager databaseManager;

    private ScrollPane scrollPane;
    private AnchorPane anchorPane;
    private FlowPane flowPane;

    private ObservableList<TranslationCards> translationCards = FXCollections.observableArrayList();

    private NoteController noteController;

    Thread backgroundThread;

    private String collectionName;

    public TranslationController(NoteController _noteController, String _collectionName) {
        databaseManager = new DatabaseManager();
        flowPane = new FlowPane();
        anchorPane = new AnchorPane();
        scrollPane = new ScrollPane();
        anchorPane.setMinWidth(940);
        noteController = _noteController;
        collectionName = _collectionName;
    }


    public void initTranslationCards() {
        startTask();
    }

    private void startTask() {
        Runnable task = () -> runTask();

        backgroundThread = new Thread(task);
        //backgroundThread.setDaemon(true);
        backgroundThread.start();
        backgroundThread.setPriority(4);

    }

    // todo: refactor to use DatabaseManager
    private void runTask() {
        MongoClientURI adminURI = new MongoClientURI("mongodb://marcosfrankowicz:Fiheci-123@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin");
        MongoClient mongoClient = new MongoClient(adminURI);

        MongoDatabase database = mongoClient.getDatabase("diagrama");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        int lineNumber = 0;
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());

                Document doc = cursor.next();
                String c_1 = doc.get("column_one", String.class);
                String c_2 = doc.get("column_two", String.class);
                String c_3 = doc.get("column_three", String.class);
                String c_4 = doc.get("column_four", String.class);

                Document c_1_styles = (Document) doc.get("column_one_format");
                Document c_2_styles = (Document) doc.get("column_two_format");
                Document c_3_styles = (Document) doc.get("column_three_format");
                Document c_4_styles = (Document) doc.get("column_four_format");

                //System.out.println(c_1 + " - " + c_2 + " - " + c_3 + " - " + c_4);

                TranslationCards t_c_1 = new TranslationCards(1, lineNumber, noteController, collection);
                t_c_1.getTextArea().appendText(c_1);
                t_c_1.applyFormatation(c_1_styles);

                TranslationCards t_c_2 = new TranslationCards(2, lineNumber, noteController, collection);
                t_c_2.getTextArea().appendText(c_2);
                t_c_2.applyFormatation(c_2_styles);

                TranslationCards t_c_3 = new TranslationCards(3, lineNumber, noteController, collection);
                t_c_3.getTextArea().appendText(c_3);
                t_c_3.applyFormatation(c_3_styles);

                TranslationCards t_c_4 = new TranslationCards(4, lineNumber, noteController, collection);
                t_c_4.getTextArea().appendText(c_4);
                t_c_4.applyFormatation(c_4_styles);

                translationCards.add(t_c_1);
                translationCards.add(t_c_2);
                translationCards.add(t_c_3);
                translationCards.add(t_c_4);

                lineNumber++;

            }
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                translationCards.forEach((p) -> flowPane.getChildren().addAll(p.getPane()));

                flowPane.setVgap(15);
                flowPane.setHgap(5);

                anchorPane.getChildren().add(flowPane);
                AnchorPane.setLeftAnchor(flowPane, 0.0);
                AnchorPane.setRightAnchor(flowPane, 0.0);
                AnchorPane.setTopAnchor(flowPane, 5.0);
                scrollPane.setContent(anchorPane);

                // after the thread note controller is loaded, setup de toggle
                getTranslationCards().forEach((translationCards) -> {
                    noteController.getNoteCards().forEach(noteCards -> {
                        if(noteCards.rowReference == translationCards.rowNumber
                                && noteCards.columnReference == translationCards.columnNumber){
                            translationCards.setNoteToggler();
                        }
                    });
                });

            }
        });

    }

    // connect to the database
    public void connect(){

    }

    // method for auto update the connection with database
    private void updateConnection(){

    }

    public FlowPane getFlowPane() {
        return flowPane;
    }

    public void setFlowPane(FlowPane flowPane) {
        this.flowPane = flowPane;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public ObservableList<TranslationCards> getTranslationCards() {
        return translationCards;
    }

    public void setTranslationCards(ObservableList<TranslationCards> translationCards) {
        this.translationCards = translationCards;
    }

}
