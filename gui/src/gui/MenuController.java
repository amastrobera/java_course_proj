package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;

import java.util.HashMap;
import java.util.ArrayList;

import client.Client;
import canteen.Course;


public class MenuController implements Initializable {
    
    @FXML private Label labMenuNotification;
    @FXML private ComboBox<String> cboFirst, cboSecond, cboDessert, cboFruit;    
    
    private Client mClient;
    
    private void initItems() {
        HashMap<String, ArrayList<String>> val = mClient.getCourses();
        
        cboFirst.getItems().clear();
        cboFirst.setItems(FXCollections.observableList(val.get("First")));
        
        cboSecond.getItems().clear();
        cboSecond.setItems(FXCollections.observableList(val.get("Second")));
        
        cboDessert.getItems().clear();
        cboDessert.setItems(FXCollections.observableList(val.get("Dessert")));
                
        cboFruit.getItems().clear();
        cboFruit.setItems(FXCollections.observableList(val.get("Fruit")));
        
        labMenuNotification.setText("Meals have been updated from data");
    }

    @FXML
    private void onFirstChanged(ActionEvent event) {
        labMenuNotification.setText("Primo " + cboFirst.getValue());
    }

    @FXML
    private void onSecondChanged(ActionEvent event) {
        labMenuNotification.setText("Secondo " + cboSecond.getValue());
    }

    @FXML
    private void onDessertChanged(ActionEvent event) {
        labMenuNotification.setText("Dolce " + cboDessert.getValue());
    }
    
    @FXML
    private void onFruitChanged(ActionEvent event) {
        labMenuNotification.setText("Fruta " + cboFruit.getValue());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("MenuController init");
        // TODO
        String host = "localhost";
        int port = 8080;
        mClient = new Client(host, port);
        initItems();
        //initListeners();
    }
    
    
//    private class CboListener<String> implements ChangeListener<String> {
//        private final Label labMenuNotification;
//        private final String mType;
//        
//        public CboListener(String type, Label lab) {
//            mType = type;
//            labMenuNotification = lab;
//        }
//        
//        @Override
//        public void changed(ObservableValue<? extends String> selected, 
//                            String oldValue, String newValue) {
//            if (!newValue.equals(oldValue)) {
//                labMenuNotification.setText(mType + " " + newValue);
//            }
//        }
//    }
//    
//    private void initListeners() {        
//        
//        cboFirst.getSelectionModel().selectedItemProperty().addListener(
//                new CboListener<>("Primo piatto: ", labMenuNotification));
//        
//        cboSecond.getSelectionModel().selectedItemProperty().addListener(
//                new CboListener<>("Secondo piatto: ", labMenuNotification));
//        
//        cboDessert.getSelectionModel().selectedItemProperty().addListener(
//                new CboListener<>("Dessert: ", labMenuNotification));
//        
//        cboFruit.getSelectionModel().selectedItemProperty().addListener(
//                new CboListener<>("Frutta: ", labMenuNotification));
//    }

    
}