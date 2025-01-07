
    package Controller;

    import DALManager.AdminDAO;
    import javafx.scene.control.Alert;

    public class LoginController {

        private AdminDAO adminDAO;

        public LoginController() {
            this.adminDAO = new AdminDAO();
        }

        public boolean loginAdmin(String username, String password) {
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill all blank fields", Alert.AlertType.ERROR);
                return false;
            }

            if (adminDAO.validateAdmin(username, password)) {
                showAlert("Success", "Successfully Logged In", Alert.AlertType.INFORMATION);
                return true;
            } else {
                showAlert("Error", "Wrong Username/Password", Alert.AlertType.ERROR);
                return false;
            }
        }

        private void showAlert(String title, String content, Alert.AlertType alertType) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }
    }
