# Library Management System (LMS)

A Spring Boot-based Library Management System that provides comprehensive functionality for managing library operations including users, authors, genres, publishers, and librarians.

## 🚀 Features

- **User Management**: Complete user registration and management system
- **Author Management**: Track author information including biography and nationality
- **Genre Management**: Organize books by categories and genres
- **Publisher Management**: Manage publishing house information
- **Librarian Management**: Staff and admin role management
- **Book Management**: Comprehensive book catalog with ISBN, titles, descriptions, and metadata
- **Book Copy Management**: Track individual book copies with barcodes, conditions, and locations
- **Borrowing System**: Complete borrowing workflow with due dates and return tracking
- **Fine Management**: Automated fine calculation and tracking for overdue books
- **Payment Processing**: Support for multiple payment methods and transaction tracking
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
│   │       │   ├── Book.java
│   │       │   ├── BookCopy.java
│   │       │   ├── Borrowed.java
│   │       │   ├── Fine.java
│   │       │   ├── Genre.java
│   │       │   ├── Librarian.java
│   │       │   ├── Payment.java
│   │       │   ├── Publisher.java
│   │       │   └── User.java
│   │       ├── enums/            # Enumerations
│   │       │   ├── BookCopyStatus.java
│   │       │   ├── BookStatus.java
│   │       │   ├── BorrowStatus.java
│   │       │   ├── FineStatus.java
│   │       │   ├── PaymentMethod.java
│   │       │   ├── PaymentStatus.java
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

### Core Entities

#### User Entity
- **Fields**: id, name, email, password, phoneNumber, address, membershipDate, status
- **Security**: Passwords are automatically encrypted using BCrypt
- **Status**: Default status is ACTIVE
- **Relationships**: One-to-many with Borrowed records

#### Author Entity
- **Fields**: id, name, biography, birthDate, nationality
- **Purpose**: Store author information for book management
- **Relationships**: Many-to-many with Books

#### Genre Entity
- **Fields**: id, name, description
- **Purpose**: Categorize books by genre
- **Relationships**: Many-to-many with Books

#### Publisher Entity
- **Fields**: Publisher information for book management
- **Relationships**: One-to-many with Books

#### Librarian Entity
- **Fields**: Staff and admin role management

### Book Management Entities

#### Book Entity
- **Fields**: id, isbn, title, description, publicationDate, language, pageCount, status
- **Purpose**: Central entity for book catalog management
- **Key Features**:
  - Unique ISBN constraint
  - Default status: AVAILABLE
  - Support for multiple authors and genres
- **Relationships**:
  - Many-to-one with Publisher
  - Many-to-many with Authors
  - Many-to-many with Genres
  - One-to-many with BookCopy

#### BookCopy Entity
- **Fields**: id, barcode, condition, status, acquisitionDate, location
- **Purpose**: Track individual physical copies of books
- **Key Features**:
  - Unique barcode for each copy
  - Condition tracking (NEW, GOOD, FAIR, POOR)
  - Location tracking within library
  - Default status: AVAILABLE
- **Relationships**:
  - Many-to-one with Book
  - One-to-many with Borrowed records

### Borrowing System Entities

#### Borrowed Entity
- **Fields**: id, borrowDate, dueDate, returnDate, status
- **Purpose**: Track book borrowing transactions
- **Key Features**:
  - Automatic 14-day due date calculation
  - Default status: BORROWED
  - Support for overdue tracking
- **Relationships**:
  - Many-to-one with User
  - Many-to-one with BookCopy
  - One-to-one with Fine

### Financial Management Entities

#### Fine Entity
- **Fields**: id, amount, assessedDate, status, reason
- **Purpose**: Track fines for overdue books and other violations
- **Key Features**:
  - BigDecimal precision for monetary amounts
  - Default status: PENDING
  - Detailed reason tracking
- **Relationships**:
  - One-to-one with Borrowed
  - One-to-many with Payment

#### Payment Entity
- **Fields**: id, amount, paymentDate, paymentMethod, transactionId, status
- **Purpose**: Process payments for fines and other charges
- **Key Features**:
  - Support for multiple payment methods (CARD, CASH, ONLINE)
  - Transaction ID tracking
  - Default status: PENDING
  - Support for partial payments
- **Relationships**:
  - Many-to-one with Fine

## 📊 Enumerations

The system uses several enumerations to maintain data consistency and provide clear status tracking:

### Book Management Enums

#### BookStatus
- **AVAILABLE**: Book is available for borrowing
- **UNAVAILABLE**: Book is not available (maintenance, lost, etc.)

#### BookCopyStatus
- **AVAILABLE**: Physical copy is available for borrowing
- **BORROWED**: Physical copy is currently borrowed
- **UNAVAILABLE**: Physical copy is not available (damaged, lost, etc.)

### Borrowing System Enums

#### BorrowStatus
- **BORROWED**: Book is currently borrowed
- **RETURNED**: Book has been returned
- **OVERDUE**: Book is past its due date

### Financial Management Enums

#### FineStatus
- **PENDING**: Fine has been assessed but not yet paid
- **PAID**: Fine has been fully paid
- **WAIVED**: Fine has been waived by library staff

#### PaymentMethod
- **CARD**: Credit/Debit card payment
- **CASH**: Cash payment
- **ONLINE**: Online payment (bank transfer, digital wallet, etc.)

#### PaymentStatus
- **COMPLETED**: Payment has been successfully processed
- **PENDING**: Payment is being processed
- **FAILED**: Payment processing failed

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
