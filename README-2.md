# Budget Tracker Application

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