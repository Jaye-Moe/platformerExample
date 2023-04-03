import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Player extends AnimatedSprite{
    private int lives;
    boolean onPlatform, inPlace;
    private final GameEngine gameEngine;
    private final ArrayList<Sprite> platforms;
    private final ArrayList<PImage> playerMoveLeft;
    private final ArrayList<PImage> playerMoveRight;
    private final ArrayList<PImage> playerStandLeft;
    private final ArrayList<PImage> playerStandRight;
    private final ArrayList<PImage> playerJumpLeft;
    private final ArrayList<PImage> playerJumpRight;


    public Player(ArrayList<Sprite> platforms,GameEngine gameEngine,PApplet sketch, ArrayList<PImage> playerMoveLeft, ArrayList<PImage> playerMoveRight, ArrayList<PImage> playerStandLeft,
                  ArrayList<PImage> playerStandRight,ArrayList<PImage> playerJumpLeft, ArrayList<PImage> playerJumpRight,float scale){
        super(sketch, playerStandRight, scale);
        this.gameEngine = gameEngine;
        this.platforms = platforms;
        lives = 3;
        direction = GameEngine.RIGHT_FACING;
        onPlatform = false;
        inPlace = true;
        standNeutral = playerStandRight.toArray(new PImage[0]);
        currentImages = playerStandRight.toArray(new PImage[0]);
        this.moveLeft = playerMoveLeft.toArray(new PImage[0]);
        this.moveRight = playerMoveRight.toArray(new PImage[0]);

        this.playerMoveLeft = playerMoveLeft;
        this.playerMoveRight = playerMoveRight;
        this.playerStandLeft = playerStandLeft;
        this.playerStandRight = playerStandRight;
        this.playerJumpLeft = playerJumpLeft;
        this.playerJumpRight = playerJumpRight;
    }

    @Override
    public void updateAnimation(){
        onPlatform = gameEngine.isOnPlatforms(this, platforms);
        inPlace = change_x ==0 && change_y ==0;
        super.updateAnimation();
    }

    @Override
    public void selectDirection(){
        if(change_x > 0){
            direction = GameEngine.RIGHT_FACING;
        }else if(change_x < 0){
            direction = GameEngine.LEFT_FACING;
        }
    }

    @Override
    public void selectCurrentImages() {
        if (direction == GameEngine.RIGHT_FACING) {
            if (inPlace) {
                currentImages = playerStandRight.toArray(new PImage[0]);
            } else if (!onPlatform) {
                currentImages = playerJumpRight.toArray(new PImage[0]);

            } else {
                currentImages = moveRight;
            }
        } else if (direction == GameEngine.LEFT_FACING) {
            if (inPlace) {
                currentImages = playerStandLeft.toArray(new PImage[0]);
            } else if (!onPlatform) {
                currentImages = playerJumpLeft.toArray(new PImage[0]);
            } else {
                currentImages = moveLeft;
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
