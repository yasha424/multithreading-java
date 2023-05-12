package Bank;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Bank {
    public static final int NTEST = 10000;
    private final int[] accounts;
    private long ntransacts = 0;
    private final ReentrantLock locker = new ReentrantLock();
    Condition emptyCondition = locker.newCondition();

    private final Object lockedObject = new Object();

    public Bank(int n, int initialBalance){
        accounts = new int[n];
        int i;
        for (i = 0; i < accounts.length; i++)
            accounts[i] = initialBalance;
        ntransacts = 0;
    }

    public synchronized void synchronizedTransfer(int from, int to, int amount) {
       while (accounts[from] < amount) {
           try {
               wait();
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
       }

        accounts[from] -= amount;
        accounts[to] += amount;
        ntransacts++;
        if (ntransacts % NTEST == 0) {
            test();
        }

        notifyAll();
    }

    public void synchronizedBlockTransfer(int from, int to, int amount) {
        synchronized (lockedObject) {
            while (accounts[from] < amount) {
                try {
                    lockedObject.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            if (ntransacts % NTEST == 0) {
                test();
            }

            lockedObject.notifyAll();
        }
    }

    public void lockedTransfer(int from, int to, int amount) {
        locker.lock();
        try {
            while (accounts[from] < amount) {
                try {
                    emptyCondition.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            if (ntransacts % NTEST == 0) {
                test();
            }
            emptyCondition.signalAll();
        } finally {
            locker.unlock();
        }
    }

    public void transfer(int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;
        ntransacts++;
        if (ntransacts % NTEST == 0)
            test();
    }

    public void test(){
        int sum = 0;
        for (int i = 0; i < accounts.length; i++)
            sum += accounts[i] ;
        System.out.println("Transactions:" + ntransacts
                + " Sum: " + sum);
    }
    public int size(){
        return accounts.length;
    }
}