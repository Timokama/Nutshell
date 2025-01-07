import javanut8.ch03.build.suits.Suit;

public class suit {
    public static void main(String[] args) {
        try {
            // Get Suit by letter
            Suit h = Suit.fromLetter('H');
            Suit c = Suit.fromLetter('C');
            Suit d = Suit.fromLetter('D');
            Suit s = Suit.fromLetter('S');



            // Print Suit symbol and letter
            System.out.println("Suit: " + h);
            System.out.println("Symbol: " + h.getSymbol());
            System.out.println("Letter: " + h.getLetter());
            System.out.println();

            System.out.println("Suit: " + c);
            System.out.println("Symbol: " + c.getSymbol());
            System.out.println("Letter: " + c.getLetter());
            System.out.println();

            System.out.println("Suit: " + d);
            System.out.println("Symbol: " + d.getSymbol());
            System.out.println("Letter: " + d.getLetter());
            System.out.println();

            System.out.println("Suit: " + s);
            System.out.println("Symbol: " + s.getSymbol());
            System.out.println("Letter: " + s.getLetter());

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
