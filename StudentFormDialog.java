package com.studentmgmt.ui;

import com.studentmgmt.model.Student;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class StudentFormDialog extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField courseField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JTextField dateField = new JTextField(20);
    private final boolean editMode;
    private Student student;
    private boolean saved;

    public StudentFormDialog(Frame owner, Student existing) {
        super(owner, existing == null ? "Add Student" : "Update Student", true);
        this.editMode = existing != null;
        this.student = existing != null ? existing : new Student();

        if (existing != null) {
            nameField.setText(existing.getName());
            emailField.setText(existing.getEmail());
            courseField.setText(existing.getCourse());
            phoneField.setText(existing.getPhone() != null ? existing.getPhone() : "");
            dateField.setText(existing.getEnrollmentDate() != null ? existing.getEnrollmentDate().toString() : "");
        } else {
            dateField.setText(LocalDate.now().toString());
        }

        setSize(400, 280);
        setLocationRelativeTo(owner);
        buildUi();
    }

    private void buildUi() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(12, 12, 8, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFieldRow(form, gbc, 0, "Name:", nameField);
        addFieldRow(form, gbc, 1, "Email:", emailField);
        addFieldRow(form, gbc, 2, "Course:", courseField);
        addFieldRow(form, gbc, 3, "Phone:", phoneField);
        addFieldRow(form, gbc, 4, "Enrollment Date (yyyy-MM-dd):", dateField);

        JPanel buttons = new JPanel();
        JButton saveBtn = new JButton(editMode ? "Update" : "Save");
        JButton cancelBtn = new JButton("Cancel");
        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        saveBtn.addActionListener(e -> save());
        cancelBtn.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void addFieldRow(JPanel form, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        form.add(field, gbc);
    }

    private void save() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String course = courseField.getText().trim();
        String phone = phoneField.getText().trim();
        String dateText = dateField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || course.isEmpty() || dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, email, course, and enrollment date are required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateText);
            student.setName(name);
            student.setEmail(email);
            student.setCourse(course);
            student.setPhone(phone);
            student.setEnrollmentDate(date);
            saved = true;
            dispose();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Use date format yyyy-MM-dd (e.g. 2026-05-16).",
                    "Validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Student getStudent() {
        return student;
    }
}
