
# MindCare: Early Mental Health Prediction and Wellness System

Welcome to the **MindCare** repository! This project is a desktop-based mental health prediction and daily wellness application built using Core Java. It aims to provide users with a secure, interactive environment to track their mood, analyze emotional trends, and access professional support or educational materials.

---

## 🚀 Features

* **Interactive Splash Screen:** Displays an animated visualization using AWT/Swing drawing tools when the application launches.
* **User Authentication & Profiles:** Secure login and multi-step registration with strict password strength validation.
* **Mood Tracking Dashboard:** Create, read, update, and delete daily mood logs and notes via an intuitive graphical user interface (GUI) connected to a relational database.
* **Weekly Trend Analytics:** Analyzes the last 7 mood inputs using a rule-based algorithm to identify dominant emotional trends and generate supportive, data-driven feedback.
* **Professional Counseling & Articles:** Direct links to articles and curated wellness support.

---

## 🛠️ Requirements

* **Java Development Kit (JDK):** Version 8 or higher
* **Database Management System:** MySQL Server
* **Libraries:** MySQL Connector/J (JDBC driver to connect Java with MySQL)

---

## 📂 Project Structure

All source code is contained within the `mindcare` package:
* `Main.java` - Application entry point.
* `MindCareSplash.java` - Animated loading screen window.
* `LoginPage.java` & `RegistrationPage.java` - Authentication forms.
* `MindCareGUI.java` - Main dashboard for CRUD operations on user mood logs.
* `Mood.java` - Data model representing a user mood entry.
* `TrendDashboard.java` - Rule-based algorithm engine and analytics dashboard.
* `CounselingPage.java` & `ArticleManager.java` - Support and wellness resources.
* `TestDB.java` - Utility file to test database connectivity.

---

## ⚙️ Setup Instructions

### 1. Database Setup
Start your MySQL server and run the following commands in your MySQL client to set up the schema:

```sql
CREATE DATABASE mindcare_db;
USE mindcare_db;

CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    age INT,
    gender VARCHAR(20)
);

CREATE TABLE mood_entries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    mood_status VARCHAR(50),
    note TEXT,
    entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username)
);
```

### 2. Configuration
1. Clone this repository and open the files in your preferred Java IDE (such as Eclipse or IntelliJ IDEA).
2. Ensure that the MySQL JDBC driver JAR file is added to your project's classpath/dependencies.
3. Open `MindCareSplash.java` and make sure the absolute path to the splash screen image points to a valid image file on your machine:
   ```java
   String path = "C:\\path\\to\\your\\image.png";
   ```

### 3. Compilation and Execution
To run the project from the command line, open your terminal or command prompt in the parent directory of the `mindcare` package and run:

```bash
javac mindcare/*.java
java mindcare.Main
```
```
