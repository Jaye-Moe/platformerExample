import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class AnimatedSprite extends Sprite{
    PImage[] currentImages;
    PImage[] standNeutral;
    PImage[] moveLeft;
    PImage[] moveRight;
    int direction;
    int index;
    int frame;

    public AnimatedSprite(PApplet sketch, ArrayList<PImage> img, float scale){
        super(sketch, img, scale);
        direction = GameEngine.NEUTRAL_FACING;
        index = 0;
        frame = 0;
    }
    public void updateAnimation(){
        frame++;
        if(frame %5 ==0){   //UPDATE IMAGE EVERY FIVE FRAMES TO AVOID LOOKING GLITCHY
            selectDirection();
            selectCurrentImages();
            advanceToNextImage();
        }
    }

    public void selectDirection(){
        if(change_x >0){
            direction = GameEngine.RIGHT_FACING;
        } else if (change_x<0) {
            direction = GameEngine.LEFT_FACING;
        }else{
            direction = GameEngine.NEUTRAL_FACING;
        }
    }

    public void selectCurrentImages(){
        if(direction== GameEngine.RIGHT_FACING){
            currentImages=moveRight;
        } else if (direction==GameEngine.LEFT_FACING) {
            currentImages=moveLeft;
        }else{
            currentImages=standNeutral;
        }
    }

    public void advanceToNextImage(){
        index++;
        if(index>=currentImages.length){
            index = 0;
        }
        img = currentImages[index];
    }

}
