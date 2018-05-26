package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

import javafx.collections.FXCollections;

import client.Client;
import canteen.Course;
import java.util.Arrays;


public class MealController implements Initializable {
        
    @FXML private TextField txtName, txtIngredients;
    @FXML private ComboBox<String> cboType;
    private Client mClient;
    
    public void updateMealInfo(String name) {
        Course course = mClient.getCourseInfo(name);
        System.out.println("updateMealInfo: " + course);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("MealController init");
        // TODO
        String host = "localhost";
        int port = 8080;
        mClient = new Client(host, port);
        
        cboType.getItems().clear();
        cboType.setItems(FXCollections.observableList(
                         Arrays.asList("Primo", "Secondo", "Dolce", "Frutta")));
        
    }
    
}