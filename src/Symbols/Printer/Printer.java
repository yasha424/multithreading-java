package Symbols.Printer;

public class Printer {
    private char symbol;

    public Printer(char symbol) {
        this.symbol = symbol;
    }

    public void print() {
        System.out.print(symbol);
    }
}
