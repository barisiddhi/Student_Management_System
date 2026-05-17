package com.studentmgmt.ui;

import com.studentmgmt.dao.StudentDAO;
import com.studentmgmt.model.Student;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

public class SearchStudentDialog extends JDialog {

    private final StudentDAO studentDAO = new StudentDAO();
    private final JComboBox<String> fieldCombo = new JComboBox<>(new String[]{"Student ID", "Name", "Course"});
    private final JTextField valueField = new JTextField(20);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Email", "Course", "Phone", "Enrollment Date"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public SearchStudentDialog(Frame owner) {
        super(owner, "Search Student", true);
        setSize(700, 400);
        setLocationRelativeTo(owner);

        JPanel top = new JPanel(new GridLayout(1, 4, 8, 8));
        top.add(new JLabel("Search by:"));
        top.add(fieldCombo);
        top.add(valueField);
        JButton searchBtn = new JButton("Search");
        top.add(searchBtn);

        searchBtn.addActionListener(e -> performSearch());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void performSearch() {
        String value = valueField.getText().trim();
        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a search value.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Student> results = studentDAO.search((String) fieldCombo.getSelectedItem(), value);
            tableModel.setRowCount(0);
            for (Student s : results) {
                tableModel.addRow(new Object[]{
                        s.getStudentId(),
                        s.getName(),
                        s.getEmail(),
                        s.getCourse(),
                        s.getPhone(),
                        s.getEnrollmentDate()
                });
            }
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No matching records found.", "Search", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
