package com.studentmgmt.config;

public final class DatabaseConfig {
	public static final String URL =
            "jdbc:mysql://localhost:3306/student_mgmt?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PASSWORD = ""; 

	private DatabaseConfig() {
		
	}
}
