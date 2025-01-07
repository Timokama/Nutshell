import javanut8.ch03.shapes.*;

public class GenericBox {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Compiles
        Box<Integer> b = new Box<>();
		NumberBox<Integer> n = new NumberBox<>();
		ComparingBox<Integer> c = new ComparingBox<>();
		// This is very dangerous
        int a = 12;
        b.box(a);
		n.box(10);
        c.box(10);
        c.compareTo(c);
		System.out.println(b.unbox());
        System.out.println(n.unbox());
        System.out.println(c.compareTo(c));
	}

}
