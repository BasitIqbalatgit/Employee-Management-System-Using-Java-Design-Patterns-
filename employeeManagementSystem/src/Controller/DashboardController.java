
package Controller;

import DALManager.DatabaseManager;
import DALManager.EmployeeDAO;
import employeemanagementsystem.employeeData;
import employeemanagementsystem.getData;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label username;

    @FXML
    private Button home_btn;

    @FXML
    private Button addEmployee_btn;

    @FXML
    private Button salary_btn;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalEmployees;

    @FXML
    private Label home_totalPresents;

    @FXML
    private Label home_totalInactiveEm;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane addEmployee_form;

    @FXML
    private TableView<employeeData> addEmployee_tableView;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_employeeID;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_firstName;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_lastName;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_gender;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_phoneNum;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_position;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_date;

    @FXML
    private TextField addEmployee_search;

    @FXML
    private TextField addEmployee_employeeID;

    @FXML
    private TextField addEmployee_firstName;

    @FXML
    private TextField addEmployee_lastName;

    @FXML
    private ComboBox<?> addEmployee_gender;

    @FXML
    private TextField addEmployee_phoneNum;

    @FXML
    private ComboBox<?> addEmployee_position;

    @FXML
    private ImageView addEmployee_image;

    @FXML
    private Button addEmployee_importBtn;

    @FXML
    private Button addEmployee_addBtn;

    @FXML
    private Button addEmployee_updateBtn;

    @FXML
    private Button addEmployee_deleteBtn;

    @FXML
    private Button addEmployee_clearBtn;

    @FXML
    private AnchorPane salary_form;

    @FXML
    private TextField salary_employeeID;

    @FXML
    private Label salary_firstName;

    @FXML
    private Label salary_lastName;

    @FXML
    private Label salary_position;

    @FXML
    private TextField salary_salary;

    @FXML
    private Button salary_updateBtn;

    @FXML
    private Button salary_clearBtn;

    @FXML
    private TableView<employeeData> salary_tableView;

    @FXML
    private TableColumn<employeeData, String> salary_col_employeeID;

    @FXML
    private TableColumn<employeeData, String> salary_col_firstName;

    @FXML
    private TableColumn<employeeData, String> salary_col_lastName;

    @FXML
    private TableColumn<employeeData, String> salary_col_position;

    @FXML
    private TableColumn<employeeData, String> salary_col_salary;

    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    private Image image;
    // Basit ..........
    
    private EmployeeDAO employeeDao;
    public DashboardController(){
        employeeDao  = new EmployeeDAO();
    }
    
    //---------------
     // Method to count total employees
    public void homeTotalEmployees() {
//        String sql = "SELECT COUNT(employee_id) AS total FROM employee";
//        int totalEmployees = executeCountQuery(sql);
        int totalEmployees = employeeDao.getTotalEmployees();
        home_totalEmployees.setText(String.valueOf(totalEmployees));
    }

    // Method to count total present employees
    public void homeEmployeeTotalPresent() {
//        String sql = "SELECT COUNT(employee_id) AS total FROM employee_info";
//        int totalPresents = executeCountQuery(sql);
        int totalPresents = employeeDao.getTotalPresentEmployees();
        home_totalPresents.setText(String.valueOf(totalPresents));
    }

    // Method to count total inactive employees
    public void homeTotalInactive() {
//        String sql = "SELECT COUNT(employee_id) AS total FROM employee_info WHERE salary = 0.0";
//        int totalInactive = executeCountQuery(sql);
            int totalInactive= employeeDao.getTotalInactiveEmployees();
        home_totalInactiveEm.setText(String.valueOf(totalInactive));
    }

    

    public void homeChart() {

        home_chart.getData().clear();

        String sql = "SELECT date, COUNT(employee_id) FROM employee GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 7";
        
        connect = DatabaseManager.getInstance().getConnection();

        try {
            XYChart.Series chart = new XYChart.Series();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            home_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    public void addEmployeeAdd() {
    try {
        // Check if any required field is empty
        if (isAnyFieldEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");
            return;
        }

        // Call the EmployeeDAO `addEmployee` method
        boolean isAdded = employeeDao.addEmployee(
                addEmployee_employeeID.getText(),
                addEmployee_firstName.getText(),
                addEmployee_lastName.getText(),
                (String) addEmployee_gender.getSelectionModel().getSelectedItem(),
                addEmployee_phoneNum.getText(),
                (String) addEmployee_position.getSelectionModel().getSelectedItem(),
                getData.path
        );

        if (isAdded) {
            showAlert(Alert.AlertType.INFORMATION, "Information Message", "Employee successfully added!");
            addEmployeeShowListData();
            addEmployeeReset();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Failed to add employee. Please try again!");
        }

    } catch (Exception e) {
        System.err.println("Error adding employee: " + e.getMessage());
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error Message", "An unexpected error occurred!");
    }
}


private boolean isAnyFieldEmpty() {
    return addEmployee_employeeID.getText().isEmpty()
            || addEmployee_firstName.getText().isEmpty()
            || addEmployee_lastName.getText().isEmpty()
            || addEmployee_gender.getSelectionModel().getSelectedItem() == null
            || addEmployee_phoneNum.getText().isEmpty()
            || addEmployee_position.getSelectionModel().getSelectedItem() == null
            || getData.path == null || getData.path.trim().isEmpty();
}


private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

public void addEmployeeUpdate() {
    try {
        // Validate input fields
        if (addEmployee_employeeID.getText().isEmpty() ||
            addEmployee_firstName.getText().isEmpty() ||
            addEmployee_lastName.getText().isEmpty() ||
            addEmployee_gender.getSelectionModel().getSelectedItem() == null ||
            addEmployee_phoneNum.getText().isEmpty() ||
            addEmployee_position.getSelectionModel().getSelectedItem() == null) {

            showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields.");
            return;
        }

        // Confirm update action
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation Message");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to UPDATE Employee ID: " + addEmployee_employeeID.getText() + "?");
        Optional<ButtonType> option = confirmationAlert.showAndWait();

        if (option.isPresent() && option.get().equals(ButtonType.OK)) {
            int employeeId = Integer.parseInt(addEmployee_employeeID.getText());
            String firstName = addEmployee_firstName.getText();
            String lastName = addEmployee_lastName.getText();
            String gender = (String) addEmployee_gender.getSelectionModel().getSelectedItem();
            String phoneNum = addEmployee_phoneNum.getText();
            String position = (String) addEmployee_position.getSelectionModel().getSelectedItem();

            // Call EmployeeDAO to perform the update
            
            boolean isUpdated = employeeDao.updateEmployee(employeeId, firstName, lastName, gender, position, phoneNum);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Information Message", "Successfully Updated!");
                addEmployeeShowListData();
                addEmployeeReset();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error Message", "Failed to update employee.");
            }
        }
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Error Message", "Invalid Employee ID.");
    } catch (Exception e) {
        showAlert(Alert.AlertType.ERROR, "Error Message", "An error occurred: " + e.getMessage());
        e.printStackTrace();
    }
}



//      Delete Functionality...............
public void addEmployeeDelete() {
    // Validate employee ID field
    if (addEmployee_employeeID.getText().isEmpty()) {
        showAlert(Alert.AlertType.ERROR, "Error Message", "Please enter an Employee ID to delete.");
        return;
    }

    try {
        // Confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation Message");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to DELETE Employee ID: " + addEmployee_employeeID.getText() + "?");
        Optional<ButtonType> option = confirmationAlert.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            // Parse employee ID
            int employeeId = Integer.parseInt(addEmployee_employeeID.getText());

            // DAO integration
            boolean isDeleted = employeeDao.deleteEmployee(employeeId);

            // Handle success or failure
            if (isDeleted) {
                showAlert(Alert.AlertType.INFORMATION, "Information Message", "Successfully Deleted!");
                addEmployeeShowListData();
                addEmployeeReset();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error Message", "Employee ID not found. Deletion failed.");
            }
        }
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Error Message", "Invalid Employee ID format. Please enter a numeric value.");
    } catch (Exception e) {
        showAlert(Alert.AlertType.ERROR, "Error Message", "An unexpected error occurred: " + e.getMessage());
    }
}


    public void addEmployeeReset() {
        addEmployee_employeeID.setText("");
        addEmployee_firstName.setText("");
        addEmployee_lastName.setText("");
        addEmployee_gender.getSelectionModel().clearSelection();
        addEmployee_position.getSelectionModel().clearSelection();
        addEmployee_phoneNum.setText("");
        addEmployee_image.setImage(null);
        getData.path = "";
    }

    public void addEmployeeInsertImage() {

        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 101, 127, false, true);
            addEmployee_image.setImage(image);
        }
    }

    private String[] positionList = {"Market Coordinator", "Web Developer (Back End)", "Web Developer (Front End)", "App Developer"};

    public void addEmployeePositionList() {
        List<String> listP = new ArrayList<>();

        for (String data : positionList) {
            listP.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listP);
        addEmployee_position.setItems(listData);
    }

    private String[] listGender = {"Male", "Female", "Others"};

    public void addEmployeeGendernList() {
        List<String> listG = new ArrayList<>();

        for (String data : listGender) {
            listG.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listG);
        addEmployee_gender.setItems(listData);
    }

    public void addEmployeeSearch() {

        FilteredList<employeeData> filter = new FilteredList<>(addEmployeeList, e -> true);

        addEmployee_search.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateEmployeeData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateEmployeeData.getEmployeeId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getFirstName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getLastName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getPhoneNum().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getPosition().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getDate().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<employeeData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(addEmployee_tableView.comparatorProperty());
        addEmployee_tableView.setItems(sortList);
    }

    public ObservableList<employeeData> addEmployeeListData() {
    ObservableList<employeeData> listData = FXCollections.observableArrayList();
    String sql = "SELECT e.employee_id, e.firstName, e.lastName, e.gender, e.phoneNum, p.position_name AS position, e.image, e.date FROM employee e LEFT JOIN position p ON e.position_id = p.position_id";
    connect = DatabaseManager.getInstance().getConnection();

    try {
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();

        while (result.next()) {
            employeeData employeeD = new employeeData(
                    result.getInt("employee_id"),
                    result.getString("firstName"),
                    result.getString("lastName"),
                    result.getString("gender"),
                    result.getString("phoneNum"),
                    result.getString("position"),  // Corrected to fetch position_name
                    result.getString("image"),
                    result.getDate("date")
            );
            listData.add(employeeD);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return listData;
}

    private ObservableList<employeeData> addEmployeeList;

    public void addEmployeeShowListData() {
    addEmployeeList = addEmployeeListData();

    // Debugging - print data once
    System.out.println("Loaded employee list: " + addEmployeeList);

    // Set up table columns
    addEmployee_col_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
    addEmployee_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    addEmployee_col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    addEmployee_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    addEmployee_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
    addEmployee_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
    addEmployee_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

    // Bind data to the TableView
    addEmployee_tableView.setItems(addEmployeeList);
}


    public void addEmployeeSelect() {
        employeeData employeeD = addEmployee_tableView.getSelectionModel().getSelectedItem();
        int num = addEmployee_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        addEmployee_employeeID.setText(String.valueOf(employeeD.getEmployeeId()));
        addEmployee_firstName.setText(employeeD.getFirstName());
        addEmployee_lastName.setText(employeeD.getLastName());
        addEmployee_phoneNum.setText(employeeD.getPhoneNum());

        getData.path = employeeD.getImage();

        String uri = "file:" + employeeD.getImage();

        image = new Image(uri, 101, 127, false, true);
        addEmployee_image.setImage(image);
    }
    
    

    public void salaryUpdate() {
    Alert alert;

    // Validate required fields
    if (salary_employeeID.getText().isEmpty() || salary_salary.getText().isEmpty()) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please select an employee and provide a valid salary.");
        alert.showAndWait();
        return;
    }

    double salary;
    int employeeId;

    // Parse salary and employee ID with validation
    try {
        salary = Double.parseDouble(salary_salary.getText());
        employeeId = Integer.parseInt(salary_employeeID.getText());

        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
    } catch (NumberFormatException e) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Invalid salary or Employee ID input! Please ensure numeric values are entered.");
        alert.showAndWait();
        return;
    } catch (IllegalArgumentException e) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        return;
    }

    // Use DAO to update the salary
    EmployeeDAO employeeDAO = new EmployeeDAO();
    boolean isUpdated = employeeDAO.updateEmployeeSalary(employeeId, salary);

    // Show appropriate alert based on the result
    if (isUpdated) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText("Salary successfully updated!");
        alert.showAndWait();

        // Refresh the table to show updated data
        salaryShowListData();

        // Reset the input fields
        salaryReset();
    } else {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Employee ID not found. Please select a valid employee.");
        alert.showAndWait();
    }
}



    public void salaryReset() {
        salary_employeeID.setText("");
        salary_firstName.setText("");
        salary_lastName.setText("");
        salary_position.setText("");
        salary_salary.setText("");
    }
    
   //       salary list...............

   public ObservableList<employeeData> salaryListData() {
    ObservableList<employeeData> listData = FXCollections.observableArrayList();
    String sql = "SELECT e.employee_id, e.firstName, e.lastName, p.position_name AS position, ei.salary " + 
             "FROM employee e " +
             "LEFT JOIN position p ON e.position_id = p.position_id " +
             "LEFT JOIN employee_info ei ON e.employee_id = ei.employee_id";

            
    connect = DatabaseManager.getInstance().getConnection();

    try {
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();

        while (result.next()) {
            employeeData employeeD = new employeeData(
                    result.getInt("employee_id"),
                    result.getString("firstName"),
                    result.getString("lastName"),
                    result.getString("position"), // Correctly fetching position_name
                    result.getDouble("salary") // Correctly fetching salary
            );
            listData.add(employeeD);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } 
    return listData;
}

private ObservableList<employeeData> salaryList;

public void salaryShowListData() {
    salaryList = salaryListData();

    // Debugging - print data once
    System.out.println("Loaded salary list: " + salaryList);

    // Set up table columns to bind with data
    salary_col_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
    salary_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    salary_col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    salary_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
    salary_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));

    // Bind data to the TableView
    salary_tableView.setItems(salaryList);
}


    public void salarySelect() {

        employeeData employeeD = salary_tableView.getSelectionModel().getSelectedItem();
        int num = salary_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        salary_employeeID.setText(String.valueOf(employeeD.getEmployeeId()));
        salary_firstName.setText(employeeD.getFirstName());
        salary_lastName.setText(employeeD.getLastName());
        salary_position.setText(employeeD.getPosition());
        salary_salary.setText(String.valueOf(employeeD.getSalary()));

    }

    public void defaultNav() {
        home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
    }

    public void displayUsername() {
        username.setText(getData.username);
        
        System.out.println("User Name is : " + username.getText() + "and the user name in here is : "+ getData.username);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            addEmployee_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            homeTotalEmployees();
            homeEmployeeTotalPresent();
            homeTotalInactive();
            homeChart();

        } else if (event.getSource() == addEmployee_btn) {
            home_form.setVisible(false);
            addEmployee_form.setVisible(true);
            salary_form.setVisible(false);

            addEmployee_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            home_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            addEmployeeGendernList();
            addEmployeePositionList();
            addEmployeeSearch();

        } else if (event.getSource() == salary_btn) {
            home_form.setVisible(false);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(true);

            salary_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            addEmployee_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");

            salaryShowListData();

        }

    }

    private double x = 0;
    private double y = 0;

    public void logout() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/employeemanagementsystem/FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        defaultNav();

        homeTotalEmployees();
        homeEmployeeTotalPresent();
        homeTotalInactive();
        homeChart();

        addEmployeeShowListData();
        addEmployeeGendernList();
        addEmployeePositionList();

        salaryShowListData();
    }

    

}
