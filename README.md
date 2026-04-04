📘 Student Result Management System
📌 Project Overview

The Student Result Management System is a Java-based desktop application developed using Java Swing and JDBC (Oracle Database).
It provides an efficient way to manage student records, faculty details, and academic results.

The system supports three types of users:

👨‍💼 Admin
👩‍🏫 Faculty
🎓 Student
🚀 Features
🔐 Login System
Secure login for Admin, Faculty, and Student
Role-based authentication
Redirects users to respective dashboards after login
👉 Implemented in
👨‍💼 Admin Module
Add / View / Edit / Delete Faculty
Add / View Students
Filter faculty by department
Manage student data (Department & Year wise)

👉 Implemented in

👩‍🏫 Faculty Module
Load students by department
Enter marks (Internal / External / Semester)
Update or delete marks
Automatic pass/fail calculation

👉 Implemented in

🎓 Student Module
View results using Roll Number
Displays:
Subject-wise marks
Grades
Total marks
SGPA & CGPA

👉 Implemented in

🗄️ Database Connectivity
Oracle Database used
JDBC connectivity implemented
Uses Prepared Statements for secure queries

👉 Implemented in

🛠️ Technologies Used
Java (Swing GUI)
JDBC (Java Database Connectivity)
Oracle Database (XE)
SQL
OOP Concepts
📂 Project Structure
📁 Project Folder
│
├── Main.java                 → Entry point
├── LoginFrame.java           → Login Page
├── AdminFrame.java           → Admin Dashboard
├── FacultyFrame.java         → Faculty Dashboard
├── StudentFrame.java         → Student Dashboard
├── DepartmentSelectFrame.java→ Department Selection
├── DBConnection.java         → Database Connection
├── ojdbc8.jar                → Oracle JDBC Driver

👉 Entry point file:

⚙️ How to Run the Project
1️⃣ Prerequisites
Java JDK (8 or above)
Oracle Database (XE)
ojdbc8.jar driver
2️⃣ Database Setup

Create the following tables:

ADMIN_TABLE
FACULTY_TABLE
STUDENT_TABLE
MARKS_TABLE

(Update credentials inside DBConnection.java if needed)

3️⃣ Compile & Run
javac *.java
java Main
📊 Functional Flow
User opens application
Login based on role
Redirect to:
Admin Dashboard
Faculty Dashboard
Student Portal
Perform operations
Data stored/retrieved from database
🔑 Key Highlights
Clean GUI using Java Swing
Role-based access system
Real-time database interaction
CRUD operations implemented
Automatic grade & CGPA calculation
⚠️ Limitations
No password encryption
Basic UI design
Runs only on local database
No web/mobile support
🔮 Future Enhancements
Add password encryption 🔐
Convert into web application 🌐
Add charts/analytics 📊
Cloud database integration ☁️
Export results as PDF 📄
👨‍💻 Team Contribution (Example)
Member 1: Frontend (Swing UI)
Member 2: Backend (JDBC & Database)
Member 3: Logic & Validation
Member 4: Testing & Integration
📌 Conclusion

This project simplifies the process of managing student academic records by integrating Admin, Faculty, and Student functionalities into a single system. It demonstrates strong concepts of Java, JDBC, and Database Management.
