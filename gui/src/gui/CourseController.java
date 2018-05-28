package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.collections.FXCollections;

import client.Client;
import canteen.Course;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.event.ActionEvent;


public class CourseController implements Initializable {
        
    @FXML private TextField txtName;
    @FXML private TextArea txtIngredients;
    @FXML private ComboBox<String> cboType, cboName;
    @FXML private Label labNotif;
    
    private HashMap<String, ArrayList<String>> mCourses;
    private Client mClient;
    
    private void initItems() {
        mCourses = mClient.getCourses();
        
        if (cboType.getItems().isEmpty()) {
            cboType.getItems().clear();
            cboType.setItems(FXCollections.observableList(
                        Arrays.asList("First", "Second", "Dessert", "Fruit")));
        }
        else 
            cboType.setValue("Course type");
        
        if (cboName.getItems().isEmpty())
            cboName.getItems().clear();
        else 
            cboName.setValue("Name of a saved course");
        
        txtName.clear();
        txtName.setText("");
        
        txtIngredients.clear();
        txtIngredients.setText("");
        
        long num = mCourses.get("First").size() + mCourses.get("Second").size() 
                + mCourses.get("Dessert").size() + mCourses.get("Fruit").size();
        
        labNotif.setText(num + " courses loaded from the database");
    }
    
    @FXML
    private void onTypeChanged(ActionEvent event) {
        
        ArrayList<String> savedCourses = mCourses.get(cboType.getValue());
        labNotif.setText("selected " + cboType.getValue() + " (" + 
                        savedCourses.size() + " saved options)");
        
        cboName.getItems().clear();
        cboName.setItems(FXCollections.observableList(savedCourses));
        
        txtName.clear();
        txtName.setText("");
        
        txtIngredients.clear();
        txtIngredients.setText("");        
    }
    
    @FXML
    private void onNameChanged(ActionEvent event) {

        Course course = mClient.getCourseInfo(cboName.getValue());
        
        txtName.setText(course.name);
        String ingredients = new String();
        for (String ing : course.ingredients) {
            ingredients += ing + "\n";
        }
        txtIngredients.setText(ingredients);
    }
    

    @FXML
    private void onRefresh(ActionEvent event) {
        initItems();
    }

    
    @FXML
    private void onSave(ActionEvent event) {
        if (txtName.getText().isEmpty() ) {
            labNotif.setText("error in saving: missing name");
            return;
        }
        if (txtIngredients.getText().isEmpty()) {
            labNotif.setText("error in saving: missing ingredients");
            return;
        }
        if (cboType.getValue().isEmpty() || 
            cboType.getValue().equals("Course type")) {
            labNotif.setText("error in saving: missing type");
            return;
        }
        Course course = new Course();
        course.name = txtName.getText();
        course.type = Course.strToType(cboType.getValue());
        String ingredients = txtIngredients.getText();
        for (String line : ingredients.split("\n"))
            for (String col : line.split(","))
                course.ingredients.add(col.trim());
        
        if (mClient.saveCourse(course))
            labNotif.setText("course saved");
        else
            labNotif.setText("failed to saved course");
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("CourseController init");
        // TODO
        String host = "localhost";
        int port = 8080;
        mClient = new Client(host, port);
        initItems();
    }
    
}