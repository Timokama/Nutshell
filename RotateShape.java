import javanut8.ch03.shapes.*;


public class RotateShape {
	public static void main(String[] args) {
		Rotate90[] rotate = new Rotate90[3];
		// Create an array to hold shapes
		rotate[0] = new Circle(2.0);
		// Fill in the array
		rotate[1] = new Rectangle(1.0, 3.0);
		rotate[2] = new Rectangle(4.0, 2.0);
		
		Rectangle r1 = new Rectangle(1.0, 3.0);
		Rectangle r2 = new Rectangle(4.0, 2.0);
				
		
		for(int i = 0; i < rotate.length; i++) {
			double clockwise = rotate[i].clockwise();
			double antiClockwise = rotate[i].antiClockwise();
			// Compute the area of the shapes
			System.out.println("Rotate: " + rotate[i].getClass().getSimpleName() + " " + clockwise);
			System.out.println("Rotate: " + rotate[i].getClass().getSimpleName() + " " + antiClockwise);
			
		}
		System.out.println("Height: " + r1.getHeight() + "width: " + r1.getWidth());
		System.out.println("Height: " + r2.getHeight() + "width: " + r2.getWidth());
	}
}
