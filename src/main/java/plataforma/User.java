package plataforma;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;


public class User {

    private String translationCollectionName;
    private String noteCollectionName;

    NoteController noteController;
    TranslationController translationController;
    AnchorPane translationAnchorPane = new AnchorPane();
    AnchorPane noteAnchorPane = new AnchorPane();

    public User(String tr, String nt){
        translationCollectionName = tr;
        noteCollectionName = nt;
        //noteTabPane = ntp;
        //translationTabPane = ttp;

    }

    public void connect(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                connectToDatabase();
            }
        };

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(false);
        backgroundThread.setName("userThread");
        backgroundThread.start();
    }

    private void connectToDatabase(){

        noteController = new NoteController(noteCollectionName);

        translationController = new TranslationController(noteController, translationCollectionName);

        translationAnchorPane.getChildren().add(translationController.getScrollPane());

        AnchorPane.setRightAnchor(translationController.getScrollPane(), 2.0);
        AnchorPane.setLeftAnchor(translationController.getScrollPane(), 2.0);
        AnchorPane.setTopAnchor(translationController.getScrollPane(), 2.0);
        AnchorPane.setBottomAnchor(translationController.getScrollPane(), 2.0);

        //notePane.getChildren().add(noteController.getMainAnchorPane());

        noteController.getMainAnchorPane().setMinSize(270, 590);
        noteController.getMainAnchorPane().setPrefSize(270, 590);

        noteAnchorPane = noteController.getMainAnchorPane();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    noteController.initNoteCards();
                } finally {
                    translationController.initTranslationCards();
                }

            }
        });
    }

}
