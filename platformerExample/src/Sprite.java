import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Sprite {
    private final PApplet sketch;
    PImage img;
    float center_x, center_y;
    float change_x, change_y;
    float width, height;
    float scale;


    public Sprite(PApplet sketch, PImage img, float center_x, float center_y, float change_x, float change_y, float scale) {
        this.sketch = sketch;
        this.img = img;
        this.center_x = center_x;
        this.center_y = center_y;
        this.change_x = change_x;
        this.change_y = change_y;
        this.scale = scale;
        width = img.width * scale;
        height = img.height * scale;
    }

    public Sprite(PApplet sketch, ArrayList<PImage> img, float scale){
        this.sketch = sketch;
        this.img = img.get(0);
        this.center_x = 0;
        this.center_y = 0;
        this.change_x = 0;
        this.change_y = 0;
        this.scale = scale;
        width = img.get(0).width * scale;
        height = img.get(0).height * scale;
    }

    public void display(){
        sketch.image(img, center_x, center_y, width, height);
    }

    public void update(){
        center_x = center_x + change_x;
        center_y = center_y + change_y;
    }

    public float getCenter_x() {
        return center_x;
    }

    public void setCenter_x(float center_x) {
        this.center_x = center_x;
    }

    public float getCenter_y() {
        return center_y;
    }

    public void setCenter_y(float center_y) {
        this.center_y = center_y;
    }

    public float getChange_x() {
        return change_x;
    }

    public void setChange_x(float change_x) {
        this.change_x = change_x;
    }

    public float getChange_y() {
        return change_y;
    }

    public void setChange_y(float change_y) {
        this.change_y = change_y;
    }


    public float getLeft(){
        return center_x - width/2;
    }

    public float getRight(){
        return center_x + width/2;
    }

    public float getTop(){
        return center_y - height/2;
    }

    public float getBottom(){
        return center_y + height/2;
    }

    public void setLeft(float newLeft){
        center_x = newLeft + width /2;
    }

    public void setRight(float newRight){
        center_x = newRight - width / 2;
    }

    public void setTop(float newTop){
        center_y = newTop + height/2;
    }

    public void setBottom(float newBottom){
        center_y = newBottom - height/2;
    }



}
