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

public class RegisterFrame extends JFrame {

    private final JTextField usernameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmField = new JPasswordField(20);
    private final UserDAO userDAO = new UserDAO();

    public RegisterFrame() {
        super("Student Management System - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addRow(form, gbc, row++, "Username:", usernameField);
        addRow(form, gbc, row++, "Email:", emailField);
        addRow(form, gbc, row++, "Password:", passwordField);
        addRow(form, gbc, row++, "Confirm Password:", confirmField);

        JPanel buttons = new JPanel();
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");
        buttons.add(registerBtn);
        buttons.add(backBtn);

        registerBtn.addActionListener(e -> handleRegister());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        form.add(field, gbc);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Enter a valid email address.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (userDAO.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists.", "Registration", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (userDAO.emailExists(email)) {
                JOptionPane.showMessageDialog(this, "Email already registered.", "Registration", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            userDAO.register(user);

            JOptionPane.showMessageDialog(this, "Registration successful. Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame().setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
