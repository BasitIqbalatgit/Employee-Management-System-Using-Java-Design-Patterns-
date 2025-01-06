package employeemanagementsystem;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertFactory {

    public static Alert createAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }
    
    public static Optional<ButtonType> showConfirmation(String title, String message) {
        Alert alert = createAlert(AlertType.CONFIRMATION, title, message);
        return alert.showAndWait();
    }

    public static void showError(String title, String message) {
        Alert alert = createAlert(AlertType.ERROR, title, message);
        alert.showAndWait();
    }
}

