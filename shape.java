import javanut8.ch03.shapes.*;


public class shape {
	public static void main(String[] args) {
		Shape[] shapes = new Shape[3];
		// Create an array to hold shapes
		shapes[0] = new Circle(2.0);
		// Fill in the array
		shapes[1] = new Rectangle(1.0, 3.0);
		shapes[2] = new Rectangle(4.0, 2.0);
		double totalArea = 0;
		for(int i = 0; i < shapes.length; i++) {
			totalArea += shapes[i].area();
			System.out.println(shapes[i].getClass().getSimpleName() + " " + shapes[i].area());
			
			// Compute the area of the shapes
		}
		System.out.println(totalArea);

		Point p = new Point(1.5, 2.5);
		System.out.println(((Translatable) p).deltaX(1.5));
	}
}
