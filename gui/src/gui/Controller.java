package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;


public class Controller implements Initializable {
    
    @FXML private CourseController mCourseController;
    @FXML private MenuController mMenuController;
    @FXML private MenuPlanController mMenuPlanController;
    @FXML private UsersController mUserController;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Controller init");
    }    
    
}
