
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author
 * branc2347
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

    public void draw(Graphics2D g, double camx, double camy) {
        g.setColor(Color.BLACK);
        g.fillOval((int) (x - camx), (int) (y - camy), (int) width, (int) height);
    }

    public void playerSpeed() {
        double speedFactor = Game.playerWidth / this.width;
        double remainder = speedFactor - (int)speedFactor;
        if(remainder == 0.1 || remainder == 0.2 || remainder == 0.25 ||remainder == 0.5){
            this.speed = Game.defaultSpeed * speedFactor;
        }
        //logic for camera zoom out as mass increases
            Game.zoomFactor = Game.defaultZoom * speedFactor;
        System.out.println(this.width + "  " + speedFactor + "    "+Game.zoomFactor);
//        this.speed =1 ;
    }
}
