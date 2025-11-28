# Library Management System (LMS)

A Spring Boot-based Library Management System that provides comprehensive functionality for managing library operations including users, authors, genres, publishers, and librarians.

## � Table of Contents

- [Features](#-features)
- [Technology Stack](#️-technology-stack)
- [Prerequisites](#-prerequisites)
- [Database Setup](#️-database-setup)
- [Getting Started](#-getting-started)
- [Project Structure](#-project-structure)
- [Entity Models](#️-entity-models)
  - [Core Entities](#core-entities)
  - [Book Management Entities](#book-management-entities)
  - [Borrowing System Entities](#borrowing-system-entities)
  - [Financial Management Entities](#financial-management-entities)
- [Enumerations](#-enumerations)
  - [Book Management Enums](#book-management-enums)
  - [Borrowing System Enums](#borrowing-system-enums)
  - [Financial Management Enums](#financial-management-enums)
  - [User Management Enums](#user-management-enums)
- [Repository Layer](#️-repository-layer)
  - [Repository Features](#repository-features)
  - [Repository Details](#repository-details)
  - [Query Method Types](#query-method-types)
- [Configuration](#-configuration)
- [Testing](#-testing)
- [Building for Production](#-building-for-production)
- [REST API Documentation](#-rest-api-documentation)
  - [User Management API](#user-management-api)
  - [Librarian Management API](#librarian-management-api)
  - [Book Management API](#book-management-api)
  - [Book Copy Management API](#book-copy-management-api)
  - [Borrowed (Borrowing System) API](#borrowed-borrowing-system-api)
  - [Fine Management API](#fine-management-api)
  - [Payment Management API](#payment-management-api)
  - [Error Responses](#error-responses)
- [Architecture & Design Patterns](#-architecture--design-patterns)
  - [Layered Architecture](#layered-architecture)
  - [DTO Pattern](#dto-pattern)
  - [Mapper Pattern](#mapper-pattern)
  - [Service Layer Features](#service-layer-features)
  - [Exception Handling](#exception-handling)
- [Security Features](#-security-features)
- [Troubleshooting](#-troubleshooting)
- [Development Notes](#-development-notes)
- [Contributing](#-contributing)
- [Support](#-support)

## �🚀 Features

- **User Management**: Complete user registration and management system
- **Author Management**: Track author information including biography and nationality
- **Genre Management**: Organize books by categories and genres
- **Publisher Management**: Manage publishing house information
- **Librarian Management**: Staff and admin role management
- **Book Management**: Comprehensive book catalog with ISBN, titles, descriptions, and metadata
  - **RESTful API**: Complete CRUD operations with search and filtering
  - **Pagination Support**: All list endpoints support pagination and sorting
  - **Advanced Search**: Search by title, author, genre, language, and more
- **Book Copy Management**: Track individual book copies with barcodes, conditions, and locations
- **Borrowing System**: Complete borrowing workflow with due dates and return tracking
- **Fine Management**: Automated fine calculation, payment tracking, and overdue penalties
- **Payment Processing**: Support for multiple payment methods and transaction tracking
- **User Management**: Complete user registration, authentication, and profile management
  - **RESTful API**: Full CRUD operations with advanced filtering
  - **Pagination Support**: All endpoints support pagination and sorting
  - **Status Management**: Activate, deactivate, and suspend user accounts
  - **Password Management**: Secure password change with validation
- **Librarian Management**: Staff and admin account management
  - **RESTful API**: Complete CRUD operations with role management
  - **Pagination Support**: All list endpoints support pagination and sorting
  - **Role Management**: Promote to admin or demote to staff
  - **Advanced Search**: Filter by role, status, name, and combined criteria
- **Security**: Password encryption using BCrypt
- **Database Integration**: PostgreSQL database with JPA/Hibernate
- **DTO Pattern**: Clean separation between entity and presentation layers
- **Mapper Layer**: Automatic mapping between DTOs and entities

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.3.5
- **Java Version**: 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security Crypto (BCrypt password encryption)
- **API Documentation**: Springdoc OpenAPI 2.3.0
- **Build Tool**: Maven 3.11.0
- **Lombok**: For reducing boilerplate code
- **DevTools**: Spring Boot DevTools for hot reloading

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

### 5. Access API Documentation
The application includes interactive API documentation using Swagger UI (OpenAPI 3.0):

**Swagger UI**: 
```
http://localhost:8080/swagger-ui.html
```
or
```
http://localhost:8080/swagger-ui/index.html
```

**OpenAPI JSON**: 
```
http://localhost:8080/v3/api-docs
```

The Swagger UI provides:
- Interactive API documentation
- Try-it-out functionality for all endpoints
- Request/response schemas
- Example values for all DTOs

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/LibraryManagementSystem/LMS/
│   │       ├── controller/       # REST API Controllers
│   │       │   ├── AuthorController.java
│   │       │   ├── BookController.java
│   │       │   ├── BookCopyController.java
│   │       │   ├── BorrowedController.java
│   │       │   ├── FineController.java
│   │       │   ├── GenreController.java
│   │       │   ├── LibrarianController.java
│   │       │   ├── PaymentController.java
│   │       │   ├── PublisherController.java
│   │       │   └── UserController.java
│   │       ├── dto/              # Data Transfer Objects
│   │       │   ├── AuthorRequestDTO.java
│   │       │   ├── AuthorResponseDTO.java
│   │       │   ├── BookRequestDTO.java
│   │       │   ├── BookResponseDTO.java
│   │       │   ├── BookCopyRequestDTO.java
│   │       │   ├── BookCopyResponseDTO.java
│   │       │   ├── BorrowedRequestDTO.java
│   │       │   ├── BorrowedResponseDTO.java
│   │       │   ├── FineRequestDTO.java
│   │       │   ├── FineResponseDTO.java
│   │       │   ├── GenreRequestDTO.java
│   │       │   ├── GenreResponseDTO.java
│   │       │   ├── LibrarianRequestDTO.java
│   │       │   ├── LibrarianResponseDTO.java
│   │       │   ├── PaymentRequestDTO.java
│   │       │   ├── PaymentResponseDTO.java
│   │       │   ├── PublisherRequestDTO.java
│   │       │   ├── PublisherResponseDTO.java
│   │       │   ├── UserRequestDTO.java
│   │       │   ├── UserResponseDTO.java
│   │       │   ├── UserSummaryDTO.java
│   │       │   └── ErrorResponse.java
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
│   │       ├── exception/        # Exception handling
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── ResourceNotFoundException.java
│   │       ├── mapper/           # DTO-Entity mappers
│   │       │   ├── AuthorMapper.java
│   │       │   ├── BookMapper.java
│   │       │   ├── BookCopyMapper.java
│   │       │   ├── BorrowedMapper.java
│   │       │   ├── FineMapper.java
│   │       │   ├── GenreMapper.java
│   │       │   ├── LibrarianMapper.java
│   │       │   ├── PaymentMapper.java
│   │       │   ├── PublisherMapper.java
│   │       │   └── UserMapper.java
│   │       ├── repository/       # Data repositories
│   │       │   ├── AuthorRepository.java
│   │       │   ├── BookCopyRepository.java
│   │       │   ├── BookRepository.java
│   │       │   ├── BorrowedRepository.java
│   │       │   ├── FineRepository.java
│   │       │   ├── GenreRepository.java
│   │       │   ├── LibrarianRepository.java
│   │       │   ├── PaymentRepository.java
│   │       │   ├── PublisherRepository.java
│   │       │   └── UserRepository.java
│   │       ├── service/          # Business logic layer
│   │       │   ├── interfaces/
│   │       │   │   ├── AuthorService.java
│   │       │   │   ├── BookService.java
│   │       │   │   ├── BookCopyService.java
│   │       │   │   ├── BorrowedService.java
│   │       │   │   ├── FineService.java
│   │       │   │   ├── GenreService.java
│   │       │   │   ├── LibrarianService.java
│   │       │   │   ├── PaymentService.java
│   │       │   │   ├── PublisherService.java
│   │       │   │   └── UserService.java
│   │       │   ├── AuthorServiceImpl.java
│   │       │   ├── BookServiceImpl.java
│   │       │   ├── BookCopyServiceImpl.java
│   │       │   ├── BorrowedServiceImpl.java
│   │       │   ├── FineServiceImpl.java
│   │       │   ├── GenreServiceImpl.java
│   │       │   ├── LibrarianServiceImpl.java
│   │       │   ├── PaymentServiceImpl.java
│   │       │   ├── PublisherServiceImpl.java
│   │       │   └── UserServiceImpl.java
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
- **Fields**: id, name, email, password, phoneNumber, address, role, hireDate, status
- **Security**: Passwords are automatically encrypted using BCrypt
- **Roles**: STAFF (default) and ADMIN
- **Status**: Default status is ACTIVE
- **Purpose**: Manage library staff accounts with role-based access control

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

### User Management Enums

#### Role
- **STAFF**: Regular library staff member
- **ADMIN**: Administrator with full access

#### Status
- **ACTIVE**: User/Librarian account is active
- **INACTIVE**: User/Librarian account is inactive
- **SUSPENDED**: User/Librarian account is suspended

## 🗃️ Repository Layer

The application includes comprehensive repository interfaces for all entities, providing both basic CRUD operations and custom query methods.

### Repository Features

All repositories extend `JpaRepository<Entity, Long>` and include:
- **Standard CRUD operations** (inherited from JpaRepository)
- **Custom query methods** using Spring Data JPA naming conventions
- **Complex queries** using `@Query` annotations with JPQL
- **Validation methods** for checking existence
- **Business-specific queries** for common operations

### Repository Details

#### AuthorRepository
**Purpose**: Manage author information
- Find authors by name (exact/partial match, case-insensitive)
- Find authors by nationality
- Check if author exists by name
- Search functionality for author discovery

#### BookRepository
**Purpose**: Manage book catalog
- Find books by ISBN (unique identifier)
- Search books by title (case-insensitive, partial match)
- Filter books by status, language, and publisher
- Find books by author name using JOIN queries
- Find books by genre name using JOIN queries
- Advanced keyword search across titles and authors
- Count books by status for inventory management
- Check ISBN uniqueness

#### BookCopyRepository
**Purpose**: Track individual physical book copies
- Find book copies by unique barcode
- List all copies of a specific book
- Filter copies by status (AVAILABLE, BORROWED, UNAVAILABLE)
- Find available copies for borrowing
- Filter by location and condition
- Count total and available copies per book
- Verify barcode uniqueness

#### BorrowedRepository
**Purpose**: Manage borrowing transactions
- Find all borrowing records for a user
- Filter records by borrow status
- Find currently active (unreturned) borrows
- Identify overdue records based on due date
- Find records due soon (within specified days)
- Track borrowing history by book copy
- Find current borrower of a specific copy
- Search by date range for reporting
- Count active borrows per user (for borrowing limits)

#### FineRepository
**Purpose**: Track and manage fines
- Find fine associated with a borrowed record
- Filter fines by status (PENDING, PAID, WAIVED)
- Find all fines for a specific user
- Identify pending (unpaid) fines
- Filter by assessment date range
- Find fines exceeding a certain amount
- Calculate total pending fines per user
- Calculate total fines by status
- Count fines by status for reporting

#### LibrarianRepository
**Purpose**: Manage library staff accounts
- Find librarian by email (for authentication)
- Filter librarians by role (STAFF, ADMIN)
- Filter by account status (ACTIVE, INACTIVE, SUSPENDED)
- Search librarians by name (case-insensitive)
- Verify email and phone number uniqueness
- Count librarians by role and status

#### PaymentRepository
**Purpose**: Process and track payments
- Find all payments for a specific fine
- Find payment by transaction ID
- Filter by payment status and method
- Find all payments made by a user
- Search by payment date range
- Calculate total amount paid for a fine
- Calculate total payments by user
- Calculate revenue within date range
- Track successful payments by method
- Verify transaction ID uniqueness

#### PublisherRepository
**Purpose**: Manage publisher information
- Find publisher by name (exact match)
- Search publishers by name (case-insensitive, partial)
- Filter publishers by country
- Find publisher by email
- Verify name and email uniqueness

#### UserRepository
**Purpose**: Manage library user accounts
- Find user by email (for authentication)
- Find user by phone number
- Filter users by status
- Search users by name (case-insensitive, partial)
- Find users by membership date range
- Identify users with overdue books (using complex JOIN query)
- Find users with pending fines (using complex JOIN query)
- Verify email and phone uniqueness
- Count users by status

#### GenreRepository
**Purpose**: Manage book genres/categories
- Basic CRUD operations for genre management
- Extensible for future custom queries

### Query Method Types

1. **Derived Query Methods**: Spring Data automatically implements methods based on naming conventions
   - Example: `findByName(String name)`
   - Example: `existsByEmail(String email)`

2. **Custom JPQL Queries**: Complex queries using `@Query` annotation
   - Example: Finding overdue records
   - Example: Searching across multiple related entities

3. **Aggregation Methods**: Calculate totals and counts
   - Example: `countByStatus(Status status)`
   - Example: `calculateTotalPendingFinesByUserId(Long userId)`

### Repository Usage Example

```java
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    
    public List<Book> searchAvailableBooks(String keyword) {
        List<Book> books = bookRepository.searchBooks(keyword);
        return books.stream()
                   .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                   .collect(Collectors.toList());
    }
}
```

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

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=BookServiceTest
```

### Test Coverage
The application includes tests for:
- Service layer business logic
- Repository custom queries
- Mapper conversions
- Controller endpoints (integration tests)

### Manual API Testing with cURL

#### Create a Book
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-0-13-468599-1",
    "title": "Clean Code",
    "description": "A Handbook of Agile Software Craftsmanship",
    "publicationDate": "2008-08-01",
    "language": "English",
    "pageCount": 464,
    "publisherId": 1,
    "authorIds": [1],
    "genreIds": [1]
  }'
```

#### Get All Books (Paginated)
```bash
curl -X GET "http://localhost:8080/api/books?page=0&size=10&sortBy=title&sortDirection=ASC"
```

#### Search Books by Title
```bash
curl -X GET "http://localhost:8080/api/books/search/title?title=clean"
```

#### Get Available Books
```bash
curl -X GET "http://localhost:8080/api/books/status/AVAILABLE?page=0&size=20"
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

## 🌐 REST API Documentation

The Library Management System provides a comprehensive RESTful API for managing all library operations.

### API Base URL
```
http://localhost:8080/api
```

### User Management API

Complete user management system for library patrons with profile management, status control, and borrowing eligibility tracking.

#### Core CRUD Operations

##### 1. Create User
**Endpoint**: `POST /api/users`

**Request Body**:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "phoneNumber": "555-0123",
  "address": "123 Main St, City, State",
  "membershipDate": "2025-11-26",
  "status": "ACTIVE"
}
```

**Notes**:
- `membershipDate` is optional and defaults to current date if not provided
- `status` is optional and defaults to `ACTIVE` if not provided
- Password is automatically encrypted using BCrypt
- Email and phone number must be unique

**Response**: `201 CREATED`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "555-0123",
  "address": "123 Main St, City, State",
  "membershipDate": "2025-11-26",
  "status": "ACTIVE"
}
```

##### 2. Get User by ID
**Endpoint**: `GET /api/users/{id}`

**Example**: `GET /api/users/1`

**Response**: `200 OK` - Returns UserResponseDTO

##### 3. Get All Users
**Endpoint**: `GET /api/users`

**Query Parameters** (all optional):
- `page` - Page number (0-indexed)
- `size` - Number of items per page
- `sortBy` - Field to sort by (default: "name")
- `sortDirection` - ASC or DESC (default: ASC)

**Examples**:
```bash
# Get all users (list)
GET /api/users

# Get paginated users
GET /api/users?page=0&size=10&sortBy=name&sortDirection=ASC
```

**Response (List)**: `200 OK` - Array of UserResponseDTOs

**Response (Paginated)**: `200 OK` - Page object with users

##### 4. Update User
**Endpoint**: `PUT /api/users/{id}`

**Request Body**: Same as Create User

**Response**: `200 OK` - Returns updated UserResponseDTO

##### 5. Delete User
**Endpoint**: `DELETE /api/users/{id}`

**Response**: `204 NO CONTENT`

#### Search and Filter Endpoints

##### Get User by Email
**Endpoint**: `GET /api/users/email/{email}`

**Example**: `GET /api/users/email/john.doe@example.com`

**Response**: `200 OK` - Single user object

**Use Case**: User authentication and profile lookup

##### Get User by Phone Number
**Endpoint**: `GET /api/users/phone/{phoneNumber}`

**Example**: `GET /api/users/phone/555-0123`

**Response**: `200 OK` - Single user object

**Use Case**: Alternative user lookup method

##### Filter Users by Status
**Endpoint**: `GET /api/users/status/{status}`

**Status Values**: `ACTIVE`, `INACTIVE`

**Query Parameters**: `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/users/status/ACTIVE?page=0&size=20`

**Response**: `200 OK` - List or Page of users with specified status

**Use Case**: View all active or inactive users

##### Search Users by Name
**Endpoint**: `GET /api/users/search/name?name={keyword}`

**Example**: `GET /api/users/search/name?name=John&page=0&size=10`

**Response**: `200 OK` - List or Page of users matching the name (case-insensitive)

**Use Case**: Quick user lookup by partial name

##### Filter by Membership Date Range
**Endpoint**: `GET /api/users/search/membership-date`

**Query Parameters**:
- `startDate` (required): Start date (format: `yyyy-MM-dd`)
- `endDate` (required): End date (format: `yyyy-MM-dd`)
- `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/users/search/membership-date?startDate=2025-01-01&endDate=2025-12-31`

**Response**: `200 OK` - List or Page of users who joined within date range

**Use Case**: Analyze user registrations by period

##### Get Users with Overdue Books
**Endpoint**: `GET /api/users/overdue-books`

**Query Parameters**: `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/users/overdue-books?page=0&size=50`

**Response**: `200 OK` - List or Page of users with overdue books

**Use Case**: Identify users who need reminders or restrictions

##### Get Users with Pending Fines
**Endpoint**: `GET /api/users/pending-fines`

**Query Parameters**: `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/users/pending-fines?page=0&size=50`

**Response**: `200 OK` - List or Page of users with unpaid fines

**Use Case**: Collections and account restrictions

#### Account Status Management

##### Activate User
**Endpoint**: `POST /api/users/{id}/activate`

**Example**: `POST /api/users/1/activate`

**Response**: `200 OK`
```json
{
  "id": 1,
  "name": "John Doe",
  "status": "ACTIVE",
  ...
}
```

**Use Case**: Restore inactive user account

##### Deactivate User
**Endpoint**: `POST /api/users/{id}/deactivate`

**Example**: `POST /api/users/2/deactivate`

**Response**: `200 OK` - Returns updated user with INACTIVE status

**Use Case**: Temporarily disable user account

##### Suspend User
**Endpoint**: `POST /api/users/{id}/suspend`

**Example**: `POST /api/users/3/suspend`

**Response**: `200 OK` - Returns updated user with INACTIVE status

**Notes**: Currently sets status to INACTIVE (suspension functionality)

**Use Case**: Discipline action for policy violations

#### Password Management

##### Change Password
**Endpoint**: `POST /api/users/{id}/change-password`

**Query Parameters**:
- `oldPassword` (required): Current password for verification
- `newPassword` (required): New password (minimum 6 characters)

**Example**: `POST /api/users/1/change-password?oldPassword=oldPass123&newPassword=newSecurePass456`

**Response**: `200 OK`
```json
{
  "message": "Password changed successfully"
}
```

**Security Features**:
- Verifies old password before allowing change
- Validates new password length (minimum 6 characters)
- Automatically encrypts new password using BCrypt

#### Validation Endpoints

##### Check if Email Exists
**Endpoint**: `GET /api/users/exists/email/{email}`

**Example**: `GET /api/users/exists/email/john.doe@example.com`

**Response**: `200 OK`
```json
{
  "exists": true
}
```

**Use Case**: Email uniqueness validation during registration

##### Check if Phone Number Exists
**Endpoint**: `GET /api/users/exists/phone/{phoneNumber}`

**Example**: `GET /api/users/exists/phone/555-0123`

**Response**: `200 OK`
```json
{
  "exists": false
}
```

**Use Case**: Phone number uniqueness validation

##### Count Users by Status
**Endpoint**: `GET /api/users/count/status/{status}`

**Example**: `GET /api/users/count/status/ACTIVE`

**Response**: `200 OK`
```json
{
  "count": 1250
}
```

**Use Case**: Dashboard statistics, member reports

##### Check if User Can Borrow
**Endpoint**: `GET /api/users/{id}/can-borrow`

**Example**: `GET /api/users/1/can-borrow`

**Response**: `200 OK`
```json
{
  "canBorrow": true
}
```

**Business Rules Checked**:
- User status is ACTIVE
- User has no overdue books
- User has no pending fines

**Use Case**: Validate before processing book checkout

#### Common Use Cases

##### User Registration Workflow
1. Validate email: `GET /api/users/exists/email/{email}`
2. Validate phone: `GET /api/users/exists/phone/{phoneNumber}`
3. Create user: `POST /api/users`
4. Verify creation: `GET /api/users/{id}`

##### User Dashboard
1. Get user details: `GET /api/users/{id}`
2. Check borrowing eligibility: `GET /api/users/{id}/can-borrow`
3. View overdue items: Check via Borrowed API
4. View pending fines: Check via Fine API

##### Administrative Reports
1. Count active members: `GET /api/users/count/status/ACTIVE`
2. New members this month: `GET /api/users/search/membership-date?startDate=...`
3. Users with issues: `GET /api/users/overdue-books`, `GET /api/users/pending-fines`

##### Account Management
1. View user profile: `GET /api/users/{id}`
2. Update profile: `PUT /api/users/{id}`
3. Change password: `POST /api/users/{id}/change-password?...`
4. Deactivate account: `POST /api/users/{id}/deactivate`

##### Collections Management
1. Get users with pending fines: `GET /api/users/pending-fines?page=0&size=100`
2. Get users with overdue books: `GET /api/users/overdue-books?page=0&size=100`
3. Process individual user fines and returns

---

### Librarian Management API

Complete staff management system with role-based access control, account management, and advanced filtering capabilities.

#### Core CRUD Operations

##### 1. Create Librarian
**Endpoint**: `POST /api/librarians`

**Request Body**:
```json
{
  "name": "Jane Smith",
  "email": "jane.smith@library.com",
  "password": "securePassword123",
  "phoneNumber": "555-0456",
  "address": "456 Library Ave, City, State",
  "role": "STAFF",
  "hireDate": "2025-11-26",
  "status": "ACTIVE"
}
```

**Notes**:
- `role` is optional and defaults to `STAFF` if not provided
- `hireDate` is optional and defaults to current date if not provided
- `status` is optional and defaults to `ACTIVE` if not provided
- Password is automatically encrypted using BCrypt

**Response**: `201 CREATED`
```json
{
  "id": 1,
  "name": "Jane Smith",
  "email": "jane.smith@library.com",
  "phoneNumber": "555-0456",
  "address": "456 Library Ave, City, State",
  "role": "STAFF",
  "hireDate": "2025-11-26",
  "status": "ACTIVE"
}
```

##### 2. Get Librarian by ID
**Endpoint**: `GET /api/librarians/{id}`

**Example**: `GET /api/librarians/1`

**Response**: `200 OK` - Returns LibrarianResponseDTO

##### 3. Get All Librarians
**Endpoint**: `GET /api/librarians`

**Query Parameters** (all optional):
- `page` - Page number (0-indexed)
- `size` - Number of items per page
- `sortBy` - Field to sort by (default: "name")
- `sortDirection` - ASC or DESC (default: ASC)

**Examples**:
```bash
# Get all librarians (list)
GET /api/librarians

# Get paginated librarians
GET /api/librarians?page=0&size=10&sortBy=name&sortDirection=ASC
```

**Response (List)**: `200 OK` - Array of LibrarianResponseDTOs

**Response (Paginated)**: `200 OK` - Page object with librarians

##### 4. Update Librarian
**Endpoint**: `PUT /api/librarians/{id}`

**Request Body**: Same as Create Librarian

**Response**: `200 OK` - Returns updated LibrarianResponseDTO

##### 5. Delete Librarian
**Endpoint**: `DELETE /api/librarians/{id}`

**Response**: `204 NO CONTENT`

#### Search and Filter Endpoints

##### Get Librarian by Email
**Endpoint**: `GET /api/librarians/email/{email}`

**Example**: `GET /api/librarians/email/jane.smith@library.com`

**Response**: `200 OK` - Single librarian object

**Use Case**: Authentication and profile lookup

##### Filter Librarians by Role
**Endpoint**: `GET /api/librarians/role/{role}`

**Role Values**: `STAFF`, `ADMIN`

**Query Parameters**: `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/librarians/role/ADMIN?page=0&size=10`

**Response**: `200 OK` - List or Page of librarians with specified role

**Use Case**: View all administrators or staff members

##### Filter Librarians by Status
**Endpoint**: `GET /api/librarians/status/{status}`

**Status Values**: `ACTIVE`, `INACTIVE`

**Example**: `GET /api/librarians/status/ACTIVE?page=0&size=20`

**Response**: `200 OK` - List or Page of librarians with specified status

**Use Case**: View active or inactive staff members

##### Search Librarians by Name
**Endpoint**: `GET /api/librarians/search/name?name={keyword}`

**Example**: `GET /api/librarians/search/name?name=Jane&page=0&size=10`

**Response**: `200 OK` - List or Page of librarians matching the name (case-insensitive)

**Use Case**: Quick staff member lookup

##### Filter by Status and Role
**Endpoint**: `GET /api/librarians/search/status-role?status={status}&role={role}`

**Example**: `GET /api/librarians/search/status-role?status=ACTIVE&role=ADMIN`

**Response**: `200 OK` - List or Page of librarians matching both criteria

**Use Case**: Find all active administrators

#### Account Status Management

##### Activate Librarian
**Endpoint**: `POST /api/librarians/{id}/activate`

**Example**: `POST /api/librarians/1/activate`

**Response**: `200 OK`
```json
{
  "id": 1,
  "name": "Jane Smith",
  "status": "ACTIVE",
  ...
}
```

**Use Case**: Restore inactive librarian account

##### Deactivate Librarian
**Endpoint**: `POST /api/librarians/{id}/deactivate`

**Example**: `POST /api/librarians/2/deactivate`

**Response**: `200 OK` - Returns updated librarian with INACTIVE status

**Use Case**: Temporarily disable librarian account

##### Suspend Librarian
**Endpoint**: `POST /api/librarians/{id}/suspend`

**Example**: `POST /api/librarians/3/suspend`

**Response**: `200 OK` - Returns updated librarian with INACTIVE status

**Notes**: Currently sets status to INACTIVE (suspension functionality)

#### Password Management

##### Change Password
**Endpoint**: `POST /api/librarians/{id}/change-password`

**Query Parameters**:
- `oldPassword` (required): Current password for verification
- `newPassword` (required): New password (minimum 6 characters)

**Example**: `POST /api/librarians/1/change-password?oldPassword=oldPass123&newPassword=newSecurePass456`

**Response**: `200 OK`
```json
{
  "message": "Password changed successfully"
}
```

**Security Features**:
- Verifies old password before allowing change
- Validates new password length (minimum 6 characters)
- Automatically encrypts new password using BCrypt

#### Role Management

##### Promote to Admin
**Endpoint**: `POST /api/librarians/{id}/promote`

**Example**: `POST /api/librarians/5/promote`

**Response**: `200 OK`
```json
{
  "id": 5,
  "name": "Jane Smith",
  "role": "ADMIN",
  ...
}
```

**Validation**:
- Throws exception if librarian is already an admin
- Only STAFF can be promoted to ADMIN

**Use Case**: Grant administrative privileges to staff member

##### Demote to Staff
**Endpoint**: `POST /api/librarians/{id}/demote`

**Example**: `POST /api/librarians/5/demote`

**Response**: `200 OK`
```json
{
  "id": 5,
  "name": "Jane Smith",
  "role": "STAFF",
  ...
}
```

**Validation**:
- Throws exception if librarian is already a staff member
- Only ADMIN can be demoted to STAFF

**Use Case**: Remove administrative privileges

#### Validation Endpoints

##### Check if Email Exists
**Endpoint**: `GET /api/librarians/exists/email/{email}`

**Example**: `GET /api/librarians/exists/email/jane.smith@library.com`

**Response**: `200 OK`
```json
{
  "exists": true
}
```

**Use Case**: Email uniqueness validation during registration

##### Check if Phone Number Exists
**Endpoint**: `GET /api/librarians/exists/phone/{phoneNumber}`

**Example**: `GET /api/librarians/exists/phone/555-0456`

**Response**: `200 OK`
```json
{
  "exists": false
}
```

**Use Case**: Phone number uniqueness validation

##### Count Librarians by Role
**Endpoint**: `GET /api/librarians/count/role/{role}`

**Example**: `GET /api/librarians/count/role/ADMIN`

**Response**: `200 OK`
```json
{
  "count": 3
}
```

**Use Case**: Dashboard statistics, staffing reports

##### Count Librarians by Status
**Endpoint**: `GET /api/librarians/count/status/{status}`

**Example**: `GET /api/librarians/count/status/ACTIVE`

**Response**: `200 OK`
```json
{
  "count": 15
}
```

**Use Case**: Active staff count for reporting

#### Common Use Cases

##### Staff Registration Workflow
1. Validate email: `GET /api/librarians/exists/email/{email}`
2. Validate phone: `GET /api/librarians/exists/phone/{phoneNumber}`
3. Create librarian: `POST /api/librarians`
4. Verify creation: `GET /api/librarians/{id}`

##### Administrative Dashboard
1. Count active staff: `GET /api/librarians/count/status/ACTIVE`
2. Count admins: `GET /api/librarians/count/role/ADMIN`
3. List all admins: `GET /api/librarians/role/ADMIN`
4. Search staff: `GET /api/librarians/search/name?name=...`

##### Staff Management
1. View all staff: `GET /api/librarians?page=0&size=20`
2. Filter active staff: `GET /api/librarians/status/ACTIVE`
3. Promote staff to admin: `POST /api/librarians/{id}/promote`
4. Deactivate staff: `POST /api/librarians/{id}/deactivate`

##### Password Reset
1. Verify librarian identity
2. Change password: `POST /api/librarians/{id}/change-password?oldPassword=...&newPassword=...`
3. Confirm success

##### Role Transition
1. Get librarian details: `GET /api/librarians/{id}`
2. Promote to admin: `POST /api/librarians/{id}/promote`
3. Or demote to staff: `POST /api/librarians/{id}/demote`
4. Verify role change: `GET /api/librarians/{id}`

---

### Book Management API

#### 1. Create a New Book
**Endpoint**: `POST /api/books`

**Request Body**:
```json
{
  "isbn": "978-0-13-468599-1",
  "title": "Clean Code",
  "description": "A Handbook of Agile Software Craftsmanship",
  "publicationDate": "2008-08-01",
  "language": "English",
  "pageCount": 464,
  "publisherId": 1,
  "authorIds": [1, 2],
  "genreIds": [3, 4]
}
```

**Response**: `201 CREATED`
```json
{
  "id": 1,
  "isbn": "978-0-13-468599-1",
  "title": "Clean Code",
  "description": "A Handbook of Agile Software Craftsmanship",
  "publicationDate": "2008-08-01",
  "language": "English",
  "pageCount": 464,
  "status": "AVAILABLE",
  "publisher": {
    "id": 1,
    "name": "Prentice Hall",
    "address": "...",
    "email": "...",
    "country": "USA"
  },
  "authors": [
    {
      "id": 1,
      "name": "Robert C. Martin",
      "biography": "...",
      "birthDate": "1952-12-05",
      "nationality": "American"
    }
  ],
  "genres": [
    {
      "id": 3,
      "name": "Programming",
      "description": "..."
    }
  ]
}
```

#### 2. Get Book by ID
**Endpoint**: `GET /api/books/{id}`

**Response**: `200 OK`
```json
{
  "id": 1,
  "isbn": "978-0-13-468599-1",
  "title": "Clean Code",
  ...
}
```

#### 3. Get All Books
**Endpoint**: `GET /api/books`

**Query Parameters** (all optional):
- `page` - Page number (0-indexed)
- `size` - Number of items per page
- `sortBy` - Field to sort by (default: "id")
- `sortDirection` - ASC or DESC (default: ASC)

**Examples**:
```bash
# Get all books (list)
GET /api/books

# Get paginated books
GET /api/books?page=0&size=10&sortBy=title&sortDirection=ASC
```

**Response (List)**: `200 OK`
```json
[
  {
    "id": 1,
    "title": "Clean Code",
    ...
  },
  {
    "id": 2,
    "title": "The Pragmatic Programmer",
    ...
  }
]
```

**Response (Paginated)**: `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "title": "Clean Code",
      ...
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalPages": 5,
  "totalElements": 42,
  "last": false,
  "first": true,
  "numberOfElements": 10
}
```

#### 4. Update Book
**Endpoint**: `PUT /api/books/{id}`

**Request Body**: Same as Create Book

**Response**: `200 OK`

#### 5. Delete Book
**Endpoint**: `DELETE /api/books/{id}`

**Response**: `204 NO CONTENT`

#### 6. Search/Filter Books

##### Get Book by ISBN
**Endpoint**: `GET /api/books/isbn/{isbn}`

**Example**: `GET /api/books/isbn/978-0-13-468599-1`

**Response**: `200 OK` - Single book object

##### Search Books by Title
**Endpoint**: `GET /api/books/search/title?title={keyword}`

**Query Parameters**:
- `title` (required) - Title keyword to search
- `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/books/search/title?title=clean&page=0&size=10`

**Response**: `200 OK` - List or Page of books

##### Filter Books by Status
**Endpoint**: `GET /api/books/status/{status}`

**Status Values**: `AVAILABLE` or `UNAVAILABLE`

**Example**: `GET /api/books/status/AVAILABLE?page=0&size=20`

**Response**: `200 OK` - List or Page of books

##### Filter Books by Publisher
**Endpoint**: `GET /api/books/publisher/{publisherId}`

**Example**: `GET /api/books/publisher/1?page=0&size=10`

**Response**: `200 OK` - List or Page of books

##### Filter Books by Language
**Endpoint**: `GET /api/books/language/{language}`

**Example**: `GET /api/books/language/English?page=0&size=10`

**Response**: `200 OK` - List or Page of books

##### Search Books by Author Name
**Endpoint**: `GET /api/books/search/author?authorName={name}`

**Example**: `GET /api/books/search/author?authorName=Martin&page=0&size=10`

**Response**: `200 OK` - List or Page of books

##### Search Books by Genre Name
**Endpoint**: `GET /api/books/search/genre?genreName={name}`

**Example**: `GET /api/books/search/genre?genreName=Programming`

**Response**: `200 OK` - List or Page of books

##### General Search (Title or Author)
**Endpoint**: `GET /api/books/search?keyword={keyword}`

**Example**: `GET /api/books/search?keyword=clean&page=0&size=10`

**Response**: `200 OK` - List or Page of books

#### 7. Utility Endpoints

##### Check if ISBN Exists
**Endpoint**: `GET /api/books/exists/isbn/{isbn}`

**Example**: `GET /api/books/exists/isbn/978-0-13-468599-1`

**Response**: `200 OK`
```json
true
```

##### Count Books by Status
**Endpoint**: `GET /api/books/count/status/{status}`

**Example**: `GET /api/books/count/status/AVAILABLE`

**Response**: `200 OK`
```json
42
```

### Author Management API

#### Create Author
**Endpoint**: `POST /api/authors`

**Request Body**:
```json
{
  "name": "Robert C. Martin",
  "biography": "Software engineer and author",
  "birthDate": "1952-12-05",
  "nationality": "American"
}
```

**Response**: `201 CREATED`

#### Get Author by ID
**Endpoint**: `GET /api/authors/{id}`

#### Get All Authors
**Endpoint**: `GET /api/authors`

#### Update Author
**Endpoint**: `PUT /api/authors/{id}`

#### Delete Author
**Endpoint**: `DELETE /api/authors/{id}`

### Genre Management API

#### Create Genre
**Endpoint**: `POST /api/genres`

**Request Body**:
```json
{
  "name": "Programming",
  "description": "Books about software development"
}
```

#### Get Genre by ID
**Endpoint**: `GET /api/genres/{id}`

#### Get All Genres
**Endpoint**: `GET /api/genres`

#### Update Genre
**Endpoint**: `PUT /api/genres/{id}`

#### Delete Genre
**Endpoint**: `DELETE /api/genres/{id}`

### Publisher Management API

#### Create Publisher
**Endpoint**: `POST /api/publishers`

**Request Body**:
```json
{
  "name": "Prentice Hall",
  "address": "123 Main St",
  "email": "info@prenticehall.com",
  "country": "USA"
}
```

#### Get Publisher by ID
**Endpoint**: `GET /api/publishers/{id}`

#### Get All Publishers
**Endpoint**: `GET /api/publishers`

#### Update Publisher
**Endpoint**: `PUT /api/publishers/{id}`

#### Delete Publisher
**Endpoint**: `DELETE /api/publishers/{id}`

### BookCopy Management API

The BookCopy API manages individual physical copies of books in the library.

#### Create BookCopy
**Endpoint**: `POST /api/book-copies`

**Request Body**:
```json
{
  "barcode": "BC123456",
  "condition": "NEW",
  "acquisitionDate": "2025-11-26",
  "location": "Shelf A-12",
  "bookId": 1
}
```

**Response**: `201 CREATED`
```json
{
  "id": 1,
  "barcode": "BC123456",
  "condition": "NEW",
  "status": "AVAILABLE",
  "acquisitionDate": "2025-11-26",
  "location": "Shelf A-12",
  "book": {
    "id": 1,
    "isbn": "978-0-13-468599-1",
    "title": "Clean Code",
    ...
  }
}
```

#### Get BookCopy by ID
**Endpoint**: `GET /api/book-copies/{id}`

**Response**: `200 OK`

#### Get All BookCopies
**Endpoint**: `GET /api/book-copies`

**Query Parameters** (all optional):
- `page` - Page number (0-indexed)
- `size` - Items per page
- `sortBy` - Sort field (default: "id")
- `sortDirection` - ASC or DESC (default: ASC)

**Example**: `GET /api/book-copies?page=0&size=10&sortBy=barcode&sortDirection=ASC`

#### Update BookCopy
**Endpoint**: `PUT /api/book-copies/{id}`

**Request Body**: Same as Create BookCopy

#### Delete BookCopy
**Endpoint**: `DELETE /api/book-copies/{id}`

**Response**: `204 NO CONTENT`

#### Search/Filter BookCopy Endpoints

##### Get BookCopy by Barcode
**Endpoint**: `GET /api/book-copies/barcode/{barcode}`

**Example**: `GET /api/book-copies/barcode/BC123456`

**Response**: `200 OK` - Single BookCopy object

##### Get All Copies of a Book
**Endpoint**: `GET /api/book-copies/book/{bookId}`

**Query Parameters**: `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/book-copies/book/1?page=0&size=10`

**Response**: `200 OK` - List or Page of BookCopies

##### Filter BookCopies by Status
**Endpoint**: `GET /api/book-copies/status/{status}`

**Status Values**: `AVAILABLE`, `BORROWED`, `UNAVAILABLE`

**Example**: `GET /api/book-copies/status/AVAILABLE?page=0&size=20`

**Response**: `200 OK` - List or Page of BookCopies

##### Get Copies of Book with Specific Status
**Endpoint**: `GET /api/book-copies/book/{bookId}/status/{status}`

**Example**: `GET /api/book-copies/book/1/status/AVAILABLE`

**Response**: `200 OK` - List or Page of BookCopies

##### Get Available Copies of a Book
**Endpoint**: `GET /api/book-copies/book/{bookId}/available`

**Example**: `GET /api/book-copies/book/1/available?page=0&size=10`

**Response**: `200 OK` - List or Page of available copies only

##### Filter BookCopies by Location
**Endpoint**: `GET /api/book-copies/location/{location}`

**Example**: `GET /api/book-copies/location/Shelf%20A-12`

**Response**: `200 OK` - List or Page of BookCopies

##### Filter BookCopies by Condition
**Endpoint**: `GET /api/book-copies/condition/{condition}`

**Condition Values**: `NEW`, `GOOD`, `FAIR`, `POOR`

**Example**: `GET /api/book-copies/condition/NEW`

**Response**: `200 OK` - List or Page of BookCopies

##### Filter by Location and Status
**Endpoint**: `GET /api/book-copies/location/{location}/status/{status}`

**Example**: `GET /api/book-copies/location/Shelf%20A-12/status/AVAILABLE`

**Response**: `200 OK` - List or Page of BookCopies

#### Utility Endpoints

##### Check if Barcode Exists
**Endpoint**: `GET /api/book-copies/exists/barcode/{barcode}`

**Example**: `GET /api/book-copies/exists/barcode/BC123456`

**Response**: `200 OK`
```json
true
```

##### Count Total Copies of a Book
**Endpoint**: `GET /api/book-copies/count/book/{bookId}`

**Example**: `GET /api/book-copies/count/book/1`

**Response**: `200 OK`
```json
5
```

##### Count BookCopies by Status
**Endpoint**: `GET /api/book-copies/count/status/{status}`

**Example**: `GET /api/book-copies/count/status/AVAILABLE`

**Response**: `200 OK`
```json
42
```

##### Count Available Copies of a Book
**Endpoint**: `GET /api/book-copies/count/book/{bookId}/available`

**Example**: `GET /api/book-copies/count/book/1/available`

**Response**: `200 OK`
```json
3
```

---

### Borrowed (Borrowing System) API

The Borrowed API manages the complete borrowing workflow including checkout, returns, overdue tracking, and borrowing limits.

#### Core CRUD Operations

##### 1. Create a Borrow Record (Checkout a Book)
**Endpoint**: `POST /api/borrowed`

**Request Body**:
```json
{
  "borrowDate": "2025-11-26",
  "dueDate": "2025-12-10",
  "userId": 1,
  "bookCopyId": 5
}
```

**Notes**:
- `dueDate` is optional. If not provided, it will be automatically calculated as borrowDate + 14 days
- The book copy status is automatically updated to `BORROWED`
- Validates that the book copy is available before borrowing

**Response**: `201 CREATED`
```json
{
  "id": 1,
  "borrowDate": "2025-11-26",
  "dueDate": "2025-12-10",
  "returnDate": null,
  "status": "BORROWED",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com"
  },
  "bookCopy": {
    "id": 5,
    "barcode": "BC123456",
    "condition": "GOOD",
    "status": "BORROWED",
    "location": "Shelf A-12",
    "book": {
      "id": 1,
      "isbn": "978-0-13-468599-1",
      "title": "Clean Code"
    }
  }
}
```

##### 2. Get Borrow Record by ID
**Endpoint**: `GET /api/borrowed/{id}`

**Example**: `GET /api/borrowed/1`

**Response**: `200 OK` - Returns BorrowedResponseDTO

##### 3. Get All Borrow Records
**Endpoint**: `GET /api/borrowed`

**Query Parameters**:
- `page` (optional): Page number (0-indexed)
- `size` (optional): Number of items per page
- `sortBy` (default: `borrowDate`): Field to sort by
- `sortDirection` (default: `DESC`): `ASC` or `DESC`

**Example**: `GET /api/borrowed?page=0&size=20&sortBy=borrowDate&sortDirection=DESC`

**Response**: `200 OK` - List or Page of BorrowedResponseDTOs

##### 4. Update Borrow Record
**Endpoint**: `PUT /api/borrowed/{id}`

**Request Body**: Same as create

**Response**: `200 OK` - Returns updated BorrowedResponseDTO

##### 5. Delete Borrow Record
**Endpoint**: `DELETE /api/borrowed/{id}`

**Response**: `204 NO CONTENT`

#### Return Operations

##### Return a Book
**Endpoint**: `POST /api/borrowed/{id}/return`

**Query Parameters**:
- `returnDate` (optional): The return date (defaults to today if not provided)

**Example**: `POST /api/borrowed/1/return?returnDate=2025-12-05`

**Response**: `200 OK`
```json
{
  "id": 1,
  "borrowDate": "2025-11-26",
  "dueDate": "2025-12-10",
  "returnDate": "2025-12-05",
  "status": "RETURNED",
  "user": { ... },
  "bookCopy": {
    "status": "AVAILABLE",
    ...
  }
}
```

**Notes**:
- Updates borrow status to `RETURNED`
- Automatically updates book copy status back to `AVAILABLE`
- Validates that the book hasn't already been returned

#### Filter and Search Endpoints

##### Get Borrows by User
**Endpoint**: `GET /api/borrowed/user/{userId}`

**Example**: `GET /api/borrowed/user/1?page=0&size=10`

**Response**: `200 OK` - List or Page of user's borrow records

##### Get Borrows by Book Copy
**Endpoint**: `GET /api/borrowed/book-copy/{bookCopyId}`

**Example**: `GET /api/borrowed/book-copy/5`

**Response**: `200 OK` - List or Page of borrow history for a specific book copy

##### Get Borrows by Status
**Endpoint**: `GET /api/borrowed/status/{status}`

**Status Values**: `BORROWED`, `RETURNED`, `OVERDUE`

**Example**: `GET /api/borrowed/status/BORROWED?page=0&size=20`

**Response**: `200 OK` - List or Page of filtered records

##### Get Borrows by User and Status
**Endpoint**: `GET /api/borrowed/user/{userId}/status/{status}`

**Example**: `GET /api/borrowed/user/1/status/BORROWED`

**Response**: `200 OK` - List or Page of filtered records

##### Get Active Borrows by User
**Endpoint**: `GET /api/borrowed/user/{userId}/active`

**Example**: `GET /api/borrowed/user/1/active`

**Response**: `200 OK` - Returns currently borrowed items (not yet returned)

#### Overdue Management

##### Get All Overdue Borrows
**Endpoint**: `GET /api/borrowed/overdue`

**Example**: `GET /api/borrowed/overdue?page=0&size=50&sortBy=dueDate&sortDirection=ASC`

**Response**: `200 OK` - List or Page of overdue borrow records

##### Get Overdue Borrows by User
**Endpoint**: `GET /api/borrowed/user/{userId}/overdue`

**Example**: `GET /api/borrowed/user/1/overdue`

**Response**: `200 OK` - List or Page of user's overdue items

##### Mark Overdue Records (Batch Operation)
**Endpoint**: `POST /api/borrowed/mark-overdue`

**Response**: `200 OK`
```json
{
  "message": "Overdue records have been updated"
}
```

**Notes**:
- This endpoint should be called periodically (e.g., daily via cron job)
- Updates all records where dueDate < currentDate and status = BORROWED to status = OVERDUE

#### Date Range Searches

##### Search by Borrow Date Range
**Endpoint**: `GET /api/borrowed/search/borrow-date`

**Query Parameters**:
- `startDate` (required): Start date (format: `yyyy-MM-dd`)
- `endDate` (required): End date (format: `yyyy-MM-dd`)
- `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/borrowed/search/borrow-date?startDate=2025-11-01&endDate=2025-11-30`

**Response**: `200 OK` - List or Page of records within date range

##### Search by Due Date Range
**Endpoint**: `GET /api/borrowed/search/due-date`

**Query Parameters**: Same as borrow date search

**Example**: `GET /api/borrowed/search/due-date?startDate=2025-12-01&endDate=2025-12-15`

**Response**: `200 OK` - List or Page of records with due dates in range

**Use Case**: Find books due soon for reminder notifications

##### Search by Return Date Range
**Endpoint**: `GET /api/borrowed/search/return-date`

**Query Parameters**: Same as borrow date search

**Example**: `GET /api/borrowed/search/return-date?startDate=2025-11-01&endDate=2025-11-30`

**Response**: `200 OK` - List or Page of returned records within date range

**Use Case**: Monthly return statistics and reports

#### Utility Endpoints

##### Check if Book Copy is Available
**Endpoint**: `GET /api/borrowed/book-copy/{bookCopyId}/available`

**Example**: `GET /api/borrowed/book-copy/5/available`

**Response**: `200 OK`
```json
{
  "available": true
}
```

##### Check if User Exceeded Borrow Limit
**Endpoint**: `GET /api/borrowed/user/{userId}/limit-check`

**Query Parameters**:
- `limit` (default: 5): Maximum allowed active borrows

**Example**: `GET /api/borrowed/user/1/limit-check?limit=3`

**Response**: `200 OK`
```json
{
  "limitExceeded": false
}
```

**Use Case**: Validate before allowing new borrows

##### Count Active Borrows by User
**Endpoint**: `GET /api/borrowed/user/{userId}/active-count`

**Example**: `GET /api/borrowed/user/1/active-count`

**Response**: `200 OK`
```json
{
  "activeCount": 2
}
```

#### Common Use Cases

##### Complete Checkout Workflow
1. Check if book copy is available: `GET /api/borrowed/book-copy/{bookCopyId}/available`
2. Check user's borrow limit: `GET /api/borrowed/user/{userId}/limit-check?limit=5`
3. Create borrow record: `POST /api/borrowed`

##### Generate Overdue Report
1. Get all overdue records: `GET /api/borrowed/overdue?page=0&size=100`
2. Or filter by user: `GET /api/borrowed/user/{userId}/overdue`

##### Monthly Statistics
1. Borrows in month: `GET /api/borrowed/search/borrow-date?startDate=2025-11-01&endDate=2025-11-30`
2. Returns in month: `GET /api/borrowed/search/return-date?startDate=2025-11-01&endDate=2025-11-30`

##### User Dashboard
1. Active borrows: `GET /api/borrowed/user/{userId}/active`
2. Overdue items: `GET /api/borrowed/user/{userId}/overdue`
3. Borrow history: `GET /api/borrowed/user/{userId}?page=0&size=10`

---

### Fine Management API

The Fine Management API handles all aspects of fines including automated assessment for overdue books, payment processing, and fine tracking.

#### Core CRUD Operations

##### 1. Create a Fine
**Endpoint**: `POST /api/fines`

**Request Body**:
```json
{
  "amount": 5.00,
  "assessedDate": "2025-11-27",
  "status": "PENDING",
  "reason": "Book returned 5 days late",
  "borrowedId": 1
}
```

**Notes**:
- `status` is optional and defaults to `PENDING` if not provided
- `reason` is optional but recommended for clarity

**Response**: `201 CREATED`
```json
{
  "id": 1,
  "amount": 5.00,
  "assessedDate": "2025-11-27",
  "status": "PENDING",
  "reason": "Book returned 5 days late",
  "borrowed": {
    "id": 1,
    "borrowDate": "2025-11-01",
    "dueDate": "2025-11-15",
    "returnDate": "2025-11-20",
    "status": "RETURNED",
    "user": { ... },
    "bookCopy": { ... }
  }
}
```

##### 2. Get Fine by ID
**Endpoint**: `GET /api/fines/{id}`

**Example**: `GET /api/fines/1`

**Response**: `200 OK` - Returns FineResponseDTO

##### 3. Get All Fines
**Endpoint**: `GET /api/fines`

**Query Parameters**:
- `page` (optional): Page number (0-indexed)
- `size` (optional): Number of items per page
- `sortBy` (default: `assessedDate`): Field to sort by
- `sortDirection` (default: `DESC`): `ASC` or `DESC`

**Example**: `GET /api/fines?page=0&size=20&sortBy=amount&sortDirection=DESC`

**Response**: `200 OK` - List or Page of FineResponseDTOs

##### 4. Update Fine
**Endpoint**: `PUT /api/fines/{id}`

**Request Body**: Same as create

**Response**: `200 OK` - Returns updated FineResponseDTO

##### 5. Delete Fine
**Endpoint**: `DELETE /api/fines/{id}`

**Response**: `204 NO CONTENT`

#### Search and Filter Endpoints

##### Get Fine by Borrowed Record
**Endpoint**: `GET /api/fines/borrowed/{borrowedId}`

**Example**: `GET /api/fines/borrowed/5`

**Response**: `200 OK` - Returns the fine associated with the borrowed record

##### Get Fines by Status
**Endpoint**: `GET /api/fines/status/{status}`

**Status Values**: `PENDING`, `PAID`, `WAIVED`

**Example**: `GET /api/fines/status/PENDING?page=0&size=50`

**Response**: `200 OK` - List or Page of fines with specified status

##### Get Fines by User
**Endpoint**: `GET /api/fines/user/{userId}`

**Example**: `GET /api/fines/user/1?page=0&size=10`

**Response**: `200 OK` - List or Page of all fines for the user

##### Get Pending Fines by User
**Endpoint**: `GET /api/fines/user/{userId}/pending`

**Example**: `GET /api/fines/user/1/pending`

**Response**: `200 OK` - List or Page of unpaid fines for the user

**Use Case**: Show user what they owe before allowing new borrows

##### Search by Assessed Date Range
**Endpoint**: `GET /api/fines/search/assessed-date`

**Query Parameters**:
- `startDate` (required): Start date (format: `yyyy-MM-dd`)
- `endDate` (required): End date (format: `yyyy-MM-dd`)
- `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/fines/search/assessed-date?startDate=2025-11-01&endDate=2025-11-30`

**Response**: `200 OK` - List or Page of fines assessed within date range

**Use Case**: Monthly fine reports and revenue tracking

##### Search by Amount
**Endpoint**: `GET /api/fines/search/amount`

**Query Parameters**:
- `amount` (required): Minimum amount
- `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/fines/search/amount?amount=10.00`

**Response**: `200 OK` - List or Page of fines greater than specified amount

**Use Case**: Identify high-value fines requiring attention

#### Payment Operations

##### Pay a Fine
**Endpoint**: `POST /api/fines/{id}/pay`

**Example**: `POST /api/fines/1/pay`

**Response**: `200 OK`
```json
{
  "id": 1,
  "amount": 5.00,
  "assessedDate": "2025-11-27",
  "status": "PAID",
  "reason": "Book returned 5 days late",
  "borrowed": { ... }
}
```

**Notes**:
- Only `PENDING` fines can be paid
- Status automatically updated to `PAID`
- Throws exception if fine is already paid or waived

##### Waive a Fine
**Endpoint**: `POST /api/fines/{id}/waive`

**Example**: `POST /api/fines/3/waive`

**Response**: `200 OK`
```json
{
  "id": 3,
  "amount": 2.50,
  "assessedDate": "2025-11-20",
  "status": "WAIVED",
  "reason": "First time offender - waived by librarian",
  "borrowed": { ... }
}
```

**Notes**:
- Only `PENDING` fines can be waived
- Typically used by librarians/admins for special cases
- Status automatically updated to `WAIVED`

#### Automated Fine Assessment

##### Assess Fine for Overdue Book
**Endpoint**: `POST /api/fines/assess/borrowed/{borrowedId}`

**Query Parameters**:
- `dailyRate` (default: 1.00): Fine amount per day overdue

**Example**: `POST /api/fines/assess/borrowed/5?dailyRate=1.50`

**Response**: `201 CREATED`
```json
{
  "id": 10,
  "amount": 7.50,
  "assessedDate": "2025-11-27",
  "status": "PENDING",
  "reason": "Book returned 5 day(s) late at 1.50 per day",
  "borrowed": {
    "id": 5,
    "dueDate": "2025-11-15",
    "returnDate": "2025-11-20",
    ...
  }
}
```

**Business Logic**:
- Automatically calculates days overdue: `returnDate - dueDate`
- Multiplies by daily rate to get total fine
- Generates descriptive reason
- Prevents duplicate fines for same borrowed record
- Validates that book is actually overdue

**Use Case**: Automatically assess fines when processing book returns

#### Financial Calculations

##### Get Total Pending Fines by User
**Endpoint**: `GET /api/fines/user/{userId}/total-pending`

**Example**: `GET /api/fines/user/1/total-pending`

**Response**: `200 OK`
```json
{
  "totalPending": 15.50
}
```

**Use Case**: Check if user can borrow more books (enforce payment before new borrows)

##### Get Total Fines by Status
**Endpoint**: `GET /api/fines/total/status/{status}`

**Example**: `GET /api/fines/total/status/PAID`

**Response**: `200 OK`
```json
{
  "total": 250.00
}
```

**Use Case**: Calculate total revenue from paid fines

##### Count Fines by Status
**Endpoint**: `GET /api/fines/count/status/{status}`

**Example**: `GET /api/fines/count/status/PENDING`

**Response**: `200 OK`
```json
{
  "count": 42
}
```

**Use Case**: Dashboard statistics and reporting

#### Validation Endpoints

##### Check if Fine Exists for Borrowed Record
**Endpoint**: `GET /api/fines/exists/borrowed/{borrowedId}`

**Example**: `GET /api/fines/exists/borrowed/5`

**Response**: `200 OK`
```json
{
  "exists": true
}
```

**Use Case**: Prevent duplicate fine creation

##### Check if User Has Pending Fines
**Endpoint**: `GET /api/fines/user/{userId}/has-pending`

**Example**: `GET /api/fines/user/1/has-pending`

**Response**: `200 OK`
```json
{
  "hasPendingFines": true
}
```

**Use Case**: Block borrowing if user has unpaid fines

#### Common Use Cases

##### Complete Return with Fine Assessment
1. Process return: `POST /api/borrowed/{id}/return`
2. Check if overdue and assess fine: `POST /api/fines/assess/borrowed/{borrowedId}?dailyRate=1.00`
3. Notify user of fine amount

##### User Payment Workflow
1. Check pending fines: `GET /api/fines/user/{userId}/pending`
2. Get total owed: `GET /api/fines/user/{userId}/total-pending`
3. Process payment: `POST /api/fines/{id}/pay`
4. Verify payment: `GET /api/fines/user/{userId}/has-pending`

##### Librarian Fine Management
1. View all pending fines: `GET /api/fines/status/PENDING?page=0&size=100`
2. Review high-value fines: `GET /api/fines/search/amount?amount=10.00`
3. Waive special cases: `POST /api/fines/{id}/waive`

##### Monthly Financial Report
1. Fines assessed: `GET /api/fines/search/assessed-date?startDate=2025-11-01&endDate=2025-11-30`
2. Revenue collected: `GET /api/fines/total/status/PAID`
3. Outstanding fines: `GET /api/fines/total/status/PENDING`
4. Count statistics: `GET /api/fines/count/status/{status}`

##### Borrowing Validation
1. Check user eligibility: `GET /api/fines/user/{userId}/has-pending`
2. If has pending fines, get total: `GET /api/fines/user/{userId}/total-pending`
3. Block borrowing until fines are paid or under threshold

---

### Payment Management API

The Payment Management API handles payment processing for fines with support for multiple payment methods, transaction tracking, and comprehensive payment history.

#### Core CRUD Operations

##### 1. Create a Payment
**Endpoint**: `POST /api/payments`

**Request Body**:
```json
{
  "amount": 5.00,
  "paymentMethod": "CARD",
  "transactionId": "TXN-123456789",
  "fineId": 1
}
```

**Notes**:
- `transactionId` is optional for external payment systems
- Payment date is automatically set to current date
- Payment status defaults to `PENDING`

**Response**: `201 CREATED`
```json
{
  "id": 1,
  "amount": 5.00,
  "paymentDate": "2025-11-27",
  "paymentMethod": "CARD",
  "transactionId": "TXN-123456789",
  "status": "PENDING",
  "fine": {
    "id": 1,
    "amount": 5.00,
    "assessedDate": "2025-11-27",
    "status": "PENDING",
    "reason": "Book returned 5 days late",
    "borrowed": { ... }
  }
}
```

##### 2. Get Payment by ID
**Endpoint**: `GET /api/payments/{id}`

**Example**: `GET /api/payments/1`

**Response**: `200 OK` - Returns PaymentResponseDTO

##### 3. Get All Payments
**Endpoint**: `GET /api/payments`

**Query Parameters**:
- `page` (optional): Page number (0-indexed)
- `size` (optional): Number of items per page
- `sortBy` (default: `paymentDate`): Field to sort by
- `sortDirection` (default: `DESC`): `ASC` or `DESC`

**Example**: `GET /api/payments?page=0&size=20&sortBy=amount&sortDirection=DESC`

**Response**: `200 OK` - List or Page of PaymentResponseDTOs

##### 4. Update Payment
**Endpoint**: `PUT /api/payments/{id}`

**Request Body**: Same as create

**Response**: `200 OK` - Returns updated PaymentResponseDTO

##### 5. Delete Payment
**Endpoint**: `DELETE /api/payments/{id}`

**Response**: `204 NO CONTENT`

#### Search and Filter Endpoints

##### Get Payment by Transaction ID
**Endpoint**: `GET /api/payments/transaction/{transactionId}`

**Example**: `GET /api/payments/transaction/TXN-123456789`

**Response**: `200 OK` - Returns unique payment by transaction ID

##### Get Payments by Fine ID
**Endpoint**: `GET /api/payments/fine/{fineId}`

**Example**: `GET /api/payments/fine/1?page=0&size=10`

**Response**: `200 OK` - List or Page of all payments for the fine

**Use Case**: Track payment history for a specific fine (supports partial payments)

##### Get Payments by Status
**Endpoint**: `GET /api/payments/status/{status}`

**Status Values**: `COMPLETED`, `PENDING`, `FAILED`

**Example**: `GET /api/payments/status/COMPLETED?page=0&size=50`

**Response**: `200 OK` - List or Page of payments with specified status

##### Get Payments by Payment Method
**Endpoint**: `GET /api/payments/method/{method}`

**Method Values**: `CARD`, `CASH`, `ONLINE`

**Example**: `GET /api/payments/method/CARD?page=0&size=20`

**Response**: `200 OK` - List or Page of payments using specified method

**Use Case**: Analyze payment method preferences and reconciliation

##### Get Payments by User
**Endpoint**: `GET /api/payments/user/{userId}`

**Example**: `GET /api/payments/user/1?page=0&size=10`

**Response**: `200 OK` - List or Page of all payments made by the user

##### Search by Payment Date Range
**Endpoint**: `GET /api/payments/search/payment-date`

**Query Parameters**:
- `startDate` (required): Start date (format: `yyyy-MM-dd`)
- `endDate` (required): End date (format: `yyyy-MM-dd`)
- `page`, `size`, `sortBy`, `sortDirection` (optional)

**Example**: `GET /api/payments/search/payment-date?startDate=2025-11-01&endDate=2025-11-30`

**Response**: `200 OK` - List or Page of payments within date range

**Use Case**: Monthly payment reports and reconciliation

##### Get Completed Payments by Method
**Endpoint**: `GET /api/payments/completed/method/{method}`

**Example**: `GET /api/payments/completed/method/CARD?page=0&size=20`

**Response**: `200 OK` - List or Page of successfully completed payments by method

**Use Case**: Revenue analysis by payment channel

#### Payment Processing Operations

##### Process a Payment
**Endpoint**: `POST /api/payments/{id}/process`

**Example**: `POST /api/payments/1/process`

**Response**: `200 OK`
```json
{
  "id": 1,
  "amount": 5.00,
  "paymentDate": "2025-11-27",
  "paymentMethod": "CARD",
  "status": "PENDING",
  ...
}
```

**Notes**:
- Only `PENDING` payments can be processed
- Sets payment date if not already set
- Prepares payment for completion

##### Complete a Payment
**Endpoint**: `POST /api/payments/{id}/complete`

**Query Parameters**:
- `transactionId` (optional): External transaction identifier

**Example**: `POST /api/payments/1/complete?transactionId=TXN-987654321`

**Response**: `200 OK`
```json
{
  "id": 1,
  "amount": 5.00,
  "paymentDate": "2025-11-27",
  "paymentMethod": "CARD",
  "transactionId": "TXN-987654321",
  "status": "COMPLETED",
  ...
}
```

**Notes**:
- Validates payment is not already completed
- Updates payment date to current date
- Validates transaction ID uniqueness
- Marks payment as successfully completed

##### Fail a Payment
**Endpoint**: `POST /api/payments/{id}/fail`

**Query Parameters**:
- `reason` (optional): Reason for payment failure

**Example**: `POST /api/payments/1/fail?reason=Insufficient%20funds`

**Response**: `200 OK`
```json
{
  "id": 1,
  "amount": 5.00,
  "paymentDate": "2025-11-27",
  "paymentMethod": "CARD",
  "status": "FAILED",
  ...
}
```

**Notes**:
- Cannot fail already completed payments
- Used when payment processing encounters errors

##### Refund a Payment
**Endpoint**: `POST /api/payments/{id}/refund`

**Example**: `POST /api/payments/3/refund`

**Response**: `201 CREATED`
```json
{
  "id": 10,
  "amount": -5.00,
  "paymentDate": "2025-11-28",
  "paymentMethod": "CARD",
  "transactionId": "REFUND-3-1732752000000",
  "status": "COMPLETED",
  "fine": { ... }
}
```

**Notes**:
- Only `COMPLETED` payments can be refunded
- Creates new payment record with negative amount
- Auto-generates refund transaction ID: `REFUND-{originalId}-{timestamp}`
- Maintains payment audit trail

#### Financial Calculations

##### Get Total Paid Amount by Fine ID
**Endpoint**: `GET /api/payments/fine/{fineId}/total-paid`

**Example**: `GET /api/payments/fine/1/total-paid`

**Response**: `200 OK`
```json
{
  "totalPaid": 5.00
}
```

**Use Case**: Calculate how much has been paid towards a fine (supports partial payments)

##### Get Total Paid Amount by User ID
**Endpoint**: `GET /api/payments/user/{userId}/total-paid`

**Example**: `GET /api/payments/user/1/total-paid`

**Response**: `200 OK`
```json
{
  "totalPaid": 25.50
}
```

**Use Case**: Track user's total payment history

##### Get Total Revenue by Date Range
**Endpoint**: `GET /api/payments/revenue`

**Query Parameters**:
- `startDate` (required): Start date (format: `yyyy-MM-dd`)
- `endDate` (required): End date (format: `yyyy-MM-dd`)

**Example**: `GET /api/payments/revenue?startDate=2025-11-01&endDate=2025-11-30`

**Response**: `200 OK`
```json
{
  "totalRevenue": 1250.00
}
```

**Use Case**: Monthly/quarterly revenue reporting

#### Validation Endpoints

##### Check if Transaction ID Exists
**Endpoint**: `GET /api/payments/exists/transaction/{transactionId}`

**Example**: `GET /api/payments/exists/transaction/TXN-123456789`

**Response**: `200 OK`
```json
{
  "exists": true
}
```

**Use Case**: Prevent duplicate transaction processing

#### Common Use Cases

##### Complete Payment Workflow
1. Create payment: `POST /api/payments`
2. Process payment: `POST /api/payments/{id}/process`
3. External payment processing...
4. Complete payment: `POST /api/payments/{id}/complete?transactionId=XXX`
5. Verify completion: `GET /api/payments/{id}`

##### Handle Payment Failure
1. Attempt payment processing
2. If fails: `POST /api/payments/{id}/fail?reason=...`
3. Notify user of failure
4. Allow retry with new payment

##### Partial Payment Scenario
1. Create first payment: `POST /api/payments` (amount: 10.00 for 25.00 fine)
2. Complete payment: `POST /api/payments/{id}/complete`
3. Create second payment: `POST /api/payments` (amount: 15.00 for same fine)
4. Check total paid: `GET /api/payments/fine/{fineId}/total-paid`
5. Complete when total equals fine amount

##### Process Refund
1. Verify payment completed: `GET /api/payments/{id}`
2. Issue refund: `POST /api/payments/{id}/refund`
3. Refund payment record created automatically
4. Check updated balance: `GET /api/payments/fine/{fineId}/total-paid`

##### Monthly Financial Report
1. Get all payments in month: `GET /api/payments/search/payment-date?startDate=...&endDate=...`
2. Calculate revenue: `GET /api/payments/revenue?startDate=...&endDate=...`
3. Break down by method: `GET /api/payments/completed/method/{method}`
4. Analyze by status: `GET /api/payments/status/COMPLETED`

##### User Payment History
1. Get all payments: `GET /api/payments/user/{userId}?page=0&size=20`
2. Get total paid: `GET /api/payments/user/{userId}/total-paid`
3. Filter by date: `GET /api/payments/search/payment-date?startDate=...&endDate=...`

##### Payment Reconciliation
1. Get completed payments: `GET /api/payments/status/COMPLETED?page=0&size=100`
2. Verify transaction IDs exist
3. Calculate total revenue: `GET /api/payments/revenue?startDate=...&endDate=...`
4. Compare with external payment gateway records

---

### Error Responses

All endpoints follow standard HTTP status codes and return consistent error responses:

**404 NOT FOUND**:
```json
{
  "timestamp": "2025-11-26T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 999",
  "path": "/api/books/999"
}
```

**400 BAD REQUEST** (Validation Error):
```json
{
  "timestamp": "2025-11-26T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "isbn": "ISBN is mandatory",
    "title": "Title is mandatory"
  },
  "path": "/api/books"
}
```

**409 CONFLICT** (Business Rule Violation):
```json
{
  "timestamp": "2025-11-26T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Book with ISBN 978-0-13-468599-1 already exists",
  "path": "/api/books"
}
```

## 📐 Architecture & Design Patterns

### Layered Architecture

The application follows a clean layered architecture:

1. **Controller Layer** (`@RestController`)
   - Handles HTTP requests and responses
   - Input validation using `@Valid`
   - Maps between DTOs and service calls
   - Pagination and sorting support

2. **Service Layer** (`@Service`)
   - Contains business logic
   - Transaction management using `@Transactional`
   - Coordinates between repositories and mappers
   - Validates business rules (e.g., ISBN uniqueness)

3. **Repository Layer** (`@Repository`)
   - Data access using Spring Data JPA
   - Custom queries with JPQL
   - Derived query methods

4. **Entity Layer** (`@Entity`)
   - JPA entities representing database tables
   - Relationships and constraints

### DTO Pattern

The application uses Data Transfer Objects (DTOs) to separate the API layer from the persistence layer:

#### Request DTOs
- Used for creating and updating resources
- Include validation annotations
- Example: `BookRequestDTO`, `AuthorRequestDTO`

#### Response DTOs
- Used for returning data to clients
- Include nested objects for related entities
- Example: `BookResponseDTO` includes nested `PublisherResponseDTO`, `AuthorResponseDTO`, `GenreResponseDTO`

### Mapper Pattern

Mappers handle conversion between DTOs and entities:

```java
@Component
public class BookMapper {
    // Converts Book entity to BookResponseDTO
    public BookResponseDTO toResponseDTO(Book book);
    
    // Converts BookRequestDTO to Book entity
    public Book toEntity(BookRequestDTO requestDTO);
    
    // Updates existing Book entity from BookRequestDTO
    public void updateEntityFromRequest(Book book, BookRequestDTO requestDTO);
}
```

### Service Layer Features

#### Transaction Management
```java
@Service
@Transactional
public class BookServiceImpl implements BookService {
    // Write operations run in transactions
    public Book create(Book book) { ... }
    
    // Read operations use read-only transactions
    @Transactional(readOnly = true)
    public Book getById(Long id) { ... }
}
```

#### Business Logic Validation
- ISBN uniqueness validation
- Book status management
- Fine calculation
- Overdue detection

### Exception Handling

Global exception handler (`@ControllerAdvice`) provides consistent error responses:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(...);
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(...);
}
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

For support and questions, please contact:
- **Email**: manutdfast91@gmail.com
- **Repository**: Create an issue in the GitHub repository

---

**Library Management System** - Built with ❤️ using Spring Boot
