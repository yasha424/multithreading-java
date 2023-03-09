package Symbols.Syncer;

import Symbols.Printer.PrinterThread;

public class Syncer {
    private boolean isPermitted;
    private int runs = 0;

    public Syncer(boolean isPermitted) {
        this.isPermitted = isPermitted;
    }

    public synchronized void func(Runnable func, boolean isPermitted) {
        while (this.isPermitted != isPermitted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }

        this.isPermitted = !this.isPermitted;
        func.run();
        runs++;

        if (runs % PrinterThread.getLineLength() == 0) {
            System.out.println();
        }

        notifyAll();
    }
}
