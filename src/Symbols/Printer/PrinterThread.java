package Symbols.Printer;

import Symbols.Syncer.Syncer;

public class PrinterThread extends Thread {
    private Printer printer;
    private Syncer syncer;
    private boolean isPermitted;

    private static final int LINES_COUNT = 100;
    private static final int LINE_LENGTH = 100;

    public PrinterThread(Printer printer, Syncer syncer, boolean isPermitted) {
        this.printer = printer;
        this.syncer = syncer;
        this.isPermitted = isPermitted;
    }

    @Override
    public void run() {
        for (int i = 0; i < LINES_COUNT; i++) {
            for (int j = 0; j < LINE_LENGTH; j++) {
                syncer.func(new Runnable() {
                    @Override
                    public void run() {
                        printer.print();
                    }
                }, isPermitted);
            }
        }
    }

    public static int getLineLength() {
        return LINE_LENGTH;
    }
}
