/*The issue in the given code was it did not had an interface
 which acts as a base  class which has the basic functionality of eating.
  */
// Base abstract class for all birds since all bird eats
abstract class Bird {
    public void eat() {
        System.out.println("This bird is eating.");
    }
}

// Interface for Flying Birds
interface Fly {
    void fly();
}

// Interface for Swimming Birds
interface Swim{
    void swim();
}

// Sparrow class 
//This class extends Birds since Bird is abstract class and Fly is an interface.
class Sparrow extends Bird implements Fly {
    private String name;

    public Sparrow(String name) {
        this.name = name; 
    }

    @Override
    public void fly() {
        System.out.println(name + " is flying.");
    }

    @Override
    public void eat() {
        System.out.println(name + " is eating.");
    }
}

// Penguin class 
class Penguin extends Bird implements Swim{
    private String name;

    public Penguin(String name) {
        this.name = name; 
    }

    @Override
    public void swim() {
        System.out.println(name + " is swimming.");
    }

    @Override
    public void eat() {
        System.out.println(name + " is eating.");
    }
}

// Main class
public class LSP {
    public static void main(String[] args) {
        Sparrow sparrow = new Sparrow("Sparrow");
        Penguin penguin = new Penguin("Penguin");

        sparrow.eat();
        sparrow.fly();

        penguin.eat();
        penguin.swim();
    }
}
