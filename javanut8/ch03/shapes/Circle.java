package javanut8.ch03.shapes; // Specify a package for the class
import javanut8.ch03.shapes.*;

public final class Circle extends Shape implements Rotate90 {
	// The class is still public
	// This is a generally useful constant, so we keep it public
	public static final double PI = 3.14159;
    protected double r;
	// Radius is hidden but visible to subclasses
	// A method to enforce the restriction on the radius
	// Subclasses may be interested in this implementation detail
	protected void checkRadius(double radius) {
		if (radius < 0.0)
			throw new IllegalArgumentException("illegal negative radius");
	}
	// The non-default constructor
	public Circle(double r) {
		checkRadius(r);
		this.r = r;
	}
	// Public data accessor methods
	public double getRadius() { return r; }
	public void setRadius(double r) {
		checkRadius(r);
		this.r = r;
	}
	// Methods to operate on the instance field
	public double area() { return PI * r * r; }
	public double circumference() { return 2 * PI * r; }
	
	@Override
	public double clockwise() {
	// No-op, circles are rotation-invariant
		System.out.println("No-op, circles are rotation-invariant");
		return r;
	}
	
	@Override
	public double antiClockwise() {
	// No-op, circles are rotation-invariant
		System.out.println("No-op, circles are rotation-invariant");
		return r;
	}
}