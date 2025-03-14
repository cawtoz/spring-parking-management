# 🚗 AutoPark - Parking Management System
This project is a **Spring Boot-based** parking management system designed to streamline vehicle entry and exit tracking, manage parking spaces, and automate fee calculations. The system includes **user authentication**, **email notifications**, and an intuitive **admin dashboard** for real-time monitoring.

## 🚀 Features
- **Secure authentication and role-based access control** for admins and operators.
- **Vehicle entry and exit management** with timestamp tracking.
- **Real-time availability monitoring** for parking spaces.
- **Automated fee calculation** based on parking duration.
- **Email notifications** for vehicle arrivals and departures.
- **Admin dashboard** for managing users, spaces, entries and exits.

## 🛠 Technologies
- Java
- Spring Boot (JPA, Web, Security)
- Thymeleaf
- HTML
- CSS
- JavaScript
- Bootstrap
- MySQL

![Parking Management](https://github.com/user-attachments/assets/d6e93feb-ff33-4edf-a633-ad1cfca1452a)

## 📌 Requirements
Before installing, ensure you have:

- Java 17+
- Maven 3+
- MySQL 8+
- Gmail SMTP credentials for email notifications

## 🛠 Installation
Follow these steps to set up the project in your environment:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/cawtoz/parking-management.git
   cd parking-management
   ```

2. **Set up environment variables:**

   Rename `.env.example` to `.env` and configure database and email settings:
   ```ini
   # Database Config
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=autopark
   DB_USERNAME=root
   DB_PASSWORD=yourpassword

   # Email Notifications (Gmail SMTP)
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-app-password
   ```

3. **Build and run the application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The application will be available at **http://localhost:8080**

## 📂 Project Structure
```
src/
├── main/
│   ├── java/com/github/cawtoz/parking/
│   │   ├── controllers/   # Handles HTTP requests
│   │   ├── models/        # Database entities
│   │   ├── repositories/  # JPA Repositories for data access
│   │   ├── security/      # Spring Security configuration
│   │   ├── services/      # Business logic and notifications
│   ├── resources/
│   │   ├── templates/     # Thymeleaf HTML templates
│   │   ├── static/css/    # CSS files for UI
│   │   ├── static/js/     # JavaScript files for frontend logic
│   │   ├── application.properties  # Configuration settings
```