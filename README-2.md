# Budget Tracker Application
First open CMD, and use:
- mysql -u root -p
the password needs to be "root"

then:
-create database budget_tracker;
then:
-use budget_tracker;
then (create tables in budget_tracker):
CREATE TABLE users (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);
CREATE TABLE categories (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    monthly_budget DOUBLE NOT NULL,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    KEY (user_id),
    CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE expense (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    date DATE DEFAULT NULL,
    name VARCHAR(255) DEFAULT NULL,
    sum DECIMAL(38,2) DEFAULT NULL,
    category_id BIGINT(20) DEFAULT NULL,
    user_id BIGINT(20) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY (category_id),
    KEY (user_id),
    CONSTRAINT fk_expense_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_expense_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE uploaded_file (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    filename VARCHAR(255) DEFAULT NULL,
    filepath VARCHAR(255) DEFAULT NULL,
    upload_date DATETIME(6) DEFAULT NULL,
    user_id BIGINT(20) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY (user_id),
    CONSTRAINT fk_uploaded_file_user FOREIGN KEY (user_id) REFERENCES users(id)
);


IGNORE ANY STUFF FROM DOWN BELOW


A web application for tracking personal and business budgets, built with Spring Boot and modern web technologies.

## Features

- User registration and authentication with Spring Security
- User roles (Client, Admin)
- Profile management
- Budget tracking and management
- File upload capabilities
- Responsive design using Bootstrap
- Docker deployment support

## Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose
- MySQL (if running locally)

## Getting Started

### Running with Docker

1. Build the application:
```bash
mvn clean package
```

2. Start the application using Docker Compose:
```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`

### Running Locally

1. Start MySQL database
2. Update `application.properties` with your database credentials
3. Run the application:
```bash
mvn spring-boot:run
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── budgettracker/
│   │           ├── config/        # Configuration classes
│   │           ├── controller/    # REST controllers
│   │           ├── model/         # Entity classes
│   │           ├── repository/    # Data repositories
│   │           ├── service/       # Business logic
│   │           └── security/      # Security configuration
│   └── resources/
│       ├── static/               # Static resources
│       ├── templates/            # Thymeleaf templates
│       └── application.properties # Application configuration
└── test/                        # Test classes
```

## Testing

Run the tests using:
```bash
mvn test
```

## Security

- Passwords are encrypted using BCrypt
- JWT-based authentication
- Role-based access control

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request 

# Database name
budget_tracker

# Table USERS
CREATE TABLE users (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,      -- unique user id, primary key
    email VARCHAR(255) NOT NULL,                -- user email, required
    password VARCHAR(255) NOT NULL,             -- user password, required
    role VARCHAR(255) NOT NULL,                 -- user role, required
    username VARCHAR(255) NOT NULL,             -- username, required, must be unique
    PRIMARY KEY (id),                           -- set id as primary key
    UNIQUE (username)                           -- enforce unique usernames
);

# Table EXPENSES
CREATE TABLE expenses (
    id_expense INT AUTO_INCREMENT PRIMARY KEY,      -- unique expense id
    id_user BIGINT(20) NOT NULL,                    -- user id (foreign key, matches users.id)
    name VARCHAR(255) NOT NULL,                     -- expense name
    sum DECIMAL(10,2) NOT NULL,                     -- amount
    date DATE NOT NULL,                             -- expense date
    category VARCHAR(100) NOT NULL,                 -- category
    proof_of_payment BLOB,                          -- photo/pdf as binary (optional)
    FOREIGN KEY (id_user) REFERENCES users(id)      -- link to users table
);

# Table CATEGORIES
-- Table for storing expense categories
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Unique ID for each category
    name VARCHAR(255) NOT NULL UNIQUE,    -- Category name (e.g., Transport)
    monthly_budget DOUBLE NOT NULL        -- Monthly budget for the category
);