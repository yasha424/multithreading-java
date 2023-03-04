public class JoinBallThread extends BallThread {
    private BallThread parentThread;

    public JoinBallThread(Ball b, BallThread parentThread) {
        super(b);
        this.parentThread = parentThread;
    }

    @Override
    public void run() {
        try {
            parentThread.join();
            super.run();
        }
        catch (InterruptedException e) {
//            super.interrupt();
        }
    }
}
