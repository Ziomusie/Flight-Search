package searchApp;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import searchApp.helpers.FXMLResources;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(FXMLResources.MAIN_SCENE));
        primaryStage.setTitle("Wyszukiwarka");
		primaryStage.getIcons().add(new Image("icons/icon.png"));
		Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.getProperties().put("hostServices", this.getHostServices());
        primaryStage.show();
		scene.getStylesheets().add("css/datepicker.css");
		scene.getStylesheets().add("css/jfoenix-components.css");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
