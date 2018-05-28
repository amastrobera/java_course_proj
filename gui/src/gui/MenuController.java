package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;


import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import client.Client;
import canteen.*;
import university.*;


public class MenuController implements Initializable {
    
    @FXML private Label labDescription, labNotif;
    @FXML private ComboBox<String> cboFirst, cboSecond, cboDessert, cboFruit;    
    @FXML private TextField txtName;
    @FXML private DatePicker dtpDate;
    @FXML private TableView<GuiCanteenUser> tableUsers;    
    
    private Client mClient;
    
    private void initItems() {
        HashMap<String, ArrayList<String>> val = mClient.getCourses();
        long num = val.get("First").size() + val.get("Second").size() +
                    val.get("Dessert").size() + val.get("Fruit").size();
        
        cboFirst.getItems().clear();
        cboFirst.setItems(FXCollections.observableList(val.get("First")));
        
        cboSecond.getItems().clear();
        cboSecond.setItems(FXCollections.observableList(val.get("Second")));
        
        cboDessert.getItems().clear();
        cboDessert.setItems(FXCollections.observableList(val.get("Dessert")));
                
        cboFruit.getItems().clear();
        cboFruit.setItems(FXCollections.observableList(val.get("Fruit")));
        
        tableUsers.getItems().clear();
        
        labNotif.setText(num + " meals have been updated from data base");
    }

    
    @FXML
    private void onFirstChanged(ActionEvent event) {
        Course course = mClient.getCourseInfo(cboFirst.getValue());
        String notif = course.name + 
                       " (" + Course.typeToString(course.type) + ")\n" +
                       "---------------------------------------------\n";
        for (String i : course.ingredients)
            notif += i + "\n";
        labDescription.setText(notif);
        tableUsers.getItems().clear();

    }

    @FXML
    private void onSecondChanged(ActionEvent event) {
        Course course = mClient.getCourseInfo(cboSecond.getValue());
        String notif = course.name + 
                       " (" + Course.typeToString(course.type) + ")\n" +
                        "---------------------------------------------\n";
        for (String i : course.ingredients)
            notif += i + "\n";
        labDescription.setText(notif);
        tableUsers.getItems().clear();
    }

    @FXML
    private void onDessertChanged(ActionEvent event) {
        Course course = mClient.getCourseInfo(cboDessert.getValue());
        String notif = course.name + 
                       " (" + Course.typeToString(course.type) + ")\n" +
                        "---------------------------------------------\n";
        for (String i : course.ingredients)
            notif += i + "\n";
        labDescription.setText(notif);
        tableUsers.getItems().clear();
    }
    
    @FXML
    private void onFruitChanged(ActionEvent event) {
        Course course = mClient.getCourseInfo(cboFruit.getValue());
        String notif = course.name + 
                       " (" + Course.typeToString(course.type) + ")\n" +
                        "---------------------------------------------\n";
        for (String i : course.ingredients)
            notif += i + "\n";
        labDescription.setText(notif);
        tableUsers.getItems().clear();
    }
    
    @FXML
    private void onGetUsers(ActionEvent event) {
        Menu menu = new Menu();
        menu.setCourse(cboFirst.getValue(), Course.Type.First);
        menu.setCourse(cboSecond.getValue(), Course.Type.Second);
        menu.setCourse(cboDessert.getValue(), Course.Type.Dessert);
        menu.setCourse(cboFruit.getValue(), Course.Type.Fruit);
        ArrayList<CanteenUser> users = mClient.getAllergicUsers(menu);
        for (CanteenUser user : users ) {
            tableUsers.getItems().add(new GuiCanteenUser(user));
        }        
        labNotif.setText(users.size() + " allergic users found");
    }

    @FXML
    private void onSave(ActionEvent event) {
        String name = txtName.getText();
        LocalDate ldate = dtpDate.getValue();
        if (ldate == null) {
            labNotif.setText("error in saving: missing date");
            return ;
        }
        String date = dtpDate.getValue().format(DateTimeFormatter.ISO_DATE);
        Menu menu = new Menu();
        menu.setName(name);
        menu.setDate(date);
        menu.setCourse(cboFirst.getValue(), Course.Type.First);
        menu.setCourse(cboSecond.getValue(), Course.Type.Second);
        menu.setCourse(cboDessert.getValue(), Course.Type.Dessert);
        menu.setCourse(cboFruit.getValue(), Course.Type.Fruit);
        if (mClient.saveMenu(menu))
            labNotif.setText("menu saved");
        else
            labNotif.setText("failed to save menu");
    }

    @FXML
    private void onRefresh(ActionEvent event) {
        initItems();
    }


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("MenuController init");
        // TODO
        String host = "localhost";
        int port = 8080;
        mClient = new Client(host, port);
        initItems();
    }

}