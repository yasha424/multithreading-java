package Symbols.Printer;

public class PrinterThread extends Thread {
    private Printer printer;

    private static final int LINES_COUNT = 100;
    private static final int LINE_LENGTH = 100;

    public PrinterThread(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        for (int i = 0; i < LINES_COUNT; i++) {
            for (int j = 0; j < LINE_LENGTH; j++) {
                printer.print();
                this.interrupt();
            }
            System.out.println();
        }
    }
}
