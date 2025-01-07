package javanut8.ch03.shapes;
import javanut8.ch03.shapes.*;

public class CenteredRectangle extends Rectangle implements Centered {
    // New instance fields
    private double cx, cy;

    // A constructor
    public CenteredRectangle(double cx, double cy, double w, double h) {
        super(w, h);
        this.cx = cx;
        this.cy = cy;
    }

    // We inherit all the methods of Rectangle but must
    // implement the methods of Centered
    public void setCenter(double x, double y) { cx = x; cy = y; }
    public double getCenterX() { return cx; }
    public double getCenterY() { return cy; }    
}