package Counter;

public class Main {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        int countOfOperations = 100000;

        Counter counter = new Counter();

//        CounterThread counterThread1 = new CounterThread(counter::increment, countOfOperations);
//        CounterThread counterThread2 = new CounterThread(counter::decrement, countOfOperations);

//        CounterThread counterThread1 = new CounterThread(counter::syncIncrement, countOfOperations);
//        CounterThread counterThread2 = new CounterThread(counter::syncDecrement, countOfOperations);

//        CounterThread counterThread1 = new CounterThread(counter::lockerIncrement, countOfOperations);
//        CounterThread counterThread2 = new CounterThread(counter::lockerDecrement, countOfOperations);

        CounterThread counterThread1 = new CounterThread(counter::syncBlockIncrement, countOfOperations);
        CounterThread counterThread2 = new CounterThread(counter::syncBlockDecrement, countOfOperations);

        counterThread1.setPriority(Thread.MAX_PRIORITY);
        counterThread2.setPriority(Thread.MAX_PRIORITY);

        counterThread1.start();
        counterThread2.start();

        try {
            counterThread1.join();
            counterThread2.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        System.out.println(counter.getCount());

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
