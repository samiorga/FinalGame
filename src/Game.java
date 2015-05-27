/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 *
 * @author branc2347
 */
// make sure you rename this class if you are doing a copy/paste
public class Game extends JComponent implements KeyListener {

    // Height and Width of our game
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //movement variables
    boolean p1Up = false;
    boolean p1Down = false;
    boolean p1Right = false;
    boolean p1Left = false;
    //size boost variables
    boolean p1Add = false;
    boolean p1Minus = false;
    boolean p2Add = false;
    boolean p2Minus = false;
    //player speed 
    int speed = 5;
    // character variables
    Player player1 = new Player(500, 300, 60, 60);
    Player player2 = new Player(300, 300, 50, 50);
    int width = player1.width;
    int height = player1.height;
    // Random for objects
    Random random = new Random();
    boolean drawn = false;
    //food gen
    int entireWidth = 5000;
    int entireHeight = 3750;
    int amountFood = 10000;
    Rectangle[] food = new Rectangle[amountFood];
    int foodWidth = 2; //used for both width and height because it's a circle
    int timer = 5 * 60; // 5 second delay
    //camera correction + zoom
    double zoomFactor = 1;
    CameraCorrection camera = new CameraCorrection();
    public static int[] camx = new int[3];
    public static int[] camy = new int[3];

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // always clear the screen first!
        g2.clearRect(0, 0, entireWidth, entireHeight);
        camera.playerCameraCorrection(player1, 0);
        //scale or zoom in
        g2.translate(camx[0], -camy[0]);
        g2.scale(zoomFactor, zoomFactor);

        // GAME DRAWING GOES HERE
        camx[2] = 0;
        camy[2] = 0;
        for (int i = 0; i < amountFood; i++) {
            g2.fillOval(food[i].x, food[i].y, food[i].width, food[i].height);
        }
        
//        camera.playerCameraCorrection(player2, 1);

        player1.draw(g2);
        player2.draw(g2);

        // GAME DRAWING ENDS HERE
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        // the main game loop section
        // game will end if you set done = false;
        genFood(amountFood);

        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            player1.move(p1Up, p1Left, p1Down, p1Right);
            if (p1Add) {
                player1.width += 2;
                player1.height += 2;
                player1.x -= 1;
                player1.y -= 1;

            }
            if (p1Minus) {
                player1.width -= 2;
                player1.height -= 2;
                player1.x += 1;
                player1.y += 1;
            }
            if (p2Add) {
                player2.width += 2;
                player2.height += 2;
                player2.x -= 1;
                player2.y -= 1;
            }
            if (p2Minus) {
                player2.width -= 2;
                player2.height -= 2;
                player2.x += 1;
                player2.y += 1;
            }

            handleCollisionPlayer(player1, player2);
            handleCollisionFood(player1, food);
            handleCollisionFood(player2, food);
            if (timer > 0) {
                timer--;
            } else // timer hit 0
            {
                foodRespawn(player1, food);
            }
            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if (deltaTime > desiredTime) {
                //took too much time, don't wait
            } else {
                try {
                    Thread.sleep(desiredTime - deltaTime);
                } catch (Exception e) {
                };
            }
        }
    }

    public void foodRespawn(Player player, Rectangle[] food) {

        timer = 5 * 60; // reset timer

        for (int i = 0; i < food.length; i++) {
            if (food[i].width < 0) {
                if (!player.contains(food[i].x, food[i].y)) {
                    food[i].width = foodWidth;
                    food[i].height = foodWidth;

                }
            }
        }
    }

    public void handleCollisionPlayer(Player p1, Player p2) {
        int xDiff = (int) (p1.getCenterX() - p2.getCenterX());
        int yDiff = (int) (p1.getCenterY() - p2.getCenterY());
        int p1Radius = p1.width / 2;
        int p2Radius = p2.width / 2;
        double centerDiff = (p1Radius + p2Radius);

        if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p2Radius
                || Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p1Radius) {
            if (0.9 * p1.width >= p2.width) {
                if (p2.height > -1 && p2.width > -1) {
                    System.out.println(p1.width);
                    p1.width += 2;
                    p1.x -= 1;
                    p1.height += 2;
                    p1.y -= 1;
                    p2.height -= 4;
                    p2.y += 2;
                    p2.width -= 4;
                    p2.x += 2;
                }
            } else if (0.9 * p2.width >= p1.width) {
                if (p1.height > -1 && p1.width > -1) {
                    p2.width += 2;
                    p2.x -= 1;
                    p2.height += 2;
                    p2.y -= 1;
                    p1.height -= 4;
                    p1.y += 2;
                    p1.width -= 4;
                    p1.x += 2;
                }
            }
        }

    }

    public void handleCollisionFood(Player player, Rectangle[] food) {
        for (Rectangle f : food) {
            int xDiff = (int) (player.getCenterX() - f.getCenterX());
            int yDiff = (int) (player.getCenterY() - f.getCenterY());
            int p1Radius = player.width / 2;
            int p2Radius = f.width / 2;
            double centerDiff = (p1Radius + p2Radius);
            if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p2Radius && f.width > 0) {
                System.out.println(player.width);
                player.width += f.width;
                player.x -= f.width / 2;
                player.height += f.height;
                player.y -= f.height / 2;
                f.height -= 2 * f.height;
                f.x += f.height;
                f.width -= 2 * f.width;
                f.y += f.width;
                repaint();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");

        // creates an instance of my game
        Game game = new Game();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            p1Up = true;
        }
        if (key == KeyEvent.VK_S) {
            p1Down = true;
        }
        if (key == KeyEvent.VK_A) {
            p1Left = true;
        }
        if (key == KeyEvent.VK_D) {
            p1Right = true;
        }
        if (key == KeyEvent.VK_P) {
            p1Add = true;
        }
        if (key == KeyEvent.VK_O) {
            p1Minus = true;
        }
        if (key == KeyEvent.VK_L) {
            p2Add = true;
        }
        if (key == KeyEvent.VK_K) {
            p2Minus = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            p1Up = false;
        }
        if (key == KeyEvent.VK_S) {
            p1Down = false;
        }
        if (key == KeyEvent.VK_A) {
            p1Left = false;
        }
        if (key == KeyEvent.VK_D) {
            p1Right = false;
        }
        if (key == KeyEvent.VK_P) {
            p1Add = false;
        }
        if (key == KeyEvent.VK_O) {
            p1Minus = false;
        }
        if (key == KeyEvent.VK_L) {
            p2Add = false;
        }
        if (key == KeyEvent.VK_K) {
            p2Minus = false;
        }

    }

    public void genFood(int amountFood) {
        for (int i = 0; i < amountFood; i++) {
            food[i] = new Rectangle(1, 1, foodWidth, foodWidth);
            food[i].x = random.nextInt(entireWidth - foodWidth) + 1;
            food[i].y = random.nextInt(entireHeight - foodWidth) + 1;
        }
    }
}
