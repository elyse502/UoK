package com.userapp.controllers;

import com.userapp.models.User;
import com.userapp.utils.DBConnection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class UserController {
    
    public boolean createUser(User user) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO users (fullname, email, phone, country) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, user.getFullName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getCountry());
            
            int rowsInserted = pst.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public boolean checkEmailExistsForOtherUser(String email, String currentUserId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE email = ? AND id != ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, currentUserId);  // Ensure this is treated as String
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkPhoneExistsForOtherUser(String phone, String currentUserId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE phone = ? AND id != ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, phone);
            pst.setString(2, currentUserId);  // Ensure this is treated as String
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    
    public boolean updateUser(User user, String searchText) {
        try (Connection con = DBConnection.getConnection()) {
            
            // Check if the email or phone already exists for another user (besides the current one)
            // We now pass searchText as the user ID
            if (checkEmailExistsForOtherUser(user.getEmail(), searchText)) {
                JOptionPane.showMessageDialog(null, "Email already exists for another user.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (checkPhoneExistsForOtherUser(user.getPhone(), searchText)) {
                JOptionPane.showMessageDialog(null, "Phone number already exists for another user.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String sql;
            // Search is based on user ID or full name (if searchText matches a numeric ID or not)
            if (searchText.matches("\\d+")) {  // If searchText is a number (user ID)
                sql = "UPDATE users SET fullname = ?, email = ?, phone = ?, country = ? WHERE id = ?";
            } else {  // If searchText is not a number, it's a user name
                sql = "UPDATE users SET fullname = ?, email = ?, phone = ?, country = ? WHERE fullname LIKE ?";
            }

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, user.getFullName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getCountry());

            if (searchText.matches("\\d+")) {  // If searchText is numeric (user ID)
                pst.setString(5, searchText);
            } else {  // If searchText is not numeric (user name)
                pst.setString(5, "%" + searchText + "%");
            }

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    
    public boolean deleteUser(String searchText) {
        try (Connection con = DBConnection.getConnection()) {
            String sql;
            if (searchText.matches("\\d+")) {
                sql = "DELETE FROM users WHERE id = ?";
            } else {
                sql = "DELETE FROM users WHERE fullname LIKE ?";
            }
            
            PreparedStatement pst = con.prepareStatement(sql);
            if (searchText.matches("\\d+")) {
                pst.setString(1, searchText);
            } else {
                pst.setString(1, "%" + searchText + "%");
            }
            
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public DefaultTableModel loadUserData() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "FULL NAME", "EMAIL", "PHONE", "COUNTRY"}, 0);
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM users")) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("country")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return model;
    }
    public boolean checkEmailExists(String email) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE email = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkPhoneExists(String phone) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE phone = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, phone);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    
    public User searchUser(String searchText) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE (id = ? OR fullname LIKE ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            // Check if searchText is an integer for id search
            try {
                int id = Integer.parseInt(searchText);
                pst.setInt(1, id);
            } catch (NumberFormatException e) {
                pst.setNull(1, java.sql.Types.INTEGER); // If not a number, set NULL so it won't match any id
            }

            pst.setString(2, "%" + searchText + "%");

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("country")
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}