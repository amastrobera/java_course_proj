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
    private HashMap<String, ArrayList<String>> mCourses;
    
    private void initItems() {
        mCourses = mClient.getCourses();
        long num = mCourses.get("First").size() + mCourses.get("Second").size() 
              + mCourses.get("Dessert").size() + mCourses.get("Fruit").size();
        
        cboFirst.getItems().clear();
        cboFirst.setItems(FXCollections.observableList(mCourses.get("First")));
        
        cboSecond.getItems().clear();
        cboSecond.setItems(FXCollections.observableList(mCourses.get("Second")));
        
        cboDessert.getItems().clear();
        cboDessert.setItems(FXCollections.observableList(mCourses.get("Dessert")));
                
        cboFruit.getItems().clear();
        cboFruit.setItems(FXCollections.observableList(mCourses.get("Fruit")));
        
        tableUsers.getItems().clear();
        
        labNotif.setText(num + " courses loaded from database");
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
        
        // dessert and fruit mutually exclusive
        cboFruit.setValue("Fruit");
        //cboFruit.getItems().clear();
        //cboFruit.setItems(FXCollections.observableList(mCourses.get("Fruit")));
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
        
        // dessert and fruit mutually exclusive
        cboDessert.setValue("Dessert");
        //cboDessert.getItems().clear();
        //cboDessert.setItems(FXCollections.observableList(mCourses.get("Dessert")));
    }
    
    @FXML
    private void onGetUsers(ActionEvent event) {
        Menu menu = new Menu();
        menu.setCourse(cboFirst.getValue(), Course.Type.First);
        menu.setCourse(cboSecond.getValue(), Course.Type.Second);
        // dessert and fruit mutually exclusive
        if (cboDessert.getValue().equals("Dessert"))
            menu.setCourse(cboFruit.getValue(), Course.Type.Fruit);
        else 
            menu.setCourse(cboDessert.getValue(), Course.Type.Dessert);

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
        if (cboFirst.getValue().equals("First course")) {
            labNotif.setText("error in saving: missing first course");
            return ;
        }
        if (cboSecond.getValue().equals("Second course")) {
            labNotif.setText("error in saving: missing second course");
            return ;
        }
        if (cboDessert.getValue().equals("Dessert") && 
            cboFruit.getValue().equals("Fruit")) {
            labNotif.setText("error in saving: missing either dessert or fruit");
            return ;
        }
        
        String date = dtpDate.getValue().format(DateTimeFormatter.ISO_DATE);
        Menu menu = new Menu();
        menu.setName(name);
        menu.setDate(date);
        menu.setCourse(cboFirst.getValue(), Course.Type.First);
        menu.setCourse(cboSecond.getValue(), Course.Type.Second);
        // dessert and fruit mutually exclusive
        if (cboDessert.getValue().equals("Dessert"))
            menu.setCourse(cboFruit.getValue(), Course.Type.Fruit);
        else
            menu.setCourse(cboDessert.getValue(), Course.Type.Dessert);
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