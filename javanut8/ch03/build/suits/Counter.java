package javanut8.ch03.build.suits;

public class Counter {
    private int i = 0;

    public synchronized int increment() {
        return i = i + 1;
    }

    public synchronized int getCounter() {
        return i;
    }
}

