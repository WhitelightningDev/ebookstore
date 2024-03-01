package ebookstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// Class representing the main program for managing books in an ebookstore database
public class Main {

    public static void main(String[] args) {
        try {
            // Connect to the ebookstore database via the jdbc:mysql channel on localhost (this PC)
            // Use username "root", password "Grayham@95".
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", "root", "Grayham@95");

            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();

            Scanner scanner = new Scanner(System.in);

            int choice;
            do {
                // Display the menu options
                System.out.println("1. Enter Book");
                System.out.println("2. Update Book");
                System.out.println("3. Delete Book");
                System.out.println("4. Search Books");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();  // clear the newline character

                // Perform actions based on user choice
                switch (choice) {
                    case 1:
                        enterBook(connection, scanner);
                        break;

                    case 2:
                        updateBook(connection, scanner);
                        break;

                    case 3:
                        deleteBook(connection, statement, scanner);
                        break;

                    case 4:
                        searchBooks(statement, scanner);
                        break;

                    case 0:
                        System.out.println("Exiting the program");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);

            // Close resources
            statement.close();
            connection.close();
            scanner.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to enter a new book into the database
    private static void enterBook(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Option 1 selected - Enter Book");

        // Collect information for the new book
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        // SQL code to insert a new book into the 'books' table
        String insertQuery = "INSERT INTO books (title, author, qty) VALUES (?, ?, ?)";

        // Use a PreparedStatement to prevent SQL injection
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, quantity);

            // Execute the query and get the number of rows affected
            int rowsAffected = preparedStatement.executeUpdate();

            // Print the result
            System.out.println("Query complete, " + rowsAffected + " rows added.");
        }
    }

    private static void updateBook(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Option 2 selected - Update Book");

        // Create a statement to use for displaying the current and updated books
        try (Statement statement = connection.createStatement()) {
            // Display the current books in the database
            displayAllBooks(statement);

            // Collect information for the book to update
            System.out.print("Enter book ID to update: ");
            int bookIdToUpdate = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            // Display menu for update options
            System.out.println("Select update option:");
            System.out.println("1. Update all details");
            System.out.println("2. Update a single field");
            System.out.print("Enter your choice: ");
            int updateOption = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (updateOption) {
                case 1:
                    // Update all details
                    updateAllDetails(connection, bookIdToUpdate, scanner);
                    break;
                case 2:
                    // Update a single field
                    updateSingleField(connection, bookIdToUpdate, scanner);
                    break;
                default:
                    System.out.println("Invalid option. No updates performed.");
            }

            // Display the updated books in the database
            displayAllBooks(statement);
        }
    }

    // Helper methods

    private static void updateAllDetails(Connection connection, int bookId, Scanner scanner) throws SQLException {
        System.out.print("Enter new quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        System.out.print("Enter new title: ");
        String newTitle = scanner.nextLine();

        System.out.print("Enter new author: ");
        String newauthor = scanner.nextLine();

        String updateQuery = "UPDATE books SET qty = ?, title = ?, author = ? WHERE id = ?";
        updateBook(connection, updateQuery, newQuantity, newTitle, newauthor, bookId);
    }

    private static void updateSingleField(Connection connection, int bookId, Scanner scanner) throws SQLException {
        System.out.println("Select field to update:");
        System.out.println("1. Quantity");
        System.out.println("2. Title");
        System.out.println("3. author");
        System.out.print("Enter your choice: ");
        int fieldChoice = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        switch (fieldChoice) {
            case 1:
                // Update quantity
                System.out.print("Enter new quantity: ");
                int newQuantity = scanner.nextInt();
                updateBook(connection, "UPDATE books SET qty = ? WHERE id = ?", newQuantity, bookId);
                break;
            case 2:
                // Update title
                System.out.print("Enter new title: ");
                String newTitle = scanner.nextLine();
                updateBook(connection, "UPDATE books SET title = ? WHERE id = ?", newTitle, bookId);
                break;
            case 3:
                // Update author
                System.out.print("Enter new author: ");
                String newauthor = scanner.nextLine();
                updateBook(connection, "UPDATE books SET author = ? WHERE id = ?", newauthor, bookId);
                break;
            default:
                System.out.println("Invalid option. No updates performed.");
        }
    }

    private static void updateBook(Connection connection, String updateQuery, Object... values) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }

            // Execute the query and get the number of rows affected
            int rowsAffected = preparedStatement.executeUpdate();

            // Print the result
            System.out.println("Query complete, " + rowsAffected + " rows updated.");
        }
    }

    // Method to delete a book from the database
    private static void deleteBook(Connection connection, Statement statement, Scanner scanner) throws SQLException {
        System.out.println("Option 3 selected - Delete Book");

        // Display the current books in the database
        displayAllBooks(statement);

        // Collect information for the book to delete
        System.out.print("Enter book ID to delete: ");
        int bookIdToDelete = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        // SQL code to delete a book from the 'books' table based on the ID
        String deleteQuery = "DELETE FROM books WHERE id = ?";

        // Use a PreparedStatement to prevent SQL injection
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, bookIdToDelete);

            // Execute the query and get the number of rows affected
            int rowsAffected = preparedStatement.executeUpdate();

            // Print the result
            System.out.println("Query complete, " + rowsAffected + " rows deleted.");

            // Display the updated books in the database
            displayAllBooks(statement);
        }
    }

    // Method to search for books based on a keyword in the title or author
    private static void searchBooks(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("Option 4 selected - Search Books");

        // Collect the keyword for the search
        System.out.print("Enter a keyword to search for: ");
        String keyword = scanner.nextLine();

        // SQL code to search for books containing the keyword in the title or author
        String searchQuery = "SELECT id, title, author, qty FROM books WHERE title LIKE ? OR author LIKE ?";

        // Use a PreparedStatement to prevent SQL injection
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(searchQuery)) {
            preparedStatement.setString(1, "%" + keyword + "%");
            preparedStatement.setString(2, "%" + keyword + "%");

            // Execute the query and get the results
            ResultSet results = preparedStatement.executeQuery();

            // Display the search results
            while (results.next()) {
                System.out.println(results.getInt("id") + ", " +
                        results.getString("title") + ", " +
                        results.getString("author") + ", " +
                        results.getInt("qty"));
            }
        }
    }

    // Method to display all books in the database
    private static void displayAllBooks(Statement statement) throws SQLException {
        // executeQuery: runs a SELECT statement and returns the results
        ResultSet results = statement.executeQuery("SELECT id, title, author, qty FROM books");

        // Loop over the results, printing them all
        System.out.println("Current books in the database:");
        while (results.next()) {
            System.out.println(results.getInt("id") + ", " +
                    results.getString("title") + ", " +
                    results.getString("author") + ", " +
                    results.getInt("qty"));
        }
    }
}
