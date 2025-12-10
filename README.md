# Timely - University Timetable Management System

Timely is a robust Spring Boot backend application designed to manage, parse, and serve university timetables. It provides role-based access for Students, Teachers, and Administrators, allowing for personalized schedule views, automated timetable parsing from HTML/CSV files, and dynamic class management.

## 🚀 Features

* **Role-Based Authentication:** Secure login and registration for Students, Teachers, and Admins using JWT (JSON Web Tokens).
* **Automated Parsing:** Includes a sophisticated parser (`TimetableParser`) that reads raw HTML and CSV timetable files and converts them into structured database entities.
* **Personalized Schedules:**
    * **Students:** View timetables based on their specific batch and enrolled courses.
    * **Teachers:** View their teaching schedule derived automatically from class slots.
* **Admin Management:**
    * Upload and process bulk timetables.
    * Manually add or delete class slots.
    * View schedules for any specific batch or teacher.
* **Class Cancellations:** Functionality to mark specific classes as cancelled.
* **Database Optimization:** Uses PostgreSQL-specific features (like `jsonb` and `string_agg`) for efficient data storage and retrieval.

## 🛠️ Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security 6, JWT (JJWT)
* **Database:** PostgreSQL (Required for `jsonb` and native queries)
* **ORM:** Spring Data JPA / Hibernate
* **Tools/Libraries:**
    * **Lombok:** Boilerplate reduction.
    * **Jsoup:** HTML parsing for timetable files.
    * **Maven:** Dependency management.

## 📂 Project Structure

```text
src/main/java/com/example/Timely
├── config/             # Security and JWT configuration
├── Controller/         # REST Controllers (Auth, Timetable, User)
├── exception/          # Global exception handling
├── Models/             # JPA Entities (User, Student, ClassSlot) and DTOs
├── Repository/         # Data Access Layer
├── Service/            # Business Logic (TimetableService, UserService)
│   ├── Parsers/        # Logic for parsing HTML/CSV files
│   └── Security/       # JWT generation and UserDetails logic
└── TimelyApplication.java
```

## ⚙️ Configuration & Setup

### 1. Prerequisites
* Java Development Kit (JDK) 17 or higher.
* Maven.
* PostgreSQL Database installed and running.

### 2. Database Configuration
Create a PostgreSQL database (e.g., `timely_db`). Update your `src/main/resources/application.properties` with the following:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/timely_db
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration (Required)
app.jwt.secret=YOUR_SUPER_SECRET_KEY_MUST_BE_LONG_ENOUGH_FOR_HMAC_SHA
app.jwt.expiration=86400000 
# (86400000 ms = 24 hours)
```

### 3. Timetable Source Files
The application looks for a specific directory to parse HTML timetables.
1. Create a folder named `htmTables` in the root directory of the project.
2. Place your HTML timetable files inside this folder.

### 4. Running the Application
```bash
mvn spring-boot:run
```

## 🔌 API Endpoints

### Authentication (`/auth`)
* `POST /auth/register`: Register a Student.
* `POST /auth/registerTeacher`: Register a Teacher.
* `POST /auth/registerAdmin`: Register an Admin.
* `POST /auth/login`: Login to receive a Bearer Token.

### Timetable Operations (`/timetable`)
* **General**
    * `GET /timetable/`: Health check.
    * `GET /timetable/{batch}`: Get full timetable for a specific batch (e.g., "B1").
* **Student**
    * `GET /timetable/student`: Get the logged-in student's personalized timetable.
* **Teacher**
    * `GET /timetable/teacher`: Get the logged-in teacher's schedule.
    * `GET /timetable/teacher/{teacherName}`: Public lookup of a teacher's schedule.
    * `POST /timetable/cancelClass`: Cancel a specific class slot.
* **Admin**
    * `GET /timetable/save`: Trigger the parser to process files in `htmTables`.
    * `POST /timetable/admin/addClass`: Manually add a class slot.
    * `POST /timetable/admin/deleteClass`: Delete a class slot.
    * `GET /timetable/admin/teacher/{teacher}`: Get teacher info (Admin view).

### Users (`/users`)
* `GET /users`: Get a list of all users.

## 📝 Parsing Logic
The `TimetableParser` is configured to extract metadata from HTML headers, including:
* Session (e.g., 2024-25)
* Semester (Even/Odd)
* Batch/Year
* Slot Types (Lecture, Lab, Tutorial)

Ensure your HTML files follow the structure expected by the Jsoup selectors in `TimetableParser.java` for successful data extraction.

## 🤝 Contributing
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/NewFeature`).
3. Commit your changes.
4. Push to the branch.
5. Open a Pull Request.

## 📄 License
[MIT](https://choosealicense.com/licenses/mit/)