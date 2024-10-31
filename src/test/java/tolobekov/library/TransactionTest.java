package tolobekov.library;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tolobekov.library.model.Book;
import tolobekov.library.model.Transaction;
import tolobekov.library.model.User;

import java.util.Calendar;
import java.util.Date;

public class TransactionTest {

    private Transaction transaction;
    private User user;
    private Book book;
    private Date issueDate;
    private Date returnDate;

    @Before
    public void setUp() {
        user = new User("johndoe", "password123", "member");
        book = new Book("1984", "George Orwell", "1234567890123", true);
        issueDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issueDate);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        returnDate = calendar.getTime();
        transaction = new Transaction(user, book, issueDate, returnDate);
    }

    @Test
    public void testTransactionConstructorAndGetters() {
        assertEquals(user, transaction.getUser());
        assertEquals(book, transaction.getBook());
        assertEquals(issueDate, transaction.getIssueDate());
        assertEquals(returnDate, transaction.getReturnDate());
    }

    @Test
    public void testSetUser() {
        User newUser = new User("janedoe", "newpassword123", "member");
        transaction.setUser(newUser);
        assertEquals(newUser, transaction.getUser());
    }

    @Test
    public void testSetBook() {
        Book newBook = new Book("Brave New World", "Aldous Huxley", "9876543210123", true);
        transaction.setBook(newBook);
        assertEquals(newBook, transaction.getBook());
    }

    @Test
    public void testSetIssueDate() {
        Date newIssueDate = new Date();
        transaction.setIssueDate(newIssueDate);
        assertEquals(newIssueDate, transaction.getIssueDate());
    }

    @Test
    public void testSetReturnDate() {
        Date newReturnDate = new Date();
        transaction.setReturnDate(newReturnDate);
        assertEquals(newReturnDate, transaction.getReturnDate());
    }

    @Test
    public void testExtendReturnDate() {
        int daysToExtend = 10;
        transaction.extendReturnDate(daysToExtend);
        Calendar expectedReturnDate = Calendar.getInstance();
        expectedReturnDate.setTime(returnDate);
        expectedReturnDate.add(Calendar.DAY_OF_MONTH, daysToExtend);
        assertEquals(expectedReturnDate.getTime(), transaction.getReturnDate());
    }

    @Test
    public void testEquals() {
        Transaction sameTransaction = new Transaction(user, book, issueDate, returnDate);
        Transaction differentTransaction = new Transaction(new User("janedoe", "newpassword123", "member"),
                new Book("Brave New World", "Aldous Huxley", "9876543210123", true),
                new Date(), null);

        assertTrue(transaction.equals(sameTransaction));
        assertFalse(transaction.equals(differentTransaction));
    }

    @Test
    public void testHashCode() {
        Transaction sameTransaction = new Transaction(user, book, issueDate, returnDate);
        assertEquals(transaction.hashCode(), sameTransaction.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "Transaction{" +
                "user=johndoe" +
                ", book=1984" +
                ", issueDate=" + issueDate +
                ", returnDate=" + returnDate +
                '}';
        assertEquals(expected, transaction.toString());
    }
}
