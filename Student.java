package com.studentmgmt.model;

import java.time.LocalDate;

public class Student {
	
	private int StudentId;
	private String name;
    private String email;
    private String course;
    private String phone;
    private LocalDate enrollmentDate;
    
    public Student() {
		// TODO Auto-generated constructor stub
	}

	public Student(int studentId, String name, String email, String course, String phone, LocalDate enrollmentDate) {
		super();
		StudentId = studentId;
		this.name = name;
		this.email = email;
		this.course = course;
		this.phone = phone;
		this.enrollmentDate = enrollmentDate;
	}

	public int getStudentId() {
		return StudentId;
	}

	public void setStudentId(int studentId) {
		StudentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(LocalDate enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
    
    
}
