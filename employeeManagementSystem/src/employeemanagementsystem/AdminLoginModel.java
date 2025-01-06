//package employeemanagementsystem;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class AdminLoginModel {
//
//    public boolean authenticate(String username, String password) {
//        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
//        
//        try (Connection connection = database.getInstance().getConnection();
//             PreparedStatement prepare = connection.prepareStatement(sql)) {
//
//            prepare.setString(1, username);
//            prepare.setString(2, password);
//
//            ResultSet result = prepare.executeQuery();
//
//            return result.next(); // Returns true if a matching record is found
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//}
