package Bank;

class TransferThread extends Thread {
    private Bank bank;
    private int fromAccount;
    private int maxAmount;
    private static final int REPS = 1000;
    private final Method method;

    public TransferThread(Bank b, int from, int max, Method method){
        bank = b;
        fromAccount = from;
        maxAmount = max;
        this.method = method;
    }

    @Override
    public void run(){
        while (true) {
            for (int i = 0; i < REPS; i++) {
                int toAccount = (int) (bank.size() * Math.random());
                int amount = (int) (maxAmount * Math.random()/REPS);

                switch (method) {
                    case async -> {
                        bank.transfer(fromAccount, toAccount, amount);
                    }
                    case sync -> {
                        bank.synchronizedTransfer(fromAccount, toAccount, amount);
                    }
                    case syncBlock -> {
                        bank.synchronizedBlockTransfer(fromAccount, toAccount, amount);
                    }
                    case lock -> {
                        bank.lockedTransfer(fromAccount, toAccount, amount);
                    }
                }
            }
        }
    }
}