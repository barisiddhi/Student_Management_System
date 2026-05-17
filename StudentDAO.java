package com.studentmgmt.dao;

import com.studentmgmt.model.Student;
import com.studentmgmt.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT student_id, name, email, course, phone, enrollment_date FROM students ORDER BY student_id";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                students.add(mapRow(rs));
            }
        }
        return students;
    }

    public List<Student> search(String field, String value) throws SQLException {
        String column = switch (field) {
            case "Student ID" -> "student_id";
            case "Name" -> "name";
            case "Course" -> "course";
            default -> throw new IllegalArgumentException("Invalid search field");
        };

        boolean byId = "student_id".equals(column);
        String sql = "SELECT student_id, name, email, course, phone, enrollment_date FROM students WHERE "
                + column + (byId ? " = ?" : " LIKE ?") + " ORDER BY student_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (byId) {
                try {
                    ps.setInt(1, Integer.parseInt(value.trim()));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Student ID must be a whole number.");
                }
            } else {
                ps.setString(1, "%" + value.trim() + "%");
            }
            List<Student> students = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }
            return students;
        }
    }

    public void insert(Student student) throws SQLException {
        String sql = "INSERT INTO students (name, email, course, phone, enrollment_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindStudent(ps, student);
            ps.executeUpdate();
        }
    }

    public void update(Student student) throws SQLException {
        String sql = "UPDATE students SET name=?, email=?, course=?, phone=?, enrollment_date=? WHERE student_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName().trim());
            ps.setString(2, student.getEmail().trim());
            ps.setString(3, student.getCourse().trim());
            ps.setString(4, student.getPhone() != null ? student.getPhone().trim() : "");
            ps.setDate(5, Date.valueOf(student.getEnrollmentDate()));
            ps.setInt(6, student.getStudentId());
            ps.executeUpdate();
        }
    }

    public void delete(int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    private void bindStudent(PreparedStatement ps, Student student) throws SQLException {
        ps.setString(1, student.getName().trim());
        ps.setString(2, student.getEmail().trim());
        ps.setString(3, student.getCourse().trim());
        ps.setString(4, student.getPhone() != null ? student.getPhone().trim() : "");
        ps.setDate(5, Date.valueOf(student.getEnrollmentDate()));
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Date date = rs.getDate("enrollment_date");
        LocalDate enrollmentDate = date != null ? date.toLocalDate() : null;
        return new Student(
                rs.getInt("student_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("course"),
                rs.getString("phone"),
                enrollmentDate);
    }
}
