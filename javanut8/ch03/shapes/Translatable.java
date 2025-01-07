package javanut8.ch03.shapes;

public interface Translatable {
    Translatable deltaX(double dx);
    Translatable deltaY(double dy);
    Translatable delta(double dx, double dy);
}