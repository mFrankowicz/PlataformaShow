package plataforma;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.bson.Document;


// a class that going to manage and controll the translation area of users
// todo: here we want to able do sync data with the database via DatabaseManager
// todo: after sync, we populate the TranslationController with the associeted data
// todo: after populate, needs to sync the credentials in UserManager so the user can edit only his tab
// todo: option -> implement a state manager
public class TranslationController {

    private final DatabaseManager databaseManager;

    private ScrollPane scrollPane;
    private AnchorPane anchorPane;
    private FlowPane flowPane;

    private ObservableList<TranslationCards> translationCards = FXCollections.observableArrayList();

    private NoteController noteController;

    public TranslationController(NoteController _noteController) {
        databaseManager = new DatabaseManager();
        flowPane = new FlowPane();
        anchorPane = new AnchorPane();
        scrollPane = new ScrollPane();
        anchorPane.setMinWidth(800);
        noteController = _noteController;

    }


    public void run() {
        startTask();
    }

    private void startTask() {
        Runnable task = () -> runTask();

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

    }

    private void runTask() {
        MongoClientURI adminURI = new MongoClientURI("mongodb://marcosfrankowicz:Fiheci-123@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin");
        MongoClient mongoClient = new MongoClient(adminURI);

        MongoDatabase database = mongoClient.getDatabase("diagrama");
        MongoCollection<Document> collection = database.getCollection("root_table");

        int lineNumber = 0;
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());

                Document doc = cursor.next();
                String c_1 = doc.get("column_one", String.class);
                String c_2 = doc.get("column_two", String.class);
                String c_3 = doc.get("column_three", String.class);
                String c_4 = doc.get("column_four", String.class);

                System.out.println(c_1 + " - " + c_2 + " - " + c_3 + " - " + c_4);

                TranslationCards t_c_1 = new TranslationCards(1, lineNumber, noteController);
                t_c_1.getTextArea().setText(c_1);

                TranslationCards t_c_2 = new TranslationCards(2, lineNumber, noteController);
                t_c_2.getTextArea().setText(c_2);

                TranslationCards t_c_3 = new TranslationCards(3, lineNumber, noteController);
                t_c_3.getTextArea().setText(c_3);

                TranslationCards t_c_4 = new TranslationCards(4, lineNumber, noteController);
                t_c_4.getTextArea().setText(c_4);


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
                anchorPane.getChildren().add(flowPane);
                AnchorPane.setLeftAnchor(flowPane, 0.0);
                AnchorPane.setRightAnchor(flowPane, 0.0);
                AnchorPane.setTopAnchor(flowPane, 0.0);
                scrollPane.setContent(anchorPane);


                //todo: refactor to method
                translationCards.forEach((p) -> {
                    p.getTextArea().focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable,
                                            Boolean oldValue,
                                            Boolean newValue) {
                            noteController.getNoteCards().forEach((q) -> {
                                if(newValue) {
                                    if ((q.rowReference == p.rowNumber && q.columnReference == p.columnNumber)) {
                                        if(q.getvBoxPaneAnchorPane().isVisible()){
                                            ScrollPane noteScroll = noteController.getScrollPane();
                                            Node noteNode = q.getvBoxPaneAnchorPane();
                                            Bounds viewport = noteScroll.getViewportBounds();
                                            double contentHeight = noteScroll
                                                    .getContent()
                                                    .localToScene(noteScroll
                                                            .getContent()
                                                            .getBoundsInLocal())
                                                    .getHeight();

                                            double nodeMinY = noteNode
                                                    .localToScene(noteNode.getBoundsInLocal())
                                                    .getMinY();
                                            double nodeMaxY = noteNode
                                                    .localToScene(noteNode.getBoundsInLocal())
                                                    .getMaxY();

                                            double vValueDelta = 0;
                                            double vValueCurrent = noteScroll.getVvalue();

                                            if(nodeMaxY < 0) {
                                                vValueDelta = (nodeMinY - viewport.getHeight()) / contentHeight;
                                            } else if (nodeMinY > viewport.getHeight()){
                                                vValueDelta = (nodeMinY + viewport.getHeight()) / contentHeight;
                                            }

                                            noteScroll.setVvalue(vValueCurrent +  vValueDelta);
                                        }
                                    }
                                } else {

                                }
                            });
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



}
