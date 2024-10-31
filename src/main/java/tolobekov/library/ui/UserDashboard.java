package tolobekov.library.ui;

import tolobekov.library.database.DataManager;
import tolobekov.library.model.Book;
import tolobekov.library.model.User;
import tolobekov.library.util.LibraryController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UserDashboard extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private User currentUser;
    private LibraryController libraryController;
    private JTextField searchField;
    private JButton searchButton, borrowButton, returnButton, viewBorrowedBooksButton;
    private JButton viewCatalogueButton;
    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    private boolean viewingBorrowedBooks = false;

    public UserDashboard(User user, LibraryController controller) {
        this.currentUser = user;
        this.libraryController = controller;

        setLayout(new BorderLayout());
        initUIComponents();
        loadAllBooks();
    }

    private void initUIComponents() {
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this::searchBooks);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Books Table
        booksTableModel = new DefaultTableModel(new String[] { "Title", "Author", "ISBN" }, 0);
        booksTable = new JTable(booksTableModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        borrowButton = new JButton("Borrow Selected Books");
        borrowButton.addActionListener(this::borrowBooks);
        returnButton = new JButton("Return Selected Books");
        returnButton.addActionListener(this::returnBooks);
        viewBorrowedBooksButton = new JButton("View Borrowed Books");
        viewBorrowedBooksButton.addActionListener(this::viewBorrowedBooks);
        viewCatalogueButton = new JButton("View Catalogue");
        viewCatalogueButton.addActionListener(e -> loadAllBooks());
        buttonPanel.add(viewCatalogueButton);
        borrowButton.setEnabled(true);
        returnButton.setEnabled(false);

        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(viewBorrowedBooksButton);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void searchBooks(ActionEvent e) {
        String searchText = searchField.getText().trim();
        List<Book> foundBooks = libraryController.searchBooks(searchText);

        // Update the books table with the search results
        booksTableModel.setRowCount(0); // Clear existing rows
        for (Book book : foundBooks) {
            booksTableModel.addRow(new Object[] { book.getTitle(), book.getAuthor(), book.getIsbn() });
        }
    }

    private void borrowBooks(ActionEvent e) {
        int[] selectedRows = booksTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one book to borrow.");
            return;
        }

        for (int rowIndex : selectedRows) {
            String title = (String) booksTableModel.getValueAt(rowIndex, 0);

            Book bookToBorrow = getBookByTitle(title);
            if (bookToBorrow != null) {
                String message = libraryController.borrowBook(currentUser, bookToBorrow);
                JOptionPane.showMessageDialog(this, message);
            }
        }
        // Optionally refresh the books table here to reflect the borrowed books
        loadAllBooks();

    }

    private void returnBooks(ActionEvent e) {
        int[] selectedRows = booksTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one book to return.");
            return;
        }

        for (int rowIndex : selectedRows) {
            String title = (String) booksTableModel.getValueAt(rowIndex, 0);

            Book bookToReturn = getBookByTitle(title);
            if (bookToReturn != null) {
                String message = libraryController.returnBook(currentUser, bookToReturn);
                JOptionPane.showMessageDialog(this, message);
            }
        }
        // refresh the books table here to reflect the returned books
        loadAllBooks(); // Or another method to refresh the view
    }

    private void viewBorrowedBooks(ActionEvent e) {

        if (!viewingBorrowedBooks) {
            loadBorrowedBooks();
            borrowButton.setEnabled(false);
            returnButton.setEnabled(true);
        } else {
            loadAllBooks();
        }
    }

    private void loadAllBooks() {

        try {
            List<Book> books = DataManager.loadBooks();
            booksTableModel.setRowCount(0); // Clear existing rows
            borrowButton.setEnabled(true);
            returnButton.setEnabled(false);
            for (Book book : books) {
                booksTableModel.addRow(new Object[] { book.getTitle(), book.getAuthor(), book.getIsbn() });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load books.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBorrowedBooks() {
        List<Book> borrowedBooks = libraryController.getBorrowedBooks(currentUser);
        booksTableModel.setRowCount(0); // Clear existing rows in the table

        for (Book book : borrowedBooks) {
            booksTableModel.addRow(new Object[] { book.getTitle(), book.getAuthor(), book.getIsbn() });
        }
    }

    private Book getBookByTitle(String title) {
        List<Book> allBooks = libraryController.getAllBooks();
        for (Book book : allBooks) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null; // Return null if no book with the given title is found
    }

    // Other helper methods as needed...
}
