package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import client.Client;
import university.*;


public class UsersController implements Initializable {
    
    @FXML private Label labNotif;
    @FXML private TableView<GuiCanteenUser> tableUsers;
    @FXML private TextField txtName;
    @FXML private TextField txtSurname;
    @FXML private ComboBox<String> cboType;
    @FXML private TextField txtPhone;
    @FXML private TextField txtStreet;
    @FXML private TextField txtPostcode;
    @FXML private TextField txtCity;
    @FXML private TextField txtFather;
    @FXML private TextField txtMother;
    @FXML private TextField txtAllergies;
    
    private Client mClient;
   
    private void getUsers() {
        ArrayList<CanteenUser> users = mClient.getUsers();
        for (CanteenUser user : users ) {
            tableUsers.getItems().add(new GuiCanteenUser(user));
        }        
        labNotif.setText(users.size() + " users have been updated from data");
    }
    
    private void initItems() {
        
        tableUsers.getItems().clear();
        tableUsers.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null && newSelection != oldSelection) {
                    txtName.setText(newSelection.getName());
                    txtSurname.setText(newSelection.getSurname());
                    cboType.setValue(newSelection.getType());
                    txtPhone.setText(newSelection.getPhone());
                    Address address = newSelection.getAddress();
                    txtStreet.setText(address.street);
                    txtPostcode.setText(address.postcode);
                    txtCity.setText(address.city);
                    txtFather.setText(newSelection.getFather());
                    txtMother.setText(newSelection.getMother());
                    txtAllergies.setText(newSelection.getAllergies());
                }
            }
        );
        
        txtName.clear();
        txtName.setText("");
        
        txtSurname.clear();
        txtSurname.setText("");
                
        txtPhone.clear();
        txtPhone.setText("");
        
        txtStreet.clear();
        txtStreet.setText("");
        
        txtPostcode.clear();
        txtPostcode.setText("");
        
        txtCity.clear();
        txtCity.setText("");
        
        txtFather.clear();
        txtFather.setText("");
        
        txtMother.clear();
        txtMother.setText("");
        
        txtAllergies.clear();
        txtAllergies.setText("");
        
        if (cboType.getItems().isEmpty()) {
            cboType.getItems().clear();
            cboType.setItems(FXCollections.observableList(
                            Arrays.asList("student", "professor")));
        }
        else 
          cboType.setValue("User Type");
    }
    
    @FXML
    private void onSave(ActionEvent event) {
        
        String name = txtName.getText().trim();
        String surname = txtSurname.getText().trim();
        String type = cboType.getValue();
        String phone = txtPhone.getText().trim();
        String street = txtStreet.getText().trim();
        String postcode = txtPostcode.getText().trim();
        String city = txtCity.getText().trim();
        String father = txtFather.getText().trim();
        String mother = txtMother.getText().trim();
        String allergies = txtAllergies.getText();
        
        if (type.isEmpty() || type.equals("User Type")) {
            labNotif.setText("error in saving: missing type");
            return ;
        }
        if (name.isEmpty()) {
            labNotif.setText("error in saving: missing name");
            return ;
        }
        if (surname.isEmpty()) {
            labNotif.setText("error in saving: missing surname");
            return ;
        }
        if (phone.isEmpty()) {
            labNotif.setText("error in saving: missing phone");
            return ;
        }
        if (street.isEmpty()) {
            labNotif.setText("error in saving: missing street");
            return ;
        }
        if (postcode.isEmpty()) {
            labNotif.setText("error in saving: missing postcode");
            return ;
        }
        if (city.isEmpty()) {
            labNotif.setText("error in saving: missing city");
            return ;
        }
        if (type.equals("student") && (father.isEmpty() && mother.isEmpty())) {
            labNotif.setText("error in saving: missing father or mother for "
                            + "a student user");
            return;
        }
        
        CanteenUser user;
        if (type.equals("student")) {
            user = new Student(name, surname);
            Person[] parents = new Person[2];
            if (!father.isEmpty()) {
                String[] arrFather = father.split(" ");
                if (arrFather.length < 2) {
                    labNotif.setText("error in saving: father is missing name" +
                                    " or surname");
                    return;
                }
                parents[0] = new Person(arrFather[0], arrFather[1]);
                if (arrFather.length > 2) 
                    parents[0].setPhone(arrFather[2]);
            }
            if (!mother.isEmpty()) {
                String[] arrMother = mother.split(" ");
                if (arrMother.length < 2) {
                    labNotif.setText("error in saving: mother is missing name" +
                                    " or surname");
                    return;
                }
                parents[1] = new Person(arrMother[0], arrMother[1]);
                if (arrMother.length > 2) 
                    parents[1].setPhone(arrMother[2]);
            }
            ((Student)user).setParents(parents);
        } else {
            user = new Professor(name, surname);
        }
        user.setPhone(phone);
        user.setAddress(new Address(street, postcode, city));
        
        HashSet<String> setAllergies = new HashSet<>();
        String[] all = allergies.split(",");
        for (String a : all)
            setAllergies.add(a.trim());
        user.setAllergies(setAllergies);
        
        if (mClient.saveUser(user))
            labNotif.setText("user saved");
        else
            labNotif.setText("failed to save user");
    }

    @FXML
    private void onRefresh(ActionEvent event) {
        initItems();
        getUsers();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("UsersController init");
        // TODO
        String host = "localhost";
        int port = 8080;
        mClient = new Client(host, port);
        initItems();
        getUsers();
    }    
    
}
