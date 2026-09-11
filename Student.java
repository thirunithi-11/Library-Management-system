package LibraryManagement;

public class Student extends Person {
	private String department;
    private int booksTaken;

    public Student(String id, String name, String department) {
        super(id, name);
        this.department = department;
        this.booksTaken = 0;
    }

    public int getBooksTaken() {
        return booksTaken;
    }

    public void increaseBooks() {
        booksTaken++;
    }

    public void decreaseBooks() {
        if (booksTaken > 0) {
            booksTaken--;
        }
    }

	@Override
	public void displayDetails() {
		// TODO Auto-generated method stub
		System.out.println("Student ID : " + id);
        System.out.println("Name       : " + name);
        System.out.println("Department : " + department);
        System.out.println("Books Taken: " + booksTaken);
    }

		

	}


