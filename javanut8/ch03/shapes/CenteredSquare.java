package javanut8.ch03.shapes;
import javanut8.ch03.shapes.*;

public class CenteredSquare extends Rectangle implements Centered {
    // New instance fields
    private double cx, cy;
    // A constructor
    public CenteredSquare(double cx, double cy, double h) {
        super(h, h);
        this.cx = cx;
        this.cy = cy;
    }
    // We inherit all the methods of Rectangle but must
    // provide implementations of all the Centered methods.
    public void setCenter(double x, double y) { cx = x; cy = y; }
    public double getCenterX() { return cx; }
    public double getCenterY() { return cy; }
}