create database Student_Managemnet;
create database student_mgmt;   
USE student_mgmt;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    enrollment_date DATE NOT NULL
);

select * from users;
select * from students;
SELECT student_id, name, email, course, phone, enrollment_date FROM students ORDER BY student_id


