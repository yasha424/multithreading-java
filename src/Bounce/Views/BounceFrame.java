package Bounce.Views;

import Bounce.Objects.Ball;
import Bounce.Objects.Hole;
import Bounce.Threads.BallThread;
import Bounce.Threads.JoinBallThread;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.lang.Thread;

public class BounceFrame extends JFrame {
    private static int ballsInHolesCounter = 0;

    private BallCanvas canvas;

    public static final int WIDTH = 900;
    public static final int HEIGHT = 400;

    static private JLabel counterLabel;

    static synchronized public void incrementBallsInHolesCounter() {
        ballsInHolesCounter++;
    }

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce programm");
        this.canvas = new BallCanvas();
        this.canvas.setBackground(Color.green);

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        canvas.add(new Hole(canvas, 0 - Hole.X_SIZE / 2, 0 - Hole.Y_SIZE / 2));
        canvas.add(new Hole(canvas,  WIDTH / 2 - Hole.X_SIZE / 2, 0 - Hole.Y_SIZE / 2));
        canvas.add(new Hole(canvas, WIDTH - Hole.X_SIZE / 2, 0 - Hole.Y_SIZE / 2));
        canvas.add(new Hole(canvas, 0 - Hole.X_SIZE / 2, HEIGHT - Hole.Y_SIZE / 2 - 65));
        canvas.add(new Hole(canvas,  WIDTH / 2 - Hole.X_SIZE / 2, HEIGHT - Hole.Y_SIZE / 2 - 65));
        canvas.add(new Hole(canvas, WIDTH - Hole.X_SIZE / 2, HEIGHT - Hole.Y_SIZE / 2 - 65));


        this.counterLabel = new JLabel("Balls in holes: " + ballsInHolesCounter);

        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        JButton buttonAddRed = new JButton("Add Red");
        JButton buttonAddBlue = new JButton("Add Blue");
        JButton buttonAddMany = new JButton("Add many");
        JButton buttonJoinThread = new JButton("Join Thread");

        buttonStart.addActionListener(e -> {
            Ball b = new Ball(canvas);
            canvas.add(b);
            BallThread thread = new BallThread(b);
            thread.start();
            System.out.println("Thread name = " + thread.getName());
        });

        buttonStop.addActionListener(e -> System.exit(0));

        buttonAddRed.addActionListener(e -> {
            Ball b = new Ball(canvas);
            b.setColor(Color.red);
            canvas.add(b);
            BallThread thread = new BallThread(b);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
            System.out.println("Thread name = " +
                    thread.getName());
        });

        buttonAddBlue.addActionListener(e -> {
            Ball b = new Ball(canvas);
            b.setColor(Color.blue);
            canvas.add(b);
            BallThread thread = new BallThread(b);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
            System.out.println("Thread name = " +
                    thread.getName());
        });

        buttonAddMany.addActionListener(e -> {

            int x = new Random().nextInt(this.canvas.getWidth());
            int y = new Random().nextInt(this.canvas.getHeight());

            int size = 400;
            BallThread threads[] = new BallThread[size + 1];

            for (int i = 0; i < size; i++) {
                Ball b = new Ball(canvas, x, y);
//                b.setColor(Color.blue);
                canvas.add(b);
                BallThread thread = new BallThread(b);
                thread.setPriority(Thread.MIN_PRIORITY);
                threads[i] = thread;
//                thread.start();
            }

            Ball b = new Ball(canvas, x, y);
            b.setColor(Color.red);
            canvas.add(b);
            BallThread thread = new BallThread(b);
            thread.setPriority(Thread.MAX_PRIORITY);
            threads[size] = thread;

            for (int i = 0; i < size + 1; i++) {
                threads[i].start();
            }
        });

        buttonJoinThread.addActionListener(e -> {
            Ball b1 = new Ball(canvas);
            b1.setColor(Color.black);
            canvas.add(b1);
            BallThread thread1 = new BallThread(b1);

            Ball b2 = new Ball(canvas);
            b2.setColor(Color.black);
            canvas.add(b2);
            BallThread thread2 = new JoinBallThread(b2, thread1);

            thread1.start();
            thread2.start();
        });

        buttonPanel.add(this.counterLabel);
        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(buttonAddRed);
        buttonPanel.add(buttonAddBlue);
        buttonPanel.add(buttonAddMany);
        buttonPanel.add(buttonJoinThread);
        content.add(buttonPanel, BorderLayout.SOUTH);
    }

    static synchronized public void updateBallsInHolesCounter() {
        counterLabel.setText("Balls in holes: " + ballsInHolesCounter);
    }

}