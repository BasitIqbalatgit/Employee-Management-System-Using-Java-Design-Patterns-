package Controller;

import Controller.LoginController;
import DALManager.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    private LoginController loginController;

    private double x = 0;
    private double y = 0;

    public FXMLDocumentController() {
        this.loginController = new LoginController();
    }

    @FXML
    private void loginAdmin(ActionEvent event) {
        String user = username.getText();
        String pass = password.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else if (loginController.loginAdmin(user, pass)) {
            try {
                // Navigate to the dashboard
                Stage currentStage = (Stage) username.getScene().getWindow();
                currentStage.hide(); // Hide the current window

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/employeemanagementsystem/dashboard.fxml"));
                Parent root = loader.load();

                Stage dashboardStage = new Stage();
                Scene scene = new Scene(root);

                // Make the window draggable
                root.setOnMousePressed(event1 -> {
                    x = event1.getSceneX();
                    y = event1.getSceneY();
                });

                root.setOnMouseDragged(event1 -> {
                    dashboardStage.setX(event1.getScreenX() - x);
                    dashboardStage.setY(event1.getScreenY() - y);
                });

                dashboardStage.initStyle(StageStyle.TRANSPARENT);
                dashboardStage.setScene(scene);
                dashboardStage.show();

                System.out.println("Logged in successfully. Navigating to dashboard.");
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to load the dashboard.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Wrong Username/Password");
            alert.showAndWait();
        }
    }

    @FXML
    private void closeApp(ActionEvent event) {
        // Gracefully close database connection and exit the app
        DatabaseManager.getInstance().closeConnection();
        Stage stage = (Stage) username.getScene().getWindow();
        stage.close();
        System.out.println("Application closed.");
    }
}
