package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Objects;

public class ToDoListJFX extends Application {
    @Override

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/MainView.fxml")));

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("ToDoListJFX - Version 0.1");
        stage.show();
    }
}
