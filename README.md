# Ebookstore Management System

Ebookstore Management System is a Java application that allows users to manage books in a database. Users can perform various operations like adding new books, updating book quantities, deleting books, and searching for books.

## Prerequisites

- Java Development Kit (JDK)
- MySQL Database Server
- JDBC Driver

## Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/your-username/ebookstore.git
    cd ebookstore
    ```

2. Set up the database:

    - Create a MySQL database named `ebookstore`.
    - Import the `ebookstore.sql` script to create the necessary tables.

3. Update database connection details:

    Open the `Main.java` file and update the following lines with your MySQL credentials:

    ```java
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", "root", "YourPassword");
    ```

4. Compile and run the application:

    ```bash
    javac Main.java
    java Main
    ```

## Features

1. **Enter Book:**
    - Add new books to the database with title, author, and quantity.

2. **Update Book:**
    - Update the quantity of existing books in the database.

3. **Delete Book:**
    - Delete a book from the database based on its ID.

4. **Search Books:**
    - Search for books containing a keyword in the title or author.

## Usage

Follow the on-screen menu to perform various operations. Enter the corresponding number for each action.

## Contributing

If you'd like to contribute to this project, please open an issue or create a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

