package javanut8.ch03.shapes;
import javanut8.ch03.shapes.*;

public interface Vocal {
    default void call() {
        System.out.println("Hello!");
    }
}