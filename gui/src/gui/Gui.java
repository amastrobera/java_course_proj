package gui;

import javafx.application.Application;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
    
public class Gui extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        URL resource = getClass().getResource("Main.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = (Parent)loader.load();
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Meals and Allergies");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
