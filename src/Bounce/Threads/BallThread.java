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
            while(!b.interactsWithHoles()) {
//                System.out.println("Thread name = " + Thread.currentThread().getName());

                b.move();
                Thread.sleep(1000 / 120);
            }

            BounceFrame.incrementBallsInHolesCounter();
            BounceFrame.updateBallsInHolesCounter();

            BallCanvas.remove(b);
            b.repaintCanvas();

        } catch (InterruptedException ex) {
            BallCanvas.remove(b);
            b.repaintCanvas();
            System.out.println(ex.getLocalizedMessage());
        }
    }
}