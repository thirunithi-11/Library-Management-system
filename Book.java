package LibraryManagement;
import java.time.LocalDate;

public class Book {

	    private String bookId;
	    private String bookName;
	    private String author;
	    private double price;
	    private boolean available;

	    private String issuedStudentId;
	    private LocalDate issueDate;
	    private LocalDate dueDate;

	    public Book(String bookId, String bookName,
	                String author, double price) {

	        this.bookId = bookId;
	        this.bookName = bookName;
	        this.author = author;
	        this.price = price;
	        this.available = true;
	    }

	    public String getBookId() {
	        return bookId;
	    }

	    public String getBookName() {
	        return bookName;
	    }

	    public String getAuthor() {
	        return author;
	    }

	    public double getPrice() {
	        return price;
	    }

	    public boolean isAvailable() {
	        return available;
	    }

	    public LocalDate getDueDate() {
	        return dueDate;
	    }

	    public void issueBook(String studentId) {

	        available = false;
	        issuedStudentId = studentId;
	        issueDate = LocalDate.now();
	        dueDate = issueDate.plusDays(7);
	    }

	    public void returnBook() {

	        available = true;
	        issuedStudentId = null;
	        issueDate = null;
	        dueDate = null;
	    }

	    public void displayBook() {

	        System.out.println("-----------------------------");
	        System.out.println("Book ID   : " + bookId);
	        System.out.println("Book Name : " + bookName);
	        System.out.println("Author    : " + author);
	        System.out.println("Price     : Rs." + price);
	        System.out.println("Status    : "
	                + (available ? "Available" : "Not Available"));
	        System.out.println("-----------------------------");
	    }
	}


