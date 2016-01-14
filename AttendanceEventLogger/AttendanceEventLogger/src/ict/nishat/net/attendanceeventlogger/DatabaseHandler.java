package ict.nishat.net.attendanceeventlogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String USER_TABLE = "xx_e_portal_users";
    private String connectionString =
        "jdbc:oracle:thin:@192.168.0.31:1522:prod";

//    private String connectionString =
//        "jdbc:oracle:thin:@10.152.13.34:1522:prod";
    
    
    
    private String user = "xx_e_portal";
    private String password = "mskiz145";
    private PreparedStatement pst;
    
    public DatabaseHandler() {}
    
    public void loadDrivers() throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }
    
    public Connection connectDB() throws SQLException {
        return DriverManager.getConnection(connectionString, user, password);
    }
    
}