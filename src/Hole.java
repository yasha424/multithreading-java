import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Hole {
    private Component canvas;

    static final int X_SIZE = 20;
    static final int Y_SIZE = 20;

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
        g2.setColor(Color.green);
        g2.fill(new Ellipse2D.Double(x, y, X_SIZE, Y_SIZE));

    }
}