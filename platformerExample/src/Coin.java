import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Coin extends AnimatedSprite{
    public Coin(PApplet sketch, ArrayList<PImage> img, float scale){
        super(sketch, img, scale);
        standNeutral = img.toArray(new PImage[0]);
        currentImages = img.toArray(new PImage[0]);
    }
}
