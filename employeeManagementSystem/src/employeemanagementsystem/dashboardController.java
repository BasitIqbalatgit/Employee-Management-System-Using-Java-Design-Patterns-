
package employeemanagementsystem;

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

public class dashboardController implements Initializable {

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

     // Method to count total employees
    public void homeTotalEmployees() {
        String sql = "SELECT COUNT(employee_id) AS total FROM employee";
        int totalEmployees = executeCountQuery(sql);
        home_totalEmployees.setText(String.valueOf(totalEmployees));
    }

    // Method to count total present employees
    public void homeEmployeeTotalPresent() {
        String sql = "SELECT COUNT(employee_id) AS total FROM employee_info";
        int totalPresents = executeCountQuery(sql);
        home_totalPresents.setText(String.valueOf(totalPresents));
    }

    // Method to count total inactive employees
    public void homeTotalInactive() {
        String sql = "SELECT COUNT(employee_id) AS total FROM employee_info WHERE salary = 0.0";
        int totalInactive = executeCountQuery(sql);
        home_totalInactiveEm.setText(String.valueOf(totalInactive));
    }

    // Generic method for executing count queries
    private int executeCountQuery(String sql) {
        connect = database.connectDb();
        int countData = 0;

        try (PreparedStatement preparedStatement = connect.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                countData = resultSet.getInt("total");
            }

        } catch (Exception e) {
            System.err.println("Error executing query: " + sql);
            e.printStackTrace();
        }

        return countData;
    }


    public void homeChart() {

        home_chart.getData().clear();

        String sql = "SELECT date, COUNT(employee_id) FROM employee GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 7";
        
        connect = database.connectDb();

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
    Date date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

    String employeeSql = "INSERT INTO employee "
            + "(employee_id, firstName, lastName, gender, phoneNum, position_id, image, date) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    
    String employeeInfoSql = "INSERT INTO employee_info "
            + "(employee_id, salary, date) "
            + "VALUES(?, ?, ?)";

    connect = database.connectDb();

    try {
        // Check if any required field is empty
        if (isAnyFieldEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");
            return;
        }

        // Check if employee ID already exists
        if (isEmployeeIdExists(addEmployee_employeeID.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error Message",
                    "Employee ID: " + addEmployee_employeeID.getText() + " already exists!");
            return;
        }

        // Get the selected position ID
        Integer positionId = getPositionId((String) addEmployee_position.getSelectionModel().getSelectedItem());
        if (positionId == null) {
            showAlert(Alert.AlertType.ERROR, "Error Message", "Invalid position selected!");
            return;
        }

        // Insert into `employee` table
        try (PreparedStatement employeeStmt = connect.prepareStatement(employeeSql)) {
            employeeStmt.setString(1, addEmployee_employeeID.getText());
            employeeStmt.setString(2, addEmployee_firstName.getText());
            employeeStmt.setString(3, addEmployee_lastName.getText());
            employeeStmt.setString(4, (String) addEmployee_gender.getSelectionModel().getSelectedItem());
            employeeStmt.setString(5, addEmployee_phoneNum.getText());
            employeeStmt.setInt(6, positionId);
            employeeStmt.setString(7, sanitizeFilePath(getData.path));
            employeeStmt.setDate(8, sqlDate);
            employeeStmt.executeUpdate();
        }

        // Insert into `employee_info` table
        try (PreparedStatement employeeInfoStmt = connect.prepareStatement(employeeInfoSql)) {
            employeeInfoStmt.setString(1, addEmployee_employeeID.getText());
            employeeInfoStmt.setDouble(2, 0.0); // Default salary
            employeeInfoStmt.setDate(3, sqlDate);
            employeeInfoStmt.executeUpdate();
        }

        showAlert(Alert.AlertType.INFORMATION, "Information Message", "Successfully Added!");
        addEmployeeShowListData();
        addEmployeeReset();

    } catch (Exception e) {
        System.err.println("Error adding employee: " + e.getMessage());
        e.printStackTrace();
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

private boolean isEmployeeIdExists(String employeeId) {
    String checkSql = "SELECT employee_id FROM employee WHERE employee_id = ?";
    try (PreparedStatement checkStmt = connect.prepareStatement(checkSql)) {
        checkStmt.setString(1, employeeId);
        ResultSet resultSet = checkStmt.executeQuery();
        return resultSet.next();
    } catch (SQLException e) {
        System.err.println("Error checking employee ID: " + e.getMessage());
        return false;
    }
}

private Integer getPositionId(String positionName) {
    if (positionName == null || positionName.isEmpty()) {
        System.out.println("Position name is null or empty");
        return null;
    }

    String positionSql = "SELECT position_id FROM position WHERE position_name = ?";
    try (PreparedStatement positionStmt = connect.prepareStatement(positionSql)) {
        positionStmt.setString(1, positionName);
        ResultSet resultSet = positionStmt.executeQuery();
        if (resultSet.next()) {
            System.out.println("The Position ID is : "+ resultSet.getInt("position_id"));
            return resultSet.getInt("position_id");
        } else {
            System.out.println("No matching position found for: " + positionName);
        }
    } catch (SQLException e) {
        System.err.println("Error retrieving position ID: " + e.getMessage());
    }
    return null;
}


private String sanitizeFilePath(String path) {
    if (path != null) {
        return path.replace("\\", "\\\\");
    }
    return "";
}

private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

// update ................

public void addEmployeeUpdate() throws SQLException {
    String uri = getData.path;
    uri = uri.replace("\\", "\\\\"); // Escape backslashes for SQL compatibility

    Date date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

    // Retrieve position_id based on selected position name
    Integer positionId = getPositionId((String) addEmployee_position.getSelectionModel().getSelectedItem());

    if (positionId == null) {
        showAlert(Alert.AlertType.ERROR, "Error Message", "Invalid position selected.");
        return;
    }

    String sql = "UPDATE employee SET firstName = ?, lastName = ?, gender = ?, phoneNum = ?, position_id = ?, image = ?, date = ? WHERE employee_id = ?";

    if (connect == null || connect.isClosed()) {
        connect = database.connectDb(); // Reopen connection if closed
    }

    try {
        if (addEmployee_employeeID.getText().isEmpty() ||
            addEmployee_firstName.getText().isEmpty() ||
            addEmployee_lastName.getText().isEmpty() ||
            addEmployee_gender.getSelectionModel().getSelectedItem() == null ||
            addEmployee_phoneNum.getText().isEmpty() ||
            addEmployee_position.getSelectionModel().getSelectedItem() == null ||
            getData.path == null || getData.path.isEmpty()) {

            showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation Message");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to UPDATE Employee ID: " + addEmployee_employeeID.getText() + "?");
        Optional<ButtonType> option = confirmationAlert.showAndWait();

        if (option.isPresent() && option.get().equals(ButtonType.OK)) {
            int employeeId = Integer.parseInt(addEmployee_employeeID.getText());

            try (PreparedStatement stmt = connect.prepareStatement(sql)) {
                stmt.setString(1, addEmployee_firstName.getText());
                stmt.setString(2, addEmployee_lastName.getText());
                stmt.setString(3, (String) addEmployee_gender.getSelectionModel().getSelectedItem());
                stmt.setString(4, addEmployee_phoneNum.getText());
                stmt.setInt(5, positionId);
                stmt.setString(6, uri);
                stmt.setDate(7, sqlDate);
                stmt.setInt(8, employeeId);
                stmt.executeUpdate();

                // Update salary in employee_info
                double currentSalary = retrieveSalary(employeeId);
                String updateSalarySql = "UPDATE employee_info SET salary = ? WHERE employee_id = ?";
                try (PreparedStatement updateSalaryStmt = connect.prepareStatement(updateSalarySql)) {
                    updateSalaryStmt.setDouble(1, currentSalary);
                    updateSalaryStmt.setInt(2, employeeId);
                    updateSalaryStmt.executeUpdate();
                }

                showAlert(Alert.AlertType.INFORMATION, "Information Message", "Successfully Updated!");
                addEmployeeShowListData();
                addEmployeeReset();
            }
        }
    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        e.printStackTrace();
    } 
}


// Method to Retrieve Salary from Database
private double retrieveSalary(int employeeId) throws SQLException {
    String query = "SELECT salary FROM employee_info WHERE employee_id = ?";
    double salary = 0.0;

    if (connect == null || connect.isClosed()) {
        connect = database.connectDb();
    }

    try (PreparedStatement stmt = connect.prepareStatement(query)) {
        stmt.setInt(1, employeeId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                salary = rs.getDouble("salary");
            }
        }
    } 
    return salary;
}





// delete operation................

    public void addEmployeeDelete() {
    // SQL query using PreparedStatement to prevent SQL injection
    String sql = "DELETE FROM employee WHERE employee_id = ?";

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

            // Get database connection
            connect = database.connectDb();

            // Prepare and execute the delete statement
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setInt(1, employeeId);
                int rowsAffected = preparedStatement.executeUpdate();

                // Check if the employee was successfully deleted
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Information Message", "Successfully Deleted!");
                    addEmployeeShowListData();
                    addEmployeeReset();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error Message", "Employee ID not found. Deletion failed.");
                }
            }
        }
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Error Message", "Invalid Employee ID format. Please enter a numeric value.");
    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the employee: " + e.getMessage());
        e.printStackTrace();
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
    connect = database.connectDb();

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
    // SQL query with placeholders to prevent SQL injection
    String sql = "UPDATE employee_info SET salary = ? WHERE employee_id = ?";

    // Connect to the database
    connect = database.connectDb();

    try {
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

        // Parse salary to ensure it's a valid number
        double salary;
        try {
            salary = Double.parseDouble(salary_salary.getText());
        } catch (NumberFormatException e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid salary input! Please enter a valid numeric value.");
            alert.showAndWait();
            return;
        }

        // Check if salary is non-negative
        if (salary < 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Salary cannot be negative.");
            alert.showAndWait();
            return;
        }

        // Parse employee ID
        int employeeId;
        try {
            employeeId = Integer.parseInt(salary_employeeID.getText());
        } catch (NumberFormatException e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Employee ID! Please ensure it is a valid number.");
            alert.showAndWait();
            return;
        }

        // Prepare the SQL statement to update salary
        prepare = connect.prepareStatement(sql);
        prepare.setDouble(1, salary);
        prepare.setInt(2, employeeId);
        // Execute the update
        int rowsUpdated = prepare.executeUpdate();

        // Check if the update was successful
        if (rowsUpdated > 0) {
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

    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while updating the salary. Please try again.");
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

            
    connect = database.connectDb();

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
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
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
