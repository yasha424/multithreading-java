package Symbols;

import Symbols.Printer.Printer;
import Symbols.Printer.PrinterThread;
import Symbols.Syncer.Syncer;

public class Main {
    public static void main(String[] args) {
        Printer p1 = new Printer('-');
        Printer p2 = new Printer('|');

        Syncer syncer = new Syncer(true);

        PrinterThread pt1 = new PrinterThread(p1, syncer, true);
        PrinterThread pt2 = new PrinterThread(p2, syncer, false);

        pt1.start();
        pt2.start();
    }
}
