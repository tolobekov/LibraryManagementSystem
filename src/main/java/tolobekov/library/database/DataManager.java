package tolobekov.library.database;

import tolobekov.library.model.Book;
import tolobekov.library.model.User;
import tolobekov.library.model.Transaction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String BOOK_FILE = "books.ser";
    private static final String USER_FILE = "users.ser";
    private static final String TRANSACTION_FILE = "transactions.ser";

    // Book Operations
    public static void saveBooks(List<Book> books) throws IOException {
        SerializationHelper.serialize(books, BOOK_FILE);
    }

    // Safely cast the deserialized object to List<Book>
    public static List<Book> loadBooks() throws IOException, ClassNotFoundException {
        return castDeserializedObjectToListOfType(SerializationHelper.deserialize(BOOK_FILE), Book.class);
    }

    // User Operations
    public static void saveUsers(List<User> users) throws IOException {
        SerializationHelper.serialize(users, USER_FILE);
    }

    // Safely cast the deserialized object to List<User>
    public static List<User> loadUsers() throws IOException, ClassNotFoundException {
        return castDeserializedObjectToListOfType(SerializationHelper.deserialize(USER_FILE), User.class);
    }

    // Transaction Operations
    public static void saveTransactions(List<Transaction> transactions) throws IOException {
        SerializationHelper.serialize(transactions, TRANSACTION_FILE);
    }

    // Safely cast the deserialized object to List<Transaction>
    public static List<Transaction> loadTransactions() throws IOException, ClassNotFoundException {
        return castDeserializedObjectToListOfType(SerializationHelper.deserialize(TRANSACTION_FILE), Transaction.class);
    }

    // Generic method to safely cast deserialized objects to a list of a specific
    // type
    @SuppressWarnings("unchecked")
    private static <T> List<T> castDeserializedObjectToListOfType(Object deserializedObject, Class<T> type) {
        if (deserializedObject instanceof List<?>) {
            List<?> rawList = (List<?>) deserializedObject;
            if (!rawList.isEmpty()) {
                // Check if all elements are instances of T
                for (Object item : rawList) {
                    if (!type.isInstance(item)) {
                        // If not, return an empty List
                        return new ArrayList<>();
                    }
                }
                // If all items are instances of T, return the list casted to List<T>
                return (List<T>) rawList;
            }
        }
        return new ArrayList<>();
    }
}
