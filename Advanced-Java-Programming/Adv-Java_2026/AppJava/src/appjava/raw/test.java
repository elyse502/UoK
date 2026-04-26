package appjava.raw;

import java.sql.*;

public class test {
    
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            String connectionurl = "jdbc:mysql://localhost/employs?user=root&password=";
            
            Connection con = DriverManager.getConnection(connectionurl);
            
            System.out.println("Connection established!");
            
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            
            String strSql = "DELETE FROM students WHERE firstname = 'Alice'";
            
            int rowsAffected = stmt.executeUpdate(strSql);
            
            System.out.println("Rows Affected: " + rowsAffected);
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            String connectionurl = "jdbc:mysql://localhost/employs?user=root&password=";
            
            Connection con = DriverManager.getConnection(connectionurl);
            
            System.out.println("Connection Established");
            
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            
            String strQry = "UPDATE employs SET location='Kigali' WHERE Identification='1'";
            
            int rowsAffected = stmt.executeUpdate(strQry);
            
            System.out.println("Rows Affected: " + rowsAffected);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }    
}
