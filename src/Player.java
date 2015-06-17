
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author branc2347
 */
public class Player extends Rectangle2D.Double {

    private double speed; // player speed
    Font nameFont = new Font(null, Font.BOLD, 10); // player name font
    //images
    public static BufferedImage russianPlayer = ImageHelper.loadImage("russianPlayer.png"); // p2 , Test player skin
    public static BufferedImage obamaPlayer = ImageHelper.loadImage("obamaPlayer.png"); // p1 skin

    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = Game.defaultSpeed;
    }

    public void move(boolean w, boolean a, boolean s, boolean d) {
        // movement : uses the booleans from the keys pressed to move player
        if (w) {
            y -= this.speed; // up
        }
        if (s) {
            y += this.speed; // down
        }
        if (a) {
            x -= this.speed; // left
        }
        if (d) {
            x += this.speed; // right
        }
    }

    public void draw(Graphics2D g, double camx, double camy, BufferedImage playerStyle, String playerName) {
        // draws the player in accordance to the camera
        // draws string: player name
        g.drawImage(playerStyle, (int) (x - camx), (int) (y - camy), (int) width, (int) height, null);
        g.setFont(nameFont);
        g.drawString(playerName, (int) (x - camx) - 10, (int) (this.getCenterY() - camy) - 10);
    }

    public void scaleFactor() {
        // scaling speed to width&height
        double speedFactor = Game.playerWidth / this.width;
        double remainder = speedFactor - (int) speedFactor;
        // to make sure the game is not clippy and jittery
        if (remainder == 0.1 || remainder == 0.2 || remainder == 0.25 || remainder == 0.5) {
            this.speed = Game.defaultSpeed * speedFactor;
        }
        //logic for camera zoom out as mass increases.. it uses the same variable as speed
        // called 'easing' or tweaning
        Game.zoomFactor = Game.defaultZoom * speedFactor; // zoom factor according to size 
        //zoomFactor for player name, to make it constant in comparison to player size. does not work well 
        double zoomFactor = 10 / speedFactor;
        nameFont = nameFont.deriveFont((float) zoomFactor + 10);
    }
}
