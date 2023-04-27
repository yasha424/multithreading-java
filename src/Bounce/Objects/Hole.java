package Bounce.Objects;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Hole {
    private Component canvas;

    static public final int X_SIZE = 40;
    static public final int Y_SIZE = 40;

    private int x = 0;
    private int y = 0;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Hole(Component canvas, int x, int y) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.white);
        g2.fill(new Ellipse2D.Double(x, y, X_SIZE, Y_SIZE));

    }
}