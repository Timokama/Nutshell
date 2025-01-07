package javanut8.ch03.shapes;

public class Box<T> {
    protected T value;

    public void box(T t) {
        value = t;
    }
    public T unbox() {
        T t = value;
        value = null;
        return t;
    }
}