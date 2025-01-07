package javanut8.ch03.shapes;
import javanut8.ch03.shapes.*;

public interface Caller {
    default void call() {
        Switchboard.placeCall(this);
    }
}