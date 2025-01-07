package javanut8.ch03.shapes;

import javanut8.ch03.shapes.*;
// Abstract methods: note
// semicolon instead of body.

public final class Rectangle extends Shape implements Rotate90 {
	// instance data
	protected double w, h;
	
	// Constructor
	public Rectangle(double w, double h) {
		this.w = w; this.h = h;
	}
	// Accessor method
	public double getWidth() { return w; }
	public double getHeight() { return h; }
	
	// implementation of abstractor methods
	public double area() { return w*h; }
	public double circumference() { return 2*(w + h); }
	
	@Override
	public double clockwise() {
		// Swap width and height
		double tmp = w;
		w = h;
		h = tmp;
		System.out.println("Swap width and height");
		System.out.println("w: " + w + "h: " + h);
		return tmp;
	}
	@Override
	public double antiClockwise() {
		// Swap width and height
		double tmp = w;
		w = h;
		h = tmp;
		System.out.println("Swap width and height");
		System.out.println("w: " + w + "h: " + h);
		return tmp;
	}
}