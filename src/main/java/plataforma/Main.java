package plataforma;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Plataforma de Tradução Show!");
        primaryStage.setScene(new Scene(root, 1350, 650));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
