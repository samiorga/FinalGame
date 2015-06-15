/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
 * @author
 * branc2347
 */
// make sure you rename this class if you are doing a copy/paste
public class Game extends JComponent implements KeyListener {

    // Height and Width of game
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //movement variables
    boolean p1Up = false; // up 
    boolean p1Down = false; // down
    boolean p1Right = false; // right
    boolean p1Left = false; // left
    //size boost variables.. debug mode
    boolean p1Add = false;
    boolean p1Minus = false;
    boolean p2Add = false;
    boolean p2Minus = false;
    //player speed 
    // character variables
    static final double playerWidth = 20;
    static final int defaultSpeed = 4; // player speed
    Player player1 = new Player(400, 400, playerWidth, playerWidth);
    Player player2 = new Player(300, 300, playerWidth, playerWidth);
    // Menu, Logic, Death - screens
    int screen = 0;
    final int startScreen = 0;
    final int gameScreen = 1;
    final int endScreen = 2;
    boolean enterPressed = false;
    public String playerName = "";
    Font playerFont = new Font(null, Font.BOLD, 20);
    int textScroll= 0;
    // Random for objects
    Random random = new Random(); // used for generating food, player position
    //food gen variables
    public static int entireWidth = 10000; // actual width of the game (not the window)
    public static int entireHeight = 7500;// actual heigh of the game (not the window)
    int amountFood = 20000; // amount of food to be generated
    Food[] food = new Food[amountFood]; //array storing all food created
    final int foodWidth = 4; //used for both width and height because it's a circle
    private int[] foodTimer = new int[amountFood];
    //camera correction + zoom
    static final double defaultZoom = 4;
    static double zoomFactor = defaultZoom; // factor to
    static double camWidth = WIDTH / zoomFactor;
    static double camHeight = HEIGHT / zoomFactor;
    double camx = (player1.getCenterX() - camWidth / 2.0);
    double camy = (player1.getCenterY() - camHeight / 2.0);

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // always clear the screen first!
        g2.clearRect(0, 0, entireWidth, entireHeight);
        if (screen == startScreen) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, WIDTH, HEIGHT);
            g2.setColor(Color.WHITE);
            g2.setFont(playerFont);
            drawCenteredString(g2, "Please enter your player name:", WIDTH/2,WIDTH/4, HEIGHT/2);
            drawCenteredString(g2, playerName, WIDTH/2, WIDTH/4, HEIGHT/2+30);
            
        } else if (screen == gameScreen) {
            //scale or zoom in
            g2.scale(zoomFactor, zoomFactor);

            // GAME DRAWING GOES HERE
            player2.draw(g2, camx, camy, Player.russianPlayer, "");
            player1.draw(g2, camx, camy, Player.obamaPlayer, playerName);
            for (Food f : food) {
                f.draw(g2, camx, camy);
            }
        } else if (screen == endScreen) {
            g2.clearRect(0, 0, entireWidth, entireHeight);
            g2.setFont(playerFont);
            g2.drawString("THANKS OBAMA ...", (int)camWidth + textScroll, (int)camHeight/2);
        }


        // GAME DRAWING ENDS HERE
    }
private void drawCenteredString(Graphics2D g2, String string, int width, int XPos, int YPos){
        int stringLen = (int)
            g2.getFontMetrics().getStringBounds(string, g2).getWidth();
        int start = width/2 - stringLen/2;
        g2.drawString(string, start + XPos, YPos);
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

            if (screen == startScreen) {
            } else if (screen == gameScreen) {
                gameLogic();
            } else if (screen == endScreen) {
                textScroll += 1;
            }
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

    void gameLogic() {
        // GAME LOGIC STARTS HERE 
        
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
        if (player2.width < 0 || player2.height < 0) {
            player2.width = 0;
            player2.height = 0;
        }
        if (player1.width < 1 || player1.height < 1) {
            player1.width = 0;
            player1.height = 0;
        }
        handleCollisionPlayer(player1, player2);
        handleCollisionFood(player1, food);
        handleCollisionFood(player2, food);
        foodRespawn(player1, food);
        player1.playerSpeed();
        player1.move(p1Up, p1Left, p1Down, p1Right);
        camWidth = WIDTH / zoomFactor;
        camHeight = HEIGHT / zoomFactor;
        camx = (player1.getCenterX() - camWidth / 2.0);
        camy = (player1.getCenterY() - camHeight / 2.0);
        if(player1.width < playerWidth){
            screen = 2;
        }
        // GAME LOGIC ENDS HERE 
    }

    public void foodRespawn(Player player, Rectangle[] food) {

        for (int i = 0; i < food.length; i++) {

            if (!player.contains(food[i].x, food[i].y) && foodTimer[i] < 1) {
                food[i].width = foodWidth;
                food[i].height = foodWidth;
                foodTimer[i] = 15 * 60;
            }
            if (food[i].width < 0) {
                if (foodTimer[i] > 0) {
                    foodTimer[i]--;
                }
            }
        }
    }

    public void handleCollisionPlayer(Player p1, Player p2) {
        int xDiff = (int) (p1.getCenterX() - p2.getCenterX());
        int yDiff = (int) (p1.getCenterY() - p2.getCenterY());
        double p1Radius = p1.width / 2;
        double p2Radius = p2.width / 2;
        double centerDiff = (p1Radius + p2Radius);

        if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p2Radius
                || Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p1Radius) {
            if (0.9 * p1.width >= p2.width) {
                if (p2.height > -1 && p2.width > -1) {
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
            double p1Radius = player.width / 2;
            int p2Radius = f.width / 2;
            double centerDiff = (p1Radius + p2Radius);
            if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p2Radius && f.width > 0) {
                //System.out.println(player.width);
                player.width += f.width / 2;
                player.x -= f.width / 4;
                player.height += f.height / 2;
                player.y -= f.height / 4;
                f.height -= 2 * f.height;
                f.width -= 2 * f.width;
                repaint();
            }
        }
    }

    /**
     * @param
     * args
     * the
     * command
     * line
     * arguments
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
        
        if (screen == startScreen) { 
            if (e.getKeyChar() >= 'A' && e.getKeyChar() <= 'z' || e.getKeyChar() == ' ') {
                playerName += e.getKeyChar(); // Typing player name
            }
            else if(key == KeyEvent.VK_BACK_SPACE){
                playerName = playerName.substring(0, playerName.length()-1);
                System.out.println(playerName);
            }
            else if(key == KeyEvent.VK_ENTER){
                screen = 1;
            }
        }
        else if(screen == gameScreen){
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
        else if( screen == endScreen){
            if(key == KeyEvent.VK_SPACE){
                screen = 0;
            }
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
            food[i] = new Food(1, 1, foodWidth, foodWidth);
            food[i].x = random.nextInt(Game.entireWidth - foodWidth) + 1;
            food[i].y = random.nextInt(Game.entireHeight - foodWidth) + 1;
            foodTimer[i] = 15 * 60;
        }
    }
}
