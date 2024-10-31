package tolobekov.library.util;

import tolobekov.library.database.DataManager;
import tolobekov.library.model.Book;
import tolobekov.library.model.Transaction;
import tolobekov.library.model.User;
import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;

public class LibraryController {
    private List<Transaction> transactions;

    public LibraryController() {
        // Load existing transactions from file
        try {
            this.transactions = DataManager.loadTransactions();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            this.transactions = new ArrayList<>(); // Start with an empty list if loading fails
        }
    }

    public String borrowBook(User user, Book book) {
        if (isBookBorrowed(book)) {
            return "The book is already borrowed.";
        }

        Date issueDate = new Date(); // Current date as issue date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issueDate);
        calendar.add(Calendar.DAY_OF_MONTH, 30); // Add 30 days for the return date
        Date returnDate = calendar.getTime();

        Transaction newTransaction = new Transaction(user, book, issueDate, returnDate);
        transactions.add(newTransaction);
        if (!saveTransactions()) {
            return "Failed to save transactions.";
        }
        return "Book borrowed successfully: " + book.getTitle();
    }

    public String returnBook(User user, Book book) {
        Transaction transactionToReturn = findTransaction(user, book);
        if (transactionToReturn != null) {
            transactions.remove(transactionToReturn);
            saveTransactions();
            return "Book returned successfully: " + book.getTitle();
        } else {
            return "No borrowing record found for this book and user.";
        }
    }

    private boolean saveTransactions() {
        try {
            DataManager.saveTransactions(transactions);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isBookBorrowed(Book book) {
        Date currentDate = new Date();
        return transactions.stream()
                .anyMatch(transaction -> transaction.getBook().equals(book) &&
                        (transaction.getReturnDate() == null ||
                                currentDate.before(transaction.getReturnDate())));
    }

    private Transaction findTransaction(User user, Book book) {
        Date currentDate = new Date();
        return transactions.stream()
                .filter(transaction -> transaction.getUser().equals(user) &&
                        transaction.getBook().equals(book) &&
                        (transaction.getReturnDate() == null ||
                                currentDate.before(transaction.getReturnDate())))
                .max(Comparator.comparing(Transaction::getIssueDate)) // Find the latest transaction
                .orElse(null);
    }

    public List<Book> getBorrowedBooks(User user) {
        List<Book> borrowedBooks = new ArrayList<>();
        Date currentDate = new Date();
        for (Transaction transaction : transactions) {
            if (transaction.getUser().equals(user) && (transaction.getReturnDate() == null ||
                    currentDate.before(transaction.getReturnDate()))) {
                borrowedBooks.add(transaction.getBook());
            }
        }
        return borrowedBooks;
    }

    public List<Book> searchBooks(String title) {
        List<Book> allBooks = getAllBooks(); // Method to get all books
        if (title == null || title.isEmpty()) {
            return allBooks;
        }

        return allBooks.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        try {
            return DataManager.loadBooks();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            return new ArrayList<>(); // Return an empty list in case of failure
        }
    }

    public void addBook(String title, String author, String isbn) throws IOException {

        // Create a new Book object
        Book newBook = new Book(title, author, isbn, true);

        // Add the new book to the existing collection of books
        List<Book> books = getAllBooks();
        books.add(newBook);

        // Save the updated list of books
        DataManager.saveBooks(books);
    }

    public void removeBook(String isbn) throws IOException {
        List<Book> books = getAllBooks();

        // Use Iterator to avoid ConcurrentModificationException
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getIsbn().equals(isbn)) {
                iterator.remove(); // Remove the book with the specified ISBN
                DataManager.saveBooks(books); // Save the updated list of books
                return;
            }
        }
        // Additional methods can be added for extending the return date, searching for
        // books, etc.
    }

    public List<User> getAllUsers() throws IOException, ClassNotFoundException {
        // Use DataManager to load the list of users from the data store
        return DataManager.loadUsers();
    }

    public int getBorrowedBooksCount(User user) throws ClassNotFoundException, IOException {
        int count = 0;
        List<Transaction> transactions = getAllTransactions(); // Method to get all transactions
        Date currentDate = new Date();
        for (Transaction transaction : transactions) {
            if (transaction.getUser().equals(user) &&
                    currentDate.before(transaction.getReturnDate())) {
                count++;
            }
        }
        return count;
    }

    public List<Transaction> getAllTransactions() throws IOException, ClassNotFoundException {
        // Use DataManager to load the list of transactions from the data store
        return DataManager.loadTransactions();
    }

    public boolean removeUser(String username) {
        try {
            // Load the current list of users
            List<User> users = DataManager.loadUsers();
            // Find the user with the given username
            User userToRemove = users.stream()
                    .filter(u -> u.getUsername().equals(username) && "Member".equals(u.getUserType()))
                    .findFirst()
                    .orElse(null);

            // If the user is found and is a Member
            if (userToRemove != null) {
                // Remove the user from the list
                users.remove(userToRemove);
                // Save the updated list of users
                DataManager.saveUsers(users);

                // Additionally, remove any transactions associated with this user
                List<Transaction> transactions = DataManager.loadTransactions();
                transactions.removeIf(t -> t.getUser().equals(userToRemove));
                DataManager.saveTransactions(transactions);

                return true; // User and transactions successfully removed
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }

        return false; // User was not found or not a Member, or an error occurred
    }

}
