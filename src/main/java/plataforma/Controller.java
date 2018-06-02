package plataforma;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.bson.Document;

import java.util.function.Consumer;

// todo: use RichText library
// todo: refactor to access the menu operations in root controller
// todo: create a userManager class
public class Controller {

    @FXML
    TabPane translationTabPane;

    @FXML
    Pane notePane;

    @FXML
    TabPane noteTabPane;

    @FXML
    MenuItem saveToDataBaseMenuItem;

    @FXML
    MenuItem threadTest;

    DatabaseManager databaseManager = new DatabaseManager();

    @FXML
    MenuItem saveAll;

    User root = new User("root_table", "root_notes");
    User margit = new User("margit_table", "margit_notes");

    @FXML
    private void initialize(){

        initializeLists();

        saveToDataBaseMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                margit.noteController.saveNotesToDatabase();
            }
        });

        saveAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                margit.translationController.getTranslationCards().forEach((p) -> {
                    if(p.saved == false) {
                        p.addNoteAsync();
                    }
                });
            }
        });

    }

    private void initializeLists() {
        startListTask();
    }

    private void startListTask() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                runListTask();
            }
        };

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(false);
        backgroundThread.setName("controllerThread");
        backgroundThread.start();
    }

    private void runListTask(){

        root.connect();
        margit.connect();

        // todo: add tabs here
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                //translation tab
                Tab rootTranslationTab = new Tab();
                rootTranslationTab.setText("Raiz");
                rootTranslationTab.setContent(root.translationAnchorPane);

                Tab margitTranslationTab = new Tab();
                margitTranslationTab.setText("Margit");
                margitTranslationTab.setContent(margit.translationAnchorPane);

                //note tab
                Tab rootNoteTab = new Tab();
                rootNoteTab.setText("Raiz");
                rootNoteTab.setContent(root.noteController.getMainAnchorPane());

                Tab margitNoteTab = new Tab();
                margitNoteTab.setText("Notas Margit");
                margitNoteTab.setContent(margit.noteController.getMainAnchorPane());


                translationTabPane.getTabs().add(rootTranslationTab);
                translationTabPane.getTabs().add(margitTranslationTab);

                noteTabPane.getTabs().add(rootNoteTab);
                noteTabPane.getTabs().add(margitNoteTab);

            }
        });


    }

    public void copyCollection(String db, String f, String t){
        MongoClientURI adminURI = new MongoClientURI("mongodb://marcosfrankowicz:Fiheci-123@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin");
        MongoClient mongoClient = new MongoClient(adminURI);

        MongoDatabase database = mongoClient.getDatabase(db);

        MongoCollection from = database.getCollection(f);
        MongoCollection to = database.getCollection(t);

        from.find().forEach((Consumer) (doc) -> {
            //System.out.println(doc);
            to.insertOne(doc);
        });
    }

/*
    public void startTask() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                runTask();
            }
        };

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.setName("controllerThread");
        backgroundThread.start();

    }

    public void runTask() {

        anchorPane.getChildren().add(translationController.getScrollPane());
        t.setText("Original");
        t.setContent(anchorPane);

        AnchorPane.setRightAnchor(translationController.getScrollPane(), 2.0);
        AnchorPane.setLeftAnchor(translationController.getScrollPane(), 2.0);
        AnchorPane.setTopAnchor(translationController.getScrollPane(), 2.0);
        AnchorPane.setBottomAnchor(translationController.getScrollPane(), 2.0);


        notePane.getChildren().add(noteController.getMainAnchorPane());

        noteController.getMainAnchorPane().setMinSize(270, 590);
        noteController.getMainAnchorPane().setPrefSize(270, 590);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                translationTabPane.getTabs().add(t);
                try {
                    noteController.initNoteCards();
                } finally {
                    translationController.initTranslationCards();
                }

            }
        });



    }
*/

}
