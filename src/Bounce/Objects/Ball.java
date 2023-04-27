package Bounce.Objects;

import Bounce.Views.BallCanvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Ellipse2D;

public class Ball {
    private Component canvas;

    private static final int X_SIZE = 20;
    private static final int Y_SIZE = 20;

    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;

    private Color color = Color.darkGray;

    public Ball(Component c) {
        this.canvas = c;

        if (Math.random() < 0.5) {
            x = new Random().nextInt(this.canvas.getWidth());
            y = 0;
        } else {
            x = 0;
            y = new Random().nextInt(this.canvas.getHeight());
        }
    }

    public Ball(Component c, int x, int y) {
        this.canvas = c;

        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        g2.fill(new Ellipse2D.Double(x, y, X_SIZE, Y_SIZE));
    }

    public void move() {
        x += dx;
        y += dy;

        if (x < 0) {
            x = 0;
            dx = -dx;
        }

        if (x + X_SIZE >= this.canvas.getWidth()) {
            x = this.canvas.getWidth() - X_SIZE;
            dx = -dx;
        }

        if (y < 0) {
            y = 0;
            dy = -dy;
        }

        if (y + Y_SIZE >= this.canvas.getHeight()) {
            y = this.canvas.getHeight() - Y_SIZE;
            dy = -dy;
        }
        repaintCanvas();
    }

    public void repaintCanvas() {
        this.canvas.repaint();
    }

    public boolean interactsWithHoles() {
        ArrayList<Hole> holes = BallCanvas.getHoles();

        for (int i = 0; i < holes.size(); i++) {
            if (interactsWithHole(holes.get(i))) {
                return true;
            }
        }

        return false;
    }

    private boolean interactsWithHole(Hole hole) {
        double centerX1 = this.x + this.Y_SIZE / 2;
        double centerY1 = this.y + this.X_SIZE / 2;
        double centerX2 = hole.getX() + hole.Y_SIZE / 2;
        double centerY2 = hole.getY() + hole.X_SIZE / 2;
        double radius1 = this.Y_SIZE / 2;
        double radius2 = hole.Y_SIZE / 2;

        double distanceBetweenCenters = Math.sqrt(
                (centerX1 - centerX2) * (centerX1 - centerX2) +
                (centerY1 - centerY2) * (centerY1 - centerY2)
        );

        return distanceBetweenCenters <= (radius1 + radius2);
    }

}