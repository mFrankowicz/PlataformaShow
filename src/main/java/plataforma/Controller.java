package plataforma;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;


// todo: use RichText library
// todo: refactor to access the menu operations in root controller
public class Controller {

    @FXML
    TabPane translationTabPane;

    @FXML
    Pane notePane;

    DatabaseManager databaseManager = new DatabaseManager();

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
        NoteController noteController = new NoteController();
        TranslationController tc = new TranslationController(noteController);
        Tab t = new Tab();
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(tc.getScrollPane());
        t.setText("Original");
        t.setContent(anchorPane);

        AnchorPane.setRightAnchor(tc.getScrollPane(), 2.0);
        AnchorPane.setLeftAnchor(tc.getScrollPane(), 2.0);
        AnchorPane.setTopAnchor(tc.getScrollPane(), 2.0);
        AnchorPane.setBottomAnchor(tc.getScrollPane(), 2.0);


        notePane.getChildren().add(noteController.getMainAnchorPane());

        noteController.getMainAnchorPane().setMinSize(300, 590);
        noteController.getMainAnchorPane().setPrefSize(300, 590);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                translationTabPane.getTabs().add(t);
                tc.run();
                noteController.updateNotes();
            }
        });

    }

    @FXML
    private void initialize(){
        startTask();
    }

}
