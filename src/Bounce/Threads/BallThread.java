package Bounce.Threads;

import Bounce.Objects.Ball;
import Bounce.Views.BallCanvas;
import Bounce.Views.BounceFrame;

public class BallThread extends Thread {
    private Ball b;

    public BallThread(Ball ball) {
        b = ball;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < 100000; i++) {
                b.move();

                if (b.interactsWithHole()) {
                    BounceFrame.ballsInHolesCounter++;
                    BounceFrame.updateBallsInHolesCounter();

                    this.interrupt();
                    BallCanvas.remove(b);
                    b.repaintCanvas();
                }

                Thread.sleep(1000 / 120);
            }
            BallCanvas.remove(b);
            b.repaintCanvas();
        } catch (InterruptedException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}