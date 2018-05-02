package plataforma;

import com.mongodb.BasicDBObject;
import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.session.ClientSession;
import com.mongodb.session.ServerSession;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.Document;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;

// a class that manages the database sync, and send data to the TranslationController
// todo: first, sync all data an send to the TranslationController so that we can view things
// todo: then it will send user auth data, and the TranslationController will decide what the user can edit
public class DatabaseManager {

    private MongoClient client;
    private MongoDatabase database;

    public DatabaseManager() {

    }

    public void connectToAuthDatabase(String username, String password){

        // todo: surround with a try / catch
        MongoClientURI adminURI = new MongoClientURI(MessageFormat.format("mongodb://{0}:{1}@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin", username, password));

        client = new MongoClient(adminURI);
    }

    public void connectToDiagramDatabase(){

        if(client != null) {
            database = client.getDatabase("diagrama");
        }

    }

    public void createUserAndCollection(String userName, String userPassword) {
        MongoClientURI adminURI = new MongoClientURI("mongodb://marcosfrankowicz:Fiheci-123@maincluster-shard-00-00-nqvuh.mongodb.net:27017,maincluster-shard-00-01-nqvuh.mongodb.net:27017,maincluster-shard-00-02-nqvuh.mongodb.net:27017/test?ssl=true&replicaSet=MainCluster-shard-0&authSource=admin");
        MongoClient mongoClient = new MongoClient(adminURI);

        MongoDatabase adminDatabase = mongoClient.getDatabase("admin");

        MongoDatabase diagramCollection = mongoClient.getDatabase("diagrama");

        // todo: this try-catch-finally try-catch sounds not ok, refactor this (done)
        // todo: so... i think its ok!?
        try {
            diagramCollection.createCollection(userName + "_collection");
        } catch (Exception e) {
            e.getStackTrace();
        }

        /*
        Document document = new Document("createUser", userName)
                .append("pwd", userPassword)
                .append("roles",
                        Collections.singletonList(new BasicDBObject("role", "readWrite")
                                .append("db", "diagrama")
                                .append("collection", userName+"_collection")));


        adminDatabase.runCommand(document);
        System.out.println(document.toJson());
        */


    }

    public UserManager.User loginUser(String username, String password) {
        return new UserManager.User(username, password, "");
    }

    public HashMap<String, UserManager.User> getAllUsers() {
        return null;
    }


}
