# Library Management System (LMS)

A Spring Boot-based Library Management System that provides comprehensive functionality for managing library operations including users, authors, genres, publishers, and librarians.

## 🚀 Features

- **User Management**: Complete user registration and management system
- **Author Management**: Track author information including biography and nationality
- **Genre Management**: Organize books by categories and genres
- **Publisher Management**: Manage publishing house information
- **Librarian Management**: Staff and admin role management
- **Security**: Password encryption using BCrypt
- **Database Integration**: PostgreSQL database with JPA/Hibernate

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.5.6
- **Java Version**: 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security Crypto
- **Build Tool**: Maven
- **Lombok**: For reducing boilerplate code

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **PostgreSQL** database
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🗄️ Database Setup

The application is configured to use PostgreSQL. You have two options:

### Option 1: Use the existing Supabase configuration
The application is already configured to connect to a Supabase PostgreSQL database. No additional setup required.

### Option 2: Set up your own PostgreSQL database

1. Install PostgreSQL on your system
2. Create a new database for the LMS application
3. Update the database configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd LMS
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application

#### Using Maven:
```bash
mvn spring-boot:run
```

#### Using the JAR file:
```bash
java -jar target/LMS-0.0.1-SNAPSHOT.jar
```

#### Using Maven Wrapper (if available):
```bash
# On Windows
./mvnw.cmd spring-boot:run

# On Unix/Linux/Mac
./mvnw spring-boot:run
```

### 4. Access the Application
The application will start on the default port `8080`. You can access it at:
```
http://localhost:8080
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/LibraryManagementSystem/LMS/
│   │       ├── entity/           # JPA entities
│   │       │   ├── Author.java
│   │       │   ├── Genre.java
│   │       │   ├── Librarian.java
│   │       │   ├── Publisher.java
│   │       │   └── User.java
│   │       ├── enums/            # Enumerations
│   │       │   ├── Role.java
│   │       │   └── Status.java
│   │       ├── repository/       # Data repositories
│   │       │   └── GenreRepository.java
│   │       └── LmsApplication.java
│   └── resources/
│       ├── application.properties
│       ├── static/               # Static web resources
│       └── templates/            # Thymeleaf templates
└── test/                         # Test files
```

## 🏗️ Entity Models

### User Entity
- **Fields**: id, name, email, password, phoneNumber, address, membershipDate, status
- **Security**: Passwords are automatically encrypted using BCrypt
- **Status**: Default status is ACTIVE

### Author Entity
- **Fields**: id, name, biography, birthDate, nationality
- **Purpose**: Store author information for book management

### Genre Entity
- **Fields**: id, name, description
- **Purpose**: Categorize books by genre

### Publisher Entity
- **Fields**: Publisher information for book management

### Librarian Entity
- **Fields**: Staff and admin role management

## 🔧 Configuration

### Application Properties
Key configuration settings in `application.properties`:

- **Database**: PostgreSQL connection settings
- **JPA**: Hibernate configuration with automatic table creation
- **Security**: Password encoding settings
- **Connection Pool**: HikariCP configuration

### Database Configuration
- **DDL Mode**: `update` (automatically creates/updates tables)
- **SQL Logging**: Enabled for development
- **Naming Strategy**: CamelCase to underscores conversion

## 🧪 Testing

Run the test suite using:
```bash
mvn test
```

## 📦 Building for Production

### Create JAR file:
```bash
mvn clean package
```

### Run the JAR:
```bash
java -jar target/LMS-0.0.1-SNAPSHOT.jar
```

## 🔒 Security Features

- **Password Encryption**: All passwords are automatically encrypted using BCrypt
- **Input Validation**: Spring Boot validation annotations
- **SQL Injection Protection**: JPA/Hibernate provides protection against SQL injection

## 🐛 Troubleshooting

### Common Issues:

1. **Database Connection Error**:
   - Verify PostgreSQL is running
   - Check database credentials in `application.properties`
   - Ensure database exists

2. **Port Already in Use**:
   - Change the port in `application.properties`:
   ```properties
   server.port=8081
   ```

3. **Java Version Issues**:
   - Ensure Java 21 is installed and configured
   - Check JAVA_HOME environment variable

## 📝 Development Notes

- The application uses Lombok for reducing boilerplate code
- Database tables are automatically created/updated on startup
- Development tools are included for hot reloading
- SQL queries are logged for debugging purposes

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request


## 📞 Support

For support and questions, please contact the development team or create an issue in the repository.
