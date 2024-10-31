package tolobekov.library.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import tolobekov.library.model.Book;
import tolobekov.library.model.User;
import tolobekov.library.util.LibraryController;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
// Other necessary imports

public class LibrarianDashboard extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JTextField searchField;
    private JButton searchButton, addBookButton, removeBookButton, manageUsersButton;
    private DefaultTableModel usersTableModel;
    private JTable usersTable;
    LibraryController libraryController = new LibraryController();

    public LibrarianDashboard() {
        setLayout(new BorderLayout());
        usersTableModel = new DefaultTableModel(new String[] { "Username", "User Type" }, 0);
        usersTable = new JTable(usersTableModel);
        initUIComponents();
    }

    private void initUIComponents() {
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this::searchBooks);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3)); // 1 row, 3 columns
        addBookButton = new JButton("Add a Book");
        removeBookButton = new JButton("Remove a Book");
        manageUsersButton = new JButton("Manage Users");

        addBookButton.addActionListener(this::addBook);
        removeBookButton.addActionListener(this::removeBook);
        manageUsersButton.addActionListener(this::manageUsers);

        buttonPanel.add(addBookButton);
        buttonPanel.add(removeBookButton);
        buttonPanel.add(manageUsersButton);

        // Adding components to the layout
        add(searchPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void addBook(ActionEvent e) {
        // Create a new dialog for adding a book
        JDialog addBookDialog = new JDialog();
        addBookDialog.setTitle("Add New Book");
        addBookDialog.setLayout(new GridLayout(0, 2)); // 0 rows, 2 columns, expandable rows
        addBookDialog.setSize(400, 200); // Set the size of the dialog

        // Create labels and text fields for book information
        addBookDialog.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        addBookDialog.add(titleField);

        addBookDialog.add(new JLabel("Author:"));
        JTextField authorField = new JTextField();
        addBookDialog.add(authorField);

        addBookDialog.add(new JLabel("ISBN:"));
        JTextField isbnField = new JTextField();
        addBookDialog.add(isbnField);

        // Create an "Add" button to submit the book details
        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(
                event -> submitBookDetails(titleField.getText(), authorField.getText(), isbnField.getText()));
        addBookDialog.add(addButton);

        // Display the dialog
        addBookDialog.setVisible(true);
    }

    private void submitBookDetails(String title, String author, String isbn) {
        // Validate the input
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for duplicate ISBN before adding the book
        try {
            List<Book> existingBooks = libraryController.getAllBooks();
            for (Book book : existingBooks) {
                if (book.getIsbn().equals(isbn)) {
                    JOptionPane.showMessageDialog(this, "A book with this ISBN already exists.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Add the book using LibraryController
            libraryController.addBook(title, author, isbn);
            JOptionPane.showMessageDialog(this, "Book added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add the book. Please try again.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeBook(ActionEvent e) {
        JDialog removeBookDialog = new JDialog();
        removeBookDialog.setTitle("Remove Book");
        removeBookDialog.setLayout(new BorderLayout());
        removeBookDialog.setSize(500, 300); // Set an appropriate size

        // Table model for the books
        DefaultTableModel model = new DefaultTableModel(new String[] { "Title", "Author", "ISBN" }, 0);
        JTable table = new JTable(model);

        // Populate the table with books that are not currently borrowed
        // Populate the table with books that are not currently borrowed
        List<Book> allBooks = libraryController.getAllBooks();
        for (Book book : allBooks) {
            if (!libraryController.isBookBorrowed(book)) {
                model.addRow(new Object[] { book.getTitle(), book.getAuthor(), book.getIsbn() });
            }
        }

        // Button to remove the selected book
        JButton removeButton = new JButton("Remove Selected Book");
        removeButton.addActionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String isbnToRemove = (String) model.getValueAt(selectedRow, 2);
                try {
                    libraryController.removeBook(isbnToRemove);
                    model.removeRow(selectedRow); // Remove the row from the table
                    JOptionPane.showMessageDialog(removeBookDialog, "Book removed successfully.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(removeBookDialog, "Failed to remove the book.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        removeBookDialog.add(new JScrollPane(table), BorderLayout.CENTER);
        removeBookDialog.add(removeButton, BorderLayout.SOUTH);

        removeBookDialog.setVisible(true);
    }

    private void searchBooks(ActionEvent e) {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title to search.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Book> foundBooks = libraryController.searchBooks(searchText);

            // Create a new table model for the search results
            DefaultTableModel searchResultsModel = new DefaultTableModel(new String[] { "Title", "Author", "ISBN" }, 0);

            // Populate the table model with the search results
            for (Book book : foundBooks) {
                searchResultsModel.addRow(new Object[] { book.getTitle(), book.getAuthor(), book.getIsbn() });
            }

            // Create a new JTable with the search results model
            JTable searchResultsTable = new JTable(searchResultsModel);

            // Create a new JDialog to display the search results
            JDialog searchResultsDialog = new JDialog();
            searchResultsDialog.setTitle("Search Results");
            searchResultsDialog.setLayout(new BorderLayout());
            searchResultsDialog.add(new JScrollPane(searchResultsTable), BorderLayout.CENTER);
            searchResultsDialog.setSize(500, 300); // Or pack for sizing based on contents
            searchResultsDialog.setLocationRelativeTo(this); // Center on current window
            searchResultsDialog.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while searching for books.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manageUsers(ActionEvent e) {
        JDialog manageUsersDialog = new JDialog();
        manageUsersDialog.setTitle("Manage Users");
        manageUsersDialog.setLayout(new BorderLayout());
        manageUsersDialog.setSize(400, 300);

        // Populate the users table with data
        loadUsersData();

        JScrollPane usersScrollPane = new JScrollPane(usersTable);
        manageUsersDialog.add(usersScrollPane, BorderLayout.CENTER);

        JButton removeUserButton = new JButton("Remove Selected User");
        removeUserButton.addActionListener(this::removeSelectedUser);
        manageUsersDialog.add(removeUserButton, BorderLayout.SOUTH);

        manageUsersDialog.setVisible(true);
    }

    private void loadUsersData() {
        // Clear existing data
        usersTableModel.setRowCount(0);

        try {

            List<User> users = libraryController.getAllUsers();
            for (User user : users) {
                usersTableModel.addRow(new Object[] { user.getUsername(), user.getUserType() });
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace(); // Log the exception or handle it as appropriate for your application
            JOptionPane.showMessageDialog(this, "Failed to load users.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSelectedUser(ActionEvent e) {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0) {
            String usernameToRemove = (String) usersTableModel.getValueAt(selectedRow, 0);
            String userType = (String) usersTableModel.getValueAt(selectedRow, 1);

            if ("Member".equals(userType)) {
                boolean success = libraryController.removeUser(usernameToRemove);
                if (success) {
                    usersTableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "User removed successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to remove user. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Only members can be removed.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user to remove.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Other helper methods and functionality as needed...
}
