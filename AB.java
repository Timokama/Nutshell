
import javanut8.ch03.build.*;

public class AB {
	public static void main(String[] args) {
		A a = new A("Kamau");
		System.out.println(a.getName());
		System.out.println(a.examine(a));
		B b = new B("Timothy");
		System.out.println(b.getName());
		System.out.println(b.examine(b));
	}
}
