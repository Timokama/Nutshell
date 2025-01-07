package javanut8.ch03.shapes;

public interface Positionable extends Centered {
    void setUpperRightCorner(double x, double y);
    double getUpperRightX();
    double getUpperRightY();
    void setLowerLeftCorner(double x, double y);
    double getLowerLeftX();
    double getLowerLeftY();
}