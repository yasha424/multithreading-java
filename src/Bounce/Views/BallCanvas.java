package Bounce.Views;

import Bounce.Objects.Ball;
import Bounce.Objects.Hole;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    static private ArrayList<Ball> balls = new ArrayList<Ball>();
    static private ArrayList<Hole> holes = new ArrayList<Hole>();

    static public void add(Ball b) {
        balls.add(b);
    }

    static public void add(Hole hole) {
        holes.add(hole);
    }

    static public ArrayList<Hole> getHoles() {
        return holes;
    }

    static synchronized public void remove(Ball b) {
        balls.remove(b);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).draw(g2);
        }

        for (int i = 0; i < holes.size(); i++) {
            holes.get(i).draw(g2);
        }
    }
}