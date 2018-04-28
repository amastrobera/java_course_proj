package main_gui;

import client.Client;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class Controller {
    public Label labelUsers;
    
    private Client mClient;
    
    public Controller(){
        mClient = new Client("localhost", 8080);
    }
    
    public void getUsers(ActionEvent event) {
        String val = mClient.getUsers().toString();
        labelUsers.setText(val);
    }
}
