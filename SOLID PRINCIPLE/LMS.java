public class LMS {

    static class Book {
        String title;
        void display() {
            System.out.println("Book Title: " + title);
        }
    }

    static class Library {
        String name;
        void display() {
            System.out.println("Library Name: " + name);
        }
    }

    static class Student {
        String name;
        void display() {
            System.out.println("Student Name: " + name);
        }
    }

    public static void main(String[] args) {
        Book book = new Book();
        Library library = new Library();
        Student student = new Student();

        book.title = "Lovely War";
        library.name = "NDG_LIB_SSSIHL";
        student.name = "SAI";

        book.display();
        library.display();
        student.display();
    }
}
