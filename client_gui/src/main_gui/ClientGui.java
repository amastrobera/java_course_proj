package main_gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import com.sun.javaws.Launcher;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;

public class ClientGui extends Application {
    
    
    @Override
    public void start(Stage stage) {
        
        Parent root = null;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("client_gui/views/main.fxml"), 
                    ResourceBundle.getBundle("client_gui.ClientGui")
            );
        } catch(Exception ex) {
            System.err.println("ClientGui::start() " + ex);
            return;
        }
        stage.setTitle("Menu");
        stage.setScene(new Scene(root, 300, 250));
        stage.show();
    }

    public static void main(String[] args) {
        
//        System.out.println("--- ClientGui --- ");
//        
//        if (args.length < 2) {
//            System.err.println("missing arguments");
//            System.out.println("$ java ClientGui host-name port-number");
//            System.exit(1);
//        }
//                
//        String host = args[0];
//        int port = Integer.valueOf(args[1]);
//
//        ClientGui gui = new ClientGui(host, port);
//        gui.launch(args);

        launch(args);

    }
    
}
