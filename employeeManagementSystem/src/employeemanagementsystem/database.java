
package employeemanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;


public class database {
    
    public static Connection connectDb(){
        
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
        
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_management", "root", "BasitIqbal@050");
            return connect;
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
    
}

//
//package employeemanagementsystem;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//public class database {
//    
//    private static database instance; // Singleton instance
//    private Connection connection;
//
//    private database() {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver"); // Updated driver
//            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_management", "root", "BasitIqbal@050");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Provide global access to the single instance
//    public static database getInstance() {
//        if (instance == null) {
//            instance = new database();
//        }
//        return instance;
//    }
//
//    public Connection getConnection() {
//        return connection;
//    }
//}
//
//
//
