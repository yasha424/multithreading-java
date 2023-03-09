package Symbols;

import Symbols.Printer.Printer;
import Symbols.Printer.PrinterThread;

public class Main {
    public static void main(String[] args) {
        Printer p1 = new Printer('-');
        Printer p2 = new Printer('|');

        PrinterThread pt1 = new PrinterThread(p1);
        PrinterThread pt2 = new PrinterThread(p2);

//        pt1.setPriority(10);
//        pt2.setPriority(1);

        pt1.start();
        pt2.start();
    }
}
