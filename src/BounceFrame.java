import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;

public class BounceFrame extends JFrame {
    static int ballsInHolesCounter = 0;

    private BallCanvas canvas;

    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;

    static private JLabel counterLabel;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce programm");
        this.canvas = new BallCanvas();

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        canvas.add(new Hole(canvas, 10, 10));

        this.counterLabel = new JLabel("Balls in holes: " + ballsInHolesCounter);

        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");

        buttonStart.addActionListener(e -> {
            Ball b = new Ball(canvas);
            canvas.add(b);
            BallThread thread = new BallThread(b);
            thread.start();
            System.out.println("Thread name = " +
                    thread.getName());
        });

        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(this.counterLabel);
        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        content.add(buttonPanel, BorderLayout.SOUTH);
    }

    static public void updateBallsInHolesCounter() {
        counterLabel.setText("Balls in holes: " + ballsInHolesCounter);
    }

}