package plataforma;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.application.Platform;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private FlowPane flowPane;
    private ObservableList<TranslationCards> translationCards = FXCollections.observableArrayList();

    public TranslationController() {
        databaseManager = new DatabaseManager();
        flowPane = new FlowPane();
    }


    public void run(ObservableList<Controller.ModelTable> list) {
        startTask(list);
    }

    private void startTask(ObservableList<Controller.ModelTable> list) {
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

                TranslationCards t_c_1 = new TranslationCards(1, lineNumber);
                t_c_1.getTextArea().setText(c_1);

                TranslationCards t_c_2 = new TranslationCards(2, lineNumber);
                t_c_2.getTextArea().setText(c_2);

                TranslationCards t_c_3 = new TranslationCards(3, lineNumber);
                t_c_3.getTextArea().setText(c_3);

                TranslationCards t_c_4 = new TranslationCards(4, lineNumber);
                t_c_4.getTextArea().setText(c_4);


                translationCards.add(t_c_1);
                translationCards.add(t_c_2);
                translationCards.add(t_c_3);
                translationCards.add(t_c_4);

            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                translationCards.forEach((p) -> flowPane.getChildren().addAll(p.getPane()));
            }
        });

    }

    // connect to the database
    public void connect(){

    }

    // method for auto update the connection with database
    private void updateConnection(){

    }




}
