package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    static UIController controller;

    @Override
    public void start(Stage firstStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI.fxml"));
        Parent root = fxmlLoader.load();

        controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 1024, 768);

        firstStage.setTitle("Feri && Boti DBMS");
        firstStage.setScene(scene);
        firstStage.setResizable(false);
        firstStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
