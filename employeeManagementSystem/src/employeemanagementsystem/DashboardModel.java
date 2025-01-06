//
//import employeemanagementsystem.database;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Observable;
//
//public class DashboardModel extends Observable {
//    private int totalEmployees;
//    private int totalPresents;
//    private int totalInactive;
//
//    public void updateData() {
//        totalEmployees = executeCountQuery("SELECT COUNT(employee_id) AS total FROM employee");
//        totalPresents = executeCountQuery("SELECT COUNT(employee_id) AS total FROM employee_info");
//        totalInactive = executeCountQuery("SELECT COUNT(employee_id) AS total FROM employee_info WHERE salary = 0.0");
//
//        // Notify observers (dashboardController)
//        setChanged();
//        notifyObservers();
//    }
//
//    private int executeCountQuery(String sql) {
//        Connection connection = database.getInstance().getConnection();
//        int countData = 0;
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//
//            if (resultSet.next()) {
//                countData = resultSet.getInt("total");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return countData;
//    }
//
//    // Getters for data
//    public int getTotalEmployees() {
//        return totalEmployees;
//    }
//
//    public int getTotalPresents() {
//        return totalPresents;
//    }
//
//    public int getTotalInactive() {
//        return totalInactive;
//    }
//}
