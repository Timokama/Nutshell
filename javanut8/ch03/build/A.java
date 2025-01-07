package javanut8.ch03.build;

public class A {
	protected final String name;
	public A(String named) {
		name =named;
	}
	public String getName() {
		return name;
	}
	public String examine(A a) {
		return "B sees: " + a.name;
	}
}
