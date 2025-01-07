package DALManager;

import Model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private Connection connect;

    public EmployeeDAO() {
        connect = DatabaseManager.getInstance().getConnection();
    }

    // Retrieve total employees
    public int getTotalEmployees() {
        return executeCountQuery("SELECT COUNT(employee_id) AS total FROM employee");
    }

    // Retrieve total present employees
    public int getTotalPresentEmployees() {
        return executeCountQuery("SELECT COUNT(employee_id) AS total FROM employee_info");
    }

    // Retrieve total inactive employees
    public int getTotalInactiveEmployees() {
        return executeCountQuery("SELECT COUNT(employee_id) AS total FROM employee_info WHERE salary = 0.0");
    }

    private int executeCountQuery(String sql) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Retrieve all employees
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, p.position_name FROM employee e JOIN position p ON e.position_id = p.position_id";

        try (PreparedStatement preparedStatement = connect.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("gender"),
                        resultSet.getString("phoneNum"),
                        resultSet.getString("position_name"),
                        resultSet.getDate("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // Add a new employee
    public boolean addEmployee(String employeeId, String firstName, String lastName, String gender, String phoneNum, String position, String imagePath) {
        String employeeSql = "INSERT INTO employee (employee_id, firstName, lastName, gender, phoneNum, position_id, image, date) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        String employeeInfoSql = "INSERT INTO employee_info (employee_id, salary, date) VALUES (?, ?, NOW())";

        try {
            Integer positionId = getPositionId(position);
            if (positionId == null) {
                System.err.println("Invalid position selected: " + position);
                return false;
            }

            connect.setAutoCommit(false);

            try (PreparedStatement employeeStmt = connect.prepareStatement(employeeSql);
                 PreparedStatement employeeInfoStmt = connect.prepareStatement(employeeInfoSql)) {
                // Insert into employee table
                employeeStmt.setString(1, employeeId);
                employeeStmt.setString(2, firstName);
                employeeStmt.setString(3, lastName);
                employeeStmt.setString(4, gender);
                employeeStmt.setString(5, phoneNum);
                employeeStmt.setInt(6, positionId);
                employeeStmt.setString(7, sanitizeFilePath(imagePath));
                employeeStmt.executeUpdate();

                // Insert into employee_info table
                employeeInfoStmt.setString(1, employeeId);
                employeeInfoStmt.setDouble(2, 0.0);
                employeeInfoStmt.executeUpdate();
            }

            connect.commit();
            connect.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            try {
                if (connect != null) {
                    connect.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }

    // Update an existing employee
    public boolean updateEmployee(int employeeId, String firstName, String lastName, String gender, String position, String phoneNum) {
        String sql = "UPDATE employee SET firstName = ?, lastName = ?, gender = ?, position_id = ?, phoneNum = ? WHERE employee_id = ?";
        try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            Integer positionId = getPositionId(position);
            if (positionId == null) {
                System.err.println("Invalid position selected: " + position);
                return false;
            }

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, gender);
            preparedStatement.setInt(4, positionId);
            preparedStatement.setString(5, phoneNum);
            preparedStatement.setInt(6, employeeId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete an employee
public boolean deleteEmployee(int employeeId) {
    String sql = "DELETE FROM employee WHERE employee_id = ?";
    try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
        preparedStatement.setInt(1, employeeId);
        return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    // Utility: Get position ID
    private Integer getPositionId(String positionName) {
        String sql = "SELECT position_id FROM position WHERE position_name = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, positionName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("position_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Utility: Sanitize file path for database
    private String sanitizeFilePath(String path) {
        return path != null ? path.replace("\\", "\\\\") : null;
    }
    
    
    // Update employee salary
    public boolean updateEmployeeSalary(int employeeId, double salary) {
        String sql = "UPDATE employee_info SET salary = ? WHERE employee_id = ?";

        try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            preparedStatement.setDouble(1, salary);
            preparedStatement.setInt(2, employeeId);

            // Execute update and return success status
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
