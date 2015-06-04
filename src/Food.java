
import java.awt.Rectangle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Caius
 */
public class Food extends Rectangle{
    
    public Food(int x, int y, int width, int height){
        super(x,y,width,height);
    }
//    public void handleCollision2(Player player, Rectangle[] food)
//    {
//        for(int i =0; i < food.length; i++){
//        int xDiff = (int) (player.getCenterX() - food[i].getCenterX());
//        int yDiff = (int) (player.getCenterY() - food[i].getCenterY());
//        int p1Radius = player.width / 2;
//        int p2Radius = food[i].width / 2;
//        double centerDiff = (p1Radius + p2Radius);
//        if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) <= centerDiff - p2Radius)
//        {
//            if (food[i].height > -1 && food[i].width > -1) {
//                    System.out.println(player.width);
//                    player.width ++;
//                    player.height ++;
//                    food[i].height -= 2;
//                    food[i].width -= 2;
//                }
//        }
//        }
//    }
}
