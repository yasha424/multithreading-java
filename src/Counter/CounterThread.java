package Counter;

public class CounterThread extends Thread {
    private Runnable lamda;
    private int countOfOperations;

    public CounterThread(Runnable lamda, int countOfOperations) {
        this.lamda = lamda;
        this.countOfOperations = countOfOperations;
    }

    @Override
    public void run() {
        for (int i = 0; i < countOfOperations; i++) {
            lamda.run();
        }
    }
}
