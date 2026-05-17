package com.studentmgmt.dao;

import com.studentmgmt.model.User;
import com.studentmgmt.util.DatabaseConnection;
import com.studentmgmt.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void register(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername().trim());
            ps.setString(2, user.getEmail().trim());
            ps.setString(3, PasswordUtil.hash(user.getPassword()));
            ps.executeUpdate();
        }
    }

    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT user_id, username, email FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, PasswordUtil.hash(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"));
                }
            }
        }
        return null;
    }
}
