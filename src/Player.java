
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author branc2347
 */
public class Player extends Rectangle {

    private double dwidth, dheight;
    private double speed = 5;
    private Color c = Color.GRAY;

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);

        dwidth = width;
        dheight = height;
    }

    public void move(boolean w, boolean a, boolean s, boolean d) {
        if (w) {
            y -= speed;
        }
        if (s) {
            y += speed;
        }
        if (a) {
            x -= speed;
        }
        if (d) {
            x += speed;
        }
    }

    public void increaseWidth(double increment) {
        dwidth += increment;
        width = (int) dwidth;
    }

    public void increaseHeight(double increment) {
        dheight += increment;
        height = (int) dheight;
    }

    public void draw(Graphics2D g) {
        g.setColor(c);
        g.fillOval(x - Game.camx[0], y - Game.camy[0], width, height);
    }
}
