package javanut8.ch03.build;

import javanut8.ch03.build.A;

public class B extends A {
	public B(String named) {
		super(named);
	}
	@Override
	public String getName() {
		return "B: " + name;
	}
	public String examine(B b) {
		return "B sees another B: " + b.name;
	}
}
