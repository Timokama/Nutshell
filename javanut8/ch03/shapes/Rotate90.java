package javanut8.ch03.shapes;

public sealed interface Rotate90 permits Circle, Rectangle {
	double clockwise();
	double antiClockwise();
}
