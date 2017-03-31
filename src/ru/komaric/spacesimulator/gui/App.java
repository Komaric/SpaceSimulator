package ru.komaric.spacesimulator.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.komaric.spacesimulator.SpaceSimulator;

class App extends Application {

    private SpaceSimulator spaceSimulator;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
        AnchorPane root = loader.load();
        Controller controller = loader.getController();
        spaceSimulator = new SpaceSimulator(1000);
        controller.initialize(spaceSimulator, primaryStage);

        primaryStage.setTitle("Space Simulator");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (spaceSimulator.isRunning()) {
            spaceSimulator.stop();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
