package backup;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

public class ControllerBackup {

    ObservableList<ModelTable> list = FXCollections.observableArrayList();

    @FXML
    private TableView<ModelTable> tv;

    @FXML
    private TableColumn<ModelTable, String> columnOne;

    @FXML
    private TableColumn<ModelTable, String> columnTwo;

    @FXML
    private TableColumn<ModelTable, String> columnThree;

    @FXML
    private TableColumn<ModelTable, String> columnFour;

    @FXML
    private Label serverLabel;

    public void startTask() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                runTask();
            }
        };

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

    }

    public void runTask() {
        MongoClientURI adminURI = new MongoClientURI("mongodb://marcosfrankowicz:Fiheci-123@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin");
        MongoClient mongoClient = new MongoClient(adminURI);

        MongoDatabase database = mongoClient.getDatabase("diagrama");
        MongoCollection<Document> collection = database.getCollection("root_table");

        MongoCursor<Document> cursor = collection.find().iterator();

        try {
            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());

                Document doc = cursor.next();
                String c_1 = doc.get("column_one", String.class);
                String c_2 = doc.get("column_two", String.class);
                String c_3 = doc.get("column_three", String.class);
                String c_4 = doc.get("column_four", String.class);

                System.out.println(c_1 + " - " + c_2 + " - " + c_3 + " - " + c_4);
                list.add( new ModelTable(c_1, c_2, c_3, c_4));

            }
        } finally {
            cursor.close();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tv.getItems().setAll(list);
                serverLabel.setText("Status: ok!");
            }
        });

    }

    protected void init() {
        MongoClientURI adminURI = new MongoClientURI("mongodb://marcosfrankowicz:Fiheci-123@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin");
        MongoClient mongoClient = new MongoClient(adminURI);

        MongoDatabase database = mongoClient.getDatabase("diagrama");
        MongoCollection<Document> collection = database.getCollection("root_table");

        MongoCursor<Document> cursor = collection.find().iterator();

        try {
            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());

                Document doc = cursor.next();
                String c_1 = doc.get("column_one", String.class);
                String c_2 = doc.get("column_two", String.class);
                String c_3 = doc.get("column_three", String.class);
                String c_4 = doc.get("column_four", String.class);

                System.out.println(c_1 + " - " + c_2 + " - " + c_3 + " - " + c_4);
                list.add( new ModelTable(c_1, c_2, c_3, c_4));

            }
        } finally {
            cursor.close();
        }

        tv.getItems().setAll(list);

    }

    private void initCol() {
        columnOne.setCellValueFactory(new PropertyValueFactory<>("bodyColumnOne"));
        columnTwo.setCellValueFactory(new PropertyValueFactory<>("bodyColumnTwo"));
        columnThree.setCellValueFactory(new PropertyValueFactory<>("bodyColumnThree"));
        columnFour.setCellValueFactory(new PropertyValueFactory<>("bodyColumnFour"));
    }


    @FXML
    private void initialize() {
        initCol();
        startTask();
    }


    public static class ModelTable {
        private final SimpleStringProperty bodyColumnOne;
        private final SimpleStringProperty bodyColumnTwo;
        private final SimpleStringProperty bodyColumnThree;
        private final SimpleStringProperty bodyColumnFour;

        ModelTable(String bc1, String bc2, String bc3, String bc4) {
            this.bodyColumnOne = new SimpleStringProperty(bc1);
            this.bodyColumnTwo = new SimpleStringProperty(bc2);
            this.bodyColumnThree = new SimpleStringProperty(bc3);
            this.bodyColumnFour = new SimpleStringProperty(bc4);
        }

        public String getBodyColumnOne() {
            return bodyColumnOne.get();
        }

        public String getBodyColumnTwo() {
            return bodyColumnTwo.get();
        }

        public String getBodyColumnThree() {
            return bodyColumnThree.get();
        }

        public String getBodyColumnFour() {
            return bodyColumnFour.get();
        }
    }
}
