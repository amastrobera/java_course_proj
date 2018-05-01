package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;


import java.util.ArrayList;

import client.Client;
import university.CanteenUser;


public class UsersController implements Initializable {
    
    @FXML private Label labUsersNotification;
    @FXML private TableView<GuiCanteenUser> tableUsers;    
    private Client mClient;
   
    private void getUsers() {
        ArrayList<CanteenUser> users = mClient.getUsers();
        for (CanteenUser user : users ) {
            tableUsers.getItems().add(new GuiCanteenUser(user));
        }        
        labUsersNotification.setText("Users have been updated from data");
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("UsersController init");
        // TODO
        String host = "localhost";
        int port = 8080;
        mClient = new Client(host, port);
        getUsers();
    }    
    
}
