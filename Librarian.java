package LibraryManagement;

public class Librarian extends Person {

    private String designation;

    public Librarian(String id, String name, String designation) {
        super(id, name);
        this.designation = designation;
    }
	@Override
	public void displayDetails() {
		// TODO Auto-generated method stub
		        System.out.println("Librarian ID : " + id);
		        System.out.println("Name         : " + name);
		        System.out.println("Designation  : " + designation);
		    }

		    public boolean approveIssue() {
		        return true;
		    }

		    public void collectFine(double fine) {
		        System.out.println("Fine collected by librarian.");
		    }

	}


