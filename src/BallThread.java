public class BallThread extends Thread {
    private Ball b;

    public BallThread(Ball ball) {
        b = ball;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < 1000; i++) {
                b.move();
                System.out.println("Thread name = " + Thread.currentThread().getName());

                if (b.interactsWithHole()) {
                    BounceFrame.ballsInHolesCounter++;
                    BounceFrame.updateBallsInHolesCounter();

                    BallCanvas.remove(b);
                    b.repaintCanvas();

                    this.interrupt();
                }

                Thread.sleep(1000 / 120);
            }
            BallCanvas.remove(b);
            b.repaintCanvas();
        } catch (InterruptedException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
        }
    }
}