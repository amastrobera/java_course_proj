package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.util.HashSet;

import canteen.*;
import client.Client;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class MenuPlanController implements Initializable {


    @FXML private Label labNotif;
    @FXML private TableView<GuiMenu> tableMenus;
    
    private Client mClient;

    
    private void getMenus() {
        HashSet<Menu> menus = mClient.getMenus();
        for (Menu menu : menus ) {
            tableMenus.getItems().add(new GuiMenu(menu));
        }        
        labNotif.setText(menus.size() + " menus have been updated from data");
    }
    
    @FXML
    private void onRefresh(ActionEvent event) {
        getMenus();
    }
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("UsersController init");
        // TODO
        String host = "localhost";
        int port = 8080;
        mClient = new Client(host, port);
        getMenus();
    }    
    
}
