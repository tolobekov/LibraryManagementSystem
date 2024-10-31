package tolobekov.library.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private User user;
    private Book book;
    private Date issueDate;
    private Date returnDate;

    // Constructor
    public Transaction(User user, Book book, Date issueDate, Date returnDate) {
        this.user = user;
        this.book = book;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    // Getters
    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    // Additional methods
    public void extendReturnDate(int days) {
        // Assuming returnDate is already set
        long extraTime = days * 24 * 60 * 60 * 1000; // days to milliseconds
        this.returnDate = new Date(this.returnDate.getTime() + extraTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Transaction))
            return false;
        Transaction that = (Transaction) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(book, that.book) &&
                Objects.equals(issueDate, that.issueDate) &&
                Objects.equals(returnDate, that.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, book, issueDate, returnDate);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "user=" + user.getUsername() +
                ", book=" + book.getTitle() +
                ", issueDate=" + issueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
