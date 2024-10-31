package tolobekov.library;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tolobekov.library.model.Book;

public class BookTest {

    private Book book;

    @Before
    public void setUp() {
        book = new Book("1984", "George Orwell", "1234567890123", true);
    }

    @Test
    public void testBookConstructorAndGetters() {
        assertEquals("1984", book.getTitle());
        assertEquals("George Orwell", book.getAuthor());
        assertEquals("1234567890123", book.getIsbn());
        assertTrue(book.isAvailable());
    }

    @Test
    public void testSetTitle() {
        book.setTitle("Animal Farm");
        assertEquals("Animal Farm", book.getTitle());
    }

    @Test
    public void testSetAuthor() {
        book.setAuthor("G. Orwell");
        assertEquals("G. Orwell", book.getAuthor());
    }

    @Test
    public void testSetIsbn() {
        book.setIsbn("9876543210123");
        assertEquals("9876543210123", book.getIsbn());
    }

    @Test
    public void testSetAvailable() {
        book.setAvailable(false);
        assertFalse(book.isAvailable());
    }

    @Test
    public void testUpdateAvailability() {
        book.updateAvailability(false);
        assertFalse(book.isAvailable());
    }

    @Test
    public void testToString() {
        String expected = "Book{title='1984', author='George Orwell', isbn='1234567890123', isAvailable=true}";
        assertEquals(expected, book.toString());
    }

    @Test
    public void testEquals() {
        Book sameBook = new Book("1984", "George Orwell", "1234567890123", true);
        Book differentBook = new Book("Animal Farm", "George Orwell", "9876543210123", true);

        assertTrue(book.equals(sameBook));
        assertFalse(book.equals(differentBook));
    }

    @Test
    public void testHashCode() {
        Book sameBook = new Book("1984", "George Orwell", "1234567890123", true);
        assertEquals(book.hashCode(), sameBook.hashCode());
    }
}
