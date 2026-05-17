package com.studentmgmt.ui;

import com.studentmgmt.dao.UserDAO;
import com.studentmgmt.model.User;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        super("Student Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        form.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        form.add(passwordField, gbc);

        JPanel buttons = new JPanel();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        buttons.add(loginBtn);
        buttons.add(registerBtn);

        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });
        passwordField.addActionListener(e -> handleLogin());

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = userDAO.authenticate(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
            new DashboardFrame(user).setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage() + "\nCheck MySQL is running and schema is created.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
