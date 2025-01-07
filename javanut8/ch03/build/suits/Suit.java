package javanut8.ch03.build.suits;

public enum Suit {
    HEART('♥'),
    CLUB('♣'),
    DIAMOND('♦'),
    SPADE('♠');

    private final char symbol;
    private final char letter;

    public char getSymbol() {
        return symbol;
    }

    public char getLetter() {
        return letter;
    }

    // Enum constructor
    Suit(char symbol) {
        this.symbol = symbol;
        this.letter = switch (symbol) {
            case '♥' -> 'H';
            case '♣' -> 'C';
            case '♦' -> 'D';
            case '♠' -> 'S';
            default -> throw new RuntimeException("Illegal: " + symbol);
        };
    }

    // Static method to get a Suit by its letter
    public static Suit fromLetter(char letter) {
        for (Suit suit : Suit.values()) {
            if (suit.letter == letter) {
                return suit;
            }
        }
        throw new IllegalArgumentException("No suit found for letter: " + letter);
    }
}
