
package employeemanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private Button close;
    
//    DATABASE TOOLS
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private double x = 0;
    private double y = 0;
    
    public void loginAdmin(){
        
        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            
            result = prepare.executeQuery();
            Alert alert;
            
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                if(result.next()){
                    getData.username = username.getText();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login");
                    alert.showAndWait();
                    
                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    
                    root.setOnMousePressed((MouseEvent event) ->{
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });
                    
                    root.setOnMouseDragged((MouseEvent event) ->{
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });
                    
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                    
                }else{
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password");
                    alert.showAndWait();
                }
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void closeApp(){
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}


//
//package employeemanagementsystem;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//
//public class FXMLDocumentController {
//
//    @FXML
//    private TextField username;
//    @FXML
//    private PasswordField password;
//    @FXML
//    private Button loginBtn;
//
//    private double x = 0;
//    private double y = 0;
//
//    // Reference to the model
//    private AdminLoginModel adminLoginModel = new AdminLoginModel();
//
//    // Handles the login process
//    public void loginAdmin() {
//        String user = username.getText();
//        String pass = password.getText();
//
//        if (user.isEmpty() || pass.isEmpty()) {
//            AlertFactory.createAlert(AlertType.ERROR, "Error", "Please fill all fields").showAndWait();
//            return;
//        }
//
//        boolean isAuthenticated = adminLoginModel.authenticate(user, pass);
//
//        if (isAuthenticated) {
//            // Successful login
//            getData.username = user;
//            AlertFactory.createAlert(AlertType.INFORMATION, "Success", "Login Successful").showAndWait();
//            openDashboard();
//        } else {
//            // Failed login
//            AlertFactory.createAlert(AlertType.ERROR, "Error", "Invalid Username or Password").showAndWait();
//        }
//    }
//
//    // Opens the dashboard screen
//    private void openDashboard() {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
//            Stage stage = new Stage();
//            Scene scene = new Scene(root);
//
//            // Make the window draggable
//            root.setOnMousePressed((MouseEvent event) -> {
//                x = event.getSceneX();
//                y = event.getSceneY();
//            });
//
//            root.setOnMouseDragged((MouseEvent event) -> {
//                stage.setX(event.getScreenX() - x);
//                stage.setY(event.getScreenY() - y);
//            });
//
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.setScene(scene);
//            stage.show();
//
//            // Close the login window
//            loginBtn.getScene().getWindow().hide();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void closeApp() {
//        System.exit(0);
//    }
//}
