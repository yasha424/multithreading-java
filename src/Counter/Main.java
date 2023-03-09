package Counter;

public class Main {
    public static void main(String[] args) {
        int countOfOperarions = 100000;

        Counter counter = new Counter();

        CounterThread counterThread1 = new CounterThread(counter::increment, countOfOperarions);
        CounterThread counterThread2 = new CounterThread(counter::decrement, countOfOperarions);

        counterThread1.start();
        counterThread2.start();

        try {
            counterThread1.join();
            counterThread2.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        System.out.println(counter.getCount());
    }
}
