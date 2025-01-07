package javanut8.ch03.shapes;
import javanut8.ch03.shapes.*;
public class Person implements Vocal, Caller {
    @Override
    public void call() {
        // Can do our own thing
        // or delegate to either interface
        // e.g.,
        Vocal.super.call();
        // or
        Caller.super.call();
    }
}