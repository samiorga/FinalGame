
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Caius
 */
public class Food extends Rectangle {

    private Color foodColour; // food colour - i hate american spelling pls stop netbeans
    Random random = new Random(); // random used for gen colours

    public Food(int x, int y, int width, int height) {
        super(x, y, width, height);
        //create an array of colours, and randomly assign every food a colour from that array
        Color[] colours = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW, Color.WHITE, Color.RED};
        foodColour = colours[random.nextInt(colours.length)];
    }

    public void draw(Graphics2D g2, double camx, double camy) {
        //draw food based on camx, camy
        //set random food colour
        g2.setColor(foodColour);
        g2.fillOval(x - (int) camx, y - (int) camy, width, height);

    }
}
