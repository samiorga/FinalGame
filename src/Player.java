
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author branc2347
 */
public class Player extends Rectangle2D.Double {

    private double speed;
    private Color c = Color.GRAY;

    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = Game.defaultSpeed;
    }
    
    public void move(boolean w, boolean a, boolean s, boolean d) {
        if (w) {
            y -= this.speed;
        }
        if (s) {
            y += this.speed;
        }
        if (a) {
            x -= this.speed;
        }
        if (d) {
            x += this.speed;
        }
    }

    public void increaseWidth(double increment) {
        width += increment;
    }

    public void increaseHeight(double increment) {
        height += increment;
    }

    public void draw(Graphics2D g, double camx, double camy) {
        g.setColor(c);
        g.fillOval((int)(x-camx), (int)(y-camy), (int)width, (int)height);
//        g.draw(this);
        
    }
    public void playerSpeed(){
        double speedFactor = Game.playerWidth/this.width;
        this.speed = Game.defaultSpeed * speedFactor;
        System.out.println(this.speed);
    }
}
