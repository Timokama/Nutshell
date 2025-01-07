
import javanut8.ch03.build.Car;
import javanut8.ch03.build.SportsCar;

public class SportCar {
	public static void main(String[] args) {
		Car car = new Car(360, 15.0, 5);
		System.out.println(car.range());
		SportsCar sport = new SportsCar(180);
		System.out.println(sport.getEfficiency());
		System.out.println(sport.range());
	}
}
