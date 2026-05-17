package com.studentmgmt.ui;

import com.studentmgmt.dao.StudentDAO;
import com.studentmgmt.model.Student;
import com.studentmgmt.model.User;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final User loggedInUser;
    private final StudentDAO studentDAO = new StudentDAO();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Email", "Course", "Phone", "Enrollment Date"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable studentTable = new JTable(tableModel);

    public DashboardFrame(User user) {
        super("Student Management System - Dashboard");
        this.loggedInUser = user;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        setJMenuBar(buildMenuBar());

        JLabel welcome = new JLabel("Logged in as: " + user.getUsername() + " (" + user.getEmail() + ")");
        welcome.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        JButton updateBtn = new JButton("Update Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        toolbar.add(refreshBtn);
        toolbar.add(updateBtn);
        toolbar.add(deleteBtn);

        refreshBtn.addActionListener(e -> loadStudents());
        updateBtn.addActionListener(e -> updateSelected());
        deleteBtn.addActionListener(e -> deleteSelected());

        add(welcome, BorderLayout.NORTH);
        add(toolbar, BorderLayout.SOUTH);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        loadStudents();
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);

        JMenu studentMenu = new JMenu("Student");
        JMenuItem addItem = new JMenuItem("Add Student");
        addItem.addActionListener(e -> openAddStudent());
        studentMenu.add(addItem);

        JMenu searchMenu = new JMenu("Search");
        JMenuItem searchItem = new JMenuItem("Search Student");
        searchItem.addActionListener(e -> new SearchStudentDialog(this).setVisible(true));
        searchMenu.add(searchItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About Application");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Student Management System\nJava Swing + JDBC + MySQL\nVersion 1.0",
                "About",
                JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        bar.add(fileMenu);
        bar.add(studentMenu);
        bar.add(searchMenu);
        bar.add(helpMenu);
        return bar;
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDAO.findAll();
            tableModel.setRowCount(0);
            for (Student s : students) {
                tableModel.addRow(new Object[]{
                        s.getStudentId(),
                        s.getName(),
                        s.getEmail(),
                        s.getCourse(),
                        s.getPhone(),
                        s.getEnrollmentDate()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load students: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddStudent() {
        StudentFormDialog dialog = new StudentFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                studentDAO.insert(dialog.getStudent());
                loadStudents();
                JOptionPane.showMessageDialog(this, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to add student: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateSelected() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student to update.", "Update", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student existing = rowToStudent(row);
        StudentFormDialog dialog = new StudentFormDialog(this, existing);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                studentDAO.update(dialog.getStudent());
                loadStudents();
                JOptionPane.showMessageDialog(this, "Student updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update student: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.", "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student \"" + name + "\" (ID: " + id + ")?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            studentDAO.delete(id);
            loadStudents();
            JOptionPane.showMessageDialog(this, "Student deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to delete student: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Student rowToStudent(int row) {
        Student s = new Student();
        s.setStudentId((Integer) tableModel.getValueAt(row, 0));
        s.setName((String) tableModel.getValueAt(row, 1));
        s.setEmail((String) tableModel.getValueAt(row, 2));
        s.setCourse((String) tableModel.getValueAt(row, 3));
        s.setPhone((String) tableModel.getValueAt(row, 4));
        Object date = tableModel.getValueAt(row, 5);
        if (date instanceof java.time.LocalDate ld) {
            s.setEnrollmentDate(ld);
        } else if (date != null) {
            s.setEnrollmentDate(java.time.LocalDate.parse(date.toString()));
        }
        return s;
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}

