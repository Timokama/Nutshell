import javanut8.ch03.shapes.*;

public class PersonCaller {
    public static void main(String[] args) {
        // Create an instance of Person
        Person person = new Person();

        // Call the overridden `call` method
        System.out.println("Calling using Person's call method:");
        person.call();

        // If needed, you can test specific interface calls like this:
        System.out.println("\nCalling using Vocal's call method:");
        ((Vocal) person).call();

        System.out.println("\nCalling using Caller's call method:");
        ((Caller) person).call();
    }
}
