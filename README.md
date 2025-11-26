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
  - [Book Management API](#book-management-api)
  - [Book Copy Management API](#book-copy-management-api)
  - [Borrowed (Borrowing System) API](#borrowed-borrowing-system-api)
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
- **Fine Management**: Automated fine calculation and tracking for overdue books
- **Payment Processing**: Support for multiple payment methods and transaction tracking
- **Security**: Password encryption using BCrypt
- **Database Integration**: PostgreSQL database with JPA/Hibernate
- **DTO Pattern**: Clean separation between entity and presentation layers
- **Mapper Layer**: Automatic mapping between DTOs and entities

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
│   │       ├── controller/       # REST API Controllers
│   │       │   ├── AuthorController.java
│   │       │   ├── BookController.java
│   │       │   ├── BookCopyController.java
│   │       │   ├── BorrowedController.java
│   │       │   ├── GenreController.java
│   │       │   └── PublisherController.java
│   │       ├── dto/              # Data Transfer Objects
│   │       │   ├── AuthorRequestDTO.java
│   │       │   ├── AuthorResponseDTO.java
│   │       │   ├── BookRequestDTO.java
│   │       │   ├── BookResponseDTO.java
│   │       │   ├── BookCopyRequestDTO.java
│   │       │   ├── BookCopyResponseDTO.java
│   │       │   ├── BorrowedRequestDTO.java
│   │       │   ├── BorrowedResponseDTO.java
│   │       │   ├── GenreRequestDTO.java
│   │       │   ├── GenreResponseDTO.java
│   │       │   ├── PublisherRequestDTO.java
│   │       │   ├── PublisherResponseDTO.java
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
│   │       │   ├── GenreMapper.java
│   │       │   └── PublisherMapper.java
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
│   │       │   │   ├── GenreService.java
│   │       │   │   └── PublisherService.java
│   │       │   ├── AuthorServiceImpl.java
│   │       │   ├── BookServiceImpl.java
│   │       │   ├── BookCopyImpl.java
│   │       │   ├── BorrowedImpl.java
│   │       │   ├── GenreServiceImpl.java
│   │       │   └── PublisherServiceImpl.java
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
