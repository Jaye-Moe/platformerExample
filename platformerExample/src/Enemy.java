import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Enemy extends AnimatedSprite {
    float boundaryLeft, boundaryRight;

    public Enemy (PApplet sketch, ArrayList<PImage>moveLeft,ArrayList<PImage> moveRight, float scale, float boundaryLeft, float boundaryRight){
        super(sketch, moveRight, 1);

            direction = GameEngine.RIGHT_FACING;
            this.boundaryLeft = boundaryLeft;
            this.boundaryRight = boundaryRight;
            standNeutral = moveLeft.toArray(new PImage[0]);
            currentImages = moveLeft.toArray(new PImage[0]);
            this.moveLeft = moveLeft.toArray(new PImage[0]);
            this.moveRight = moveRight.toArray(new PImage[0]);
            change_x = 2;
    }

    public void update(){
        super.update();
        if(getLeft()<= boundaryLeft){
            setLeft(boundaryLeft);
            change_x = change_x * -1;
        }else if (getRight()>=boundaryRight){
            setRight(boundaryRight);
            change_x = change_x * -1;
        }

    }

}


