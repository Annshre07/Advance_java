//Creating the interface for SRP (Single Responsibility Principle)
interface Employee {
    void printPaySlip();
}

//Creating the interface for soring employee
interface EmployeeStorage {
    void save(EmployeeImp EmployeeImp);
}

//Employee Implementation
class EmployeeImp implements Employee {
    private String name;
    private double salary;

    // Constructor as the object EmployeeImp is created it gets iniialised.
    public EmployeeImp(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    //For getting name and salary
    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public void printPaySlip() {
        System.out.println("Employee: " + name + ", Salary: $" + salary);
    }
}



//This class is for database storage
class DatabaseStorage implements EmployeeStorage {
    @Override
    public void save(EmployeeImp employee) {
        System.out.println("Saving " + employee.getName() + " to the database...");
    }
}

//This class is for file storage
class FileStorage implements EmployeeStorage {
    @Override
    public void save(EmployeeImp employee) {
        System.out.println("Saving " + employee.getName() + " to a file...");
    }
}

// Main class
public class Employee2 {
    public static void main(String[] args) {
        // Creating Employee objects
        EmployeeImp emp1 = new EmployeeImp("Anuj", 2002.29);
        EmployeeImp emp2 = new EmployeeImp("Sugam", 2003.29);

        // Printing pay slips
        emp1.printPaySlip();
        emp2.printPaySlip();

        // Saving Employees
        EmployeeStorage dbStorage = new DatabaseStorage();
        EmployeeStorage fileStorage = new FileStorage();

        dbStorage.save(emp1);  
        fileStorage.save(emp2); 
    }
}
