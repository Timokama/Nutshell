import javanut8.ch03.shapes.*;


public class CenteredShape {
	public static void main(String[] args) {
		Shape[] shapes = new Shape[3];
		// Create an array to hold shapes
		// Create some centered shapes, and store them in the Shape[]
		// No cast necessary: these are all compatible assignments
		shapes[0] = new CenteredCircle(1.0, 1.0, 1.0);
		shapes[1] = new CenteredSquare(2.5, 2, 3);
		shapes[2] = new CenteredRectangle(2.3, 4.5, 3, 4);
		
		// Compute average area of the shapes and
		// average distance from the origin
		double totalArea = 0;
		double totalDistance = 0;
		for(int i = 0; i < shapes.length; i = i + 1) {
			totalArea += shapes[i].area();
			// Compute the area of the shapes
			// Be careful, in general, the use of instanceof to determine the
			// runtime type of an object is quite often an indication of a
			// problem with the design
			if (shapes[i] instanceof Centered) { // The shape is a Centered shape
				// Note the required cast from Shape to Centered (no cast would
				// be required to go from CenteredSquare to Centered, however).
				Centered c = (Centered) shapes[i];
				double cx = c.getCenterX();
				// Get coordinates of the center
				double cy = c.getCenterY();
				// Compute distance from origin
				totalDistance += Math.sqrt(cx*cx + cy*cy);
			}
		}
		System.out.println("Average area: " + totalArea/shapes.length);
		System.out.println("Average distance: " + totalDistance/shapes.length);
	}
}
