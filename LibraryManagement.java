package LibraryManagement;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
public class LibraryManagement implements LibraryOperations {

    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();
    Map<String, Book> issuedBooks = new HashMap<>();

    Librarian librarian =
            new Librarian("L001", "Thiru", "Head Librarian");

    @Override
    public void addBook() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Book ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Book Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Author: ");
        String author = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        books.add(new Book(id, name, author, price));

        System.out.println("Book added successfully!");
    }

    @Override
    public void searchBook() {

        try (Scanner sc = new Scanner(System.in)) {
			System.out.println("\nBook Name\tAuthor");

			for (Book book : books) {
			    System.out.println(
			            book.getBookName() + "\t" +
			            book.getAuthor());
			}

			System.out.print("\nEnter Book Name: ");
			String name = sc.nextLine();

			boolean found = false;

			for (Book book : books) {

			    if (book.getBookName()
			            .equalsIgnoreCase(name)) {

			        System.out.println("\nBook Name : "
			                + book.getBookName());

			        System.out.println("Author    : "
			                + book.getAuthor());

			        System.out.println("Status    : "
			                + (book.isAvailable()
			                ? "Available"
			                : "Not Available"));

			        found = true;
			    }
			}

			if (!found) {
			    System.out.println("Book not found.");
			}
		}
    }

    @Override
    public void issueBook() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Book ID: ");
        String bookId = sc.nextLine();

        System.out.print("Enter Student ID: ");
        String studentId = sc.nextLine();

        Student student = getStudent(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        try {

            Book book = findBook(bookId);

            if (book == null) {
                throw new Exception("Invalid Book ID.");
            }

            if (!book.isAvailable()) {
                throw new Exception(
                        "Book is already issued.");
            }

            if (student.getBooksTaken() >= 3) {
                throw new Exception(
                        "Maximum 3 books allowed.");
            }

            if (!librarian.approveIssue()) {
                throw new Exception(
                        "Librarian did not approve.");
            }

            book.issueBook(student.getId());

            issuedBooks.put(bookId, book);

            student.increaseBooks();

            System.out.println(
                    "Book issued successfully!");

            System.out.println(
                    "Issue Date : " + LocalDate.now());

            System.out.println(
                    "Due Date   : " + book.getDueDate());

        } catch (Exception e) {

            System.out.println(
                    "Error : " + e.getMessage());
        }
    }

    @Override
    public void returnBook() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Book ID: ");
        String bookId = sc.nextLine();

        System.out.print("Enter Student ID: ");
        String studentId = sc.nextLine();

        Student student = getStudent(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println(
                "Damage Type: none / minor / page / major");

        System.out.print("Enter Damage Type: ");
        String damageType = sc.nextLine();

        try {

            Book book = findBook(bookId);

            if (book == null) {
                throw new Exception(
                        "Invalid Book ID.");
            }

            if (book.isAvailable()) {
                throw new Exception(
                        "This book is not issued.");
            }

            double fine =
                    calculateFine(book, damageType);

            book.returnBook();

            issuedBooks.remove(bookId);

            student.decreaseBooks();

            System.out.println(
                    "Book returned successfully!");

            if (fine > 0) {

                System.out.println(
                        "Total Fine : Rs." + fine);

                librarian.collectFine(fine);

            } else {

                System.out.println("No Fine.");
            }

        } catch (Exception e) {

            System.out.println(
                    "Error : " + e.getMessage());
        }
    }

    public Book findBook(String bookId) {

        for (Book book : books) {

            if (book.getBookId()
                    .equalsIgnoreCase(bookId)) {

                return book;
            }
        }

        return null;
    }

    public double calculateFine(
            Book book,
            String damageType) {

        double fine = 0;

        LocalDate today = LocalDate.now();

        if (today.isAfter(book.getDueDate())) {

            long lateDays =
                    ChronoUnit.DAYS.between(
                            book.getDueDate(),
                            today);

            fine = lateDays * 5;
        }

        switch (damageType.toLowerCase()) {

        case "none":
            break;

        case "minor":
            fine = fine + 20;
            break;

        case "page":
            fine = fine + 50;
            break;

        case "major":
            fine = fine + 100;
            break;

        default:
            System.out.println(
                    "Invalid damage type.");
        }

        return fine;
    }

    @Override
    public void showAvailableBooks() {

        System.out.println("\nAvailable Books:");

        for (Book book : books) {

            if (book.isAvailable()) {
                book.displayBook();
            }
        }
    }

    public void showOverdueBooks() {

        LocalDate today = LocalDate.now();

        ArrayList<Book> overdueBooks =
                books.stream()
                .filter(book -> !book.isAvailable())
                .filter(book ->
                        book.getDueDate()
                        .isBefore(today))
                .collect(
                        Collectors.toCollection(
                                ArrayList::new));

        if (overdueBooks.isEmpty()) {

            System.out.println(
                    "No overdue books.");

        } else {

            System.out.println(
                    "\nOverdue Books:");

            overdueBooks.forEach(
                    Book::displayBook);
        }
    }

    public void showBookNames() {

        ArrayList<String> names =
                books.stream()
                .map(Book::getBookName)
                .collect(
                        Collectors.toCollection(
                                ArrayList::new));

        System.out.println("\nBook Names:");

        names.forEach(
                System.out::println);
    }

    public void sortBooksByPrice() {

        books.stream()
                .sorted((b1, b2) ->
                        Double.compare(
                                b1.getPrice(),
                                b2.getPrice()))
                .forEach(Book::displayBook);
    }

    public void generateReport() {

        long totalBooks = books.size();

        long availableBooks =
                books.stream()
                .filter(Book::isAvailable)
                .count();

        long issuedBooksCount =
                books.stream()
                .filter(book -> !book.isAvailable())
                .count();

        System.out.println(
                "\n========== LIBRARY REPORT ==========");

        System.out.println(
                "Total Books     : " + totalBooks);

        System.out.println(
                "Available Books : " + availableBooks);

        System.out.println(
                "Issued Books    : " + issuedBooksCount);

        System.out.println(
                "Students        : " + students.size());

        System.out.println(
                "====================================");
    }

    public void dueDateReminder() {

        LocalDate today = LocalDate.now();

        for (Book book : issuedBooks.values()) {

            long days =
                    ChronoUnit.DAYS.between(
                            today,
                            book.getDueDate());

            if (days == 0) {

                System.out.println(
                        "Reminder: "
                        + book.getBookName()
                        + " is due today.");

            } else if (days > 0) {

                System.out.println(
                        "Book "
                        + book.getBookName()
                        + " is due in "
                        + days + " days.");

            } else {

                System.out.println(
                        "Book "
                        + book.getBookName()
                        + " is overdue.");
            }
        }
    }

    public void saveReportToFile() {

        try {

            FileWriter writer =
                    new FileWriter(
                            "library_report.txt");

            writer.write(
                    "LIBRARY MANAGEMENT REPORT\n");

            writer.write(
                    "========================\n");

            writer.write(
                    "Total Books : "
                    + books.size() + "\n");

            writer.write(
                    "Available Books : "
                    + books.stream()
                    .filter(Book::isAvailable)
                    .count() + "\n");

            writer.write(
                    "Issued Books : "
                    + issuedBooks.size() + "\n");

            writer.close();

            System.out.println(
                    "Report saved successfully.");

        } catch (IOException e) {

            System.out.println(
                    "File error : "
                    + e.getMessage());
        }
    }

    public void startReminderThread() {

        Thread reminderThread =
                new Thread(() -> {

                    System.out.println(
                            "\nChecking due dates...");

                    dueDateReminder();
                });

        reminderThread.start();
    }

    public void addSampleData() {

        books.add(new Book(
                "B101",
                "Java Programming",
                "James",
                500));

        books.add(new Book(
                "B102",
                "SQL Basics",
                "John",
                400));

        books.add(new Book(
                "B103",
                "HTML and CSS",
                "David",
                350));

        books.add(new Book(
                "B104",
                "Spring Boot",
                "Mark",
                600));

        students.add(new Student(
                "S101",
                "Arun",
                "ECE"));

        students.add(new Student(
                "S102",
                "Priya",
                "CSE"));
    }

    public Student getStudent(String studentId) {

        for (Student student : students) {

            if (student.getId()
                    .equalsIgnoreCase(studentId)) {

                return student;
            }
        }

        return null;
    }

    public static void main(String[] args) {

        LibraryManagement library =
                new LibraryManagement();

        library.addSampleData();

        Scanner sc =
                new Scanner(System.in);

        int choice;

        do {

            System.out.println(
                    "\n========== LIBRARY MANAGEMENT ==========");

            System.out.println("1. Add Book");
            System.out.println("2. Search Book");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Show Available Books");
            System.out.println("6. Show Overdue Books");
            System.out.println("7. Due Date Reminder");
            System.out.println("8. Generate Report");
            System.out.println("9. Show Book Names");
            System.out.println("10. Sort Books By Price");
            System.out.println("11. Save Report");
            System.out.println("12. Start Reminder Thread");
            System.out.println("13. Exit");

            System.out.print(
                    "Enter your choice: ");

            try {

                choice = sc.nextInt();
                sc.nextLine();

            } catch (Exception e) {

                System.out.println(
                        "Please enter a number.");

                sc.nextLine();

                choice = 0;
            }

            switch (choice) {

            case 1:
                library.addBook();
                break;

            case 2:
                library.searchBook();
                break;

            case 3:
                library.issueBook();
                break;

            case 4:
                library.returnBook();
                break;

            case 5:
                library.showAvailableBooks();
                break;

            case 6:
                library.showOverdueBooks();
                break;

            case 7:
                library.dueDateReminder();
                break;

            case 8:
                library.generateReport();
                break;

            case 9:
                library.showBookNames();
                break;

            case 10:
                library.sortBooksByPrice();
                break;

            case 11:
                library.saveReportToFile();
                break;

            case 12:
                library.startReminderThread();
                break;

            case 13:
                System.out.println(
                        "Thank you for using Library Management System.");
                break;

            default:
                System.out.println(
                        "Invalid choice.");
            }

        } while (choice != 13);

        sc.close();
    }
}