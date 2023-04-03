import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.util.ArrayList;

public class GameEngine extends PApplet {
    private final ArrayList<Player> sprites;
    private final ArrayList<Sprite> platforms;
    private final ArrayList<Sprite> lavaImages;
    private final ArrayList<Sprite> coins;
    private final ArrayList<Sprite> spiders;
    private final ArrayList<PImage> coinImages;
    private final ArrayList<PImage> enemyMoveLeft;
    private final ArrayList<PImage> enemyMoveRight;
    private final ArrayList<PImage> playerStandLeft;
    private final ArrayList<PImage> playerStandRight;
    private final ArrayList<PImage> playerMoveLeft;
    private final ArrayList<PImage> playerMoveRight;
    private final ArrayList<PImage> playerJumpLeft;
    private final ArrayList<PImage> playerJumpRight;
    float view_x = 0;
    float view_y= 0;
    static float MOVE_SPEED = 5;
    final static  float PLAYER_SCALE = (float) (90.0/110);
    final static float SPRITE_SCALE = (float) (50.0/128);
    final static float SPRITE_SIZE = 50;
    final static float GRAVITY = (float) (.6);
    final static float JUMP_SPEED = 14;
    final static float RIGHT_MARGIN = 400;
    final static float LEFT_MARGIN = 180;
    final static float VERTICAL_MARGIN = 40;
    final static int NEUTRAL_FACING = 0;
    final static int RIGHT_FACING = 1;
    final static int LEFT_FACING = 2;
    final static float HEIGHT = SPRITE_SIZE * 28; //600 27 tiles tall, change to 26 to die on lava
    final static float GROUND_LEVEL = HEIGHT - SPRITE_SIZE;
    private boolean isGameOver = false;
    private  float playerStartX = 0;
    private float playerStartY = 0;
    private boolean gameWon = false;
    private boolean playerDied = false;
    int score;
    PFont f;
    public GameEngine(){
        sprites = new ArrayList<>();
        platforms = new ArrayList<>();
        coins = new ArrayList<>();
        coinImages = new ArrayList<>();
        enemyMoveLeft = new ArrayList<>();
        enemyMoveRight = new ArrayList<>();
        lavaImages = new ArrayList<>();
        spiders = new ArrayList<>();
        playerStandRight = new ArrayList<>();
        playerStandLeft = new ArrayList<>();
        playerMoveRight = new ArrayList<>();
        playerMoveLeft = new ArrayList<>();
        playerJumpLeft = new ArrayList<>();
        playerJumpRight = new ArrayList<>();
        score = 0;
    }


    public void setup(){
        f = createFont("Arial",32,true);
    }

    public void settings(){
        size(800,600);
        createPlatforms("data/map.csv");
        isGameOver = false;
        gameWon = false;

    }


    public void draw(){
        background(255);
        imageMode(CENTER);
        scroll();
        displayAll();
        if(!isGameOver){
            try {
                updateAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else if(gameWon){
            text("SCORE: " + score+ "     LIVES: " + sprites.get(0).getLives() +"     YOU WIN.  \nPRESS ENTER TO RESTART" ,view_x,view_y+30);
        }else{
            text("SCORE: " + score+ "     LIVES: " + sprites.get(0).getLives() +"     GAME OVER.  \nPRESS ENTER TO RESTART" ,view_x,view_y+30);
        }
    }

    public void displayAll(){
        textFont(f,32);                  // STEP 3 Specify font to be used
        fill(255,0,0);                         // STEP 4 Specify font color
        for(Player s: sprites){ //Actually only player for now
            s.display();
        }
        for(Sprite p: platforms){
            p.display();
        }
        for(Sprite c: coins){
            c.display();
        }
        for(Sprite s: spiders){
            s.display();
        }
        for(Sprite l: lavaImages){
            l.display();
        }
    }

    public void updateAll() throws InterruptedException {
        for(Player s: sprites){ //Actually only player for now
            ((AnimatedSprite)s).updateAnimation();
            resolvePlatformCollisions(s, platforms);
            checkForCollectingCoin(s, coins);
            checkDeath();
        }
        for(Sprite c: coins){
            ((AnimatedSprite)c).updateAnimation();
        }
        for(Sprite s: spiders){
            s.update();
            ((AnimatedSprite)s).updateAnimation();
        }
        text("SCORE: " + score+ "     LIVES: " + sprites.get(0).getLives() ,view_x,view_y+30);

    }




    public void checkForCollectingCoin(Sprite s, ArrayList<Sprite> coins){
        ArrayList<Sprite> collisionList = checkCollisionList(s,coins);
        if (collisionList.size() > 0) {
            int numberOfCoins = coins.size();
            int i = 0;
            while (i < numberOfCoins){
                if (collisionList.contains(coins.get(i))) {
                    coins.remove(i);
                    numberOfCoins = coins.size();
                    score++;
                }
                i++;
            }
        }
        if(coins.size() == 0){
            isGameOver = true;
            gameWon = true;
            text("SCORE: " + score+ "     LIVES: " + sprites.get(0).getLives() +"     YOU WIN.  \nPRESS ENTER TO RESTART" ,view_x,view_y+30);
        }
    }



    public void checkDeath() throws InterruptedException {

        boolean collideWithEnemy = checkCollision(sprites.get(0), spiders.get(0)); //TODO: Only works for one spider, update for multiple
        boolean fallOffCliff = sprites.get(0).getBottom() > GROUND_LEVEL;

        if(collideWithEnemy || fallOffCliff){
            sprites.get(0).setLives(sprites.get(0).getLives() -1);
            if(sprites.get(0).getLives() == 0){
                isGameOver = true;
            }else{
                playerDied = true;
                sprites.get(0).setBottom(GROUND_LEVEL);
                Thread.sleep(2000);
                sprites.get(0).setCenter_x(playerStartX);
                sprites.get(0).setCenter_y(playerStartY);
                playerDied = false;
                }

            }

    }



    public void scroll(){
        float right_boundary = view_x + width - RIGHT_MARGIN;
        if(sprites.get(0).getRight() > right_boundary){
            view_x = view_x + sprites.get(0).getRight() - right_boundary;
        }
        float left_boundary = view_x + LEFT_MARGIN;
        if(sprites.get(0).getLeft() < left_boundary){
            view_x = view_x -left_boundary + sprites.get(0).getLeft();
        }
        float bottom_boundary = view_y + height - VERTICAL_MARGIN;
        if(sprites.get(0).getBottom() > bottom_boundary){
            view_y = view_y + sprites.get(0).getBottom() - bottom_boundary;
        }
        float top_boundary = view_y + VERTICAL_MARGIN;
        if(sprites.get(0).getTop() < top_boundary){
            view_y = view_y - top_boundary - sprites.get(0).getTop();
        }
        translate(-view_x, - view_y);
        text("SCORE: " + score+ "     LIVES: " + sprites.get(0).getLives() ,view_x,view_y+30);
    }

    public boolean isOnPlatforms(Sprite s, ArrayList<Sprite> walls){
        s.setCenter_y(s.getCenter_y()+5);
        ArrayList<Sprite> collisionList = checkCollisionList(s, walls);
        s.setCenter_y(s.getCenter_y()-5);
        return collisionList.size() > 0;
    }

    public void resolvePlatformCollisions(Sprite s, ArrayList<Sprite> walls){
        s.setChange_y(s.getChange_y() + GRAVITY);
        //MOVE IN THE Y DIRECTION FIRST AND RESOLVE ANY COLLISIONS
        s.setCenter_y(s.getCenter_y() + s.getChange_y());
        ArrayList<Sprite> collisionList = checkCollisionList(s,walls);
        if(collisionList.size()>0){
            Sprite collided = collisionList.get(0); //GET THE FIRST PLATFORM COLLIDED WITH
            if(s.change_y>0){   //CHANGE IN Y > 0 MEANS FALLING DOWN
                s.setBottom(collided.getTop());     //SET BOTTOM TO TOP OF ITEM COLLIDED WITH
            }else if(s.change_y<0){ //CHANGE IN Y < 0 MEANS JUMPING
                s.setTop(collided.getBottom());
            }
            s.setChange_y(0);
        }

        //MOVE IN THE X DIRECTION NEXT AND RESOLVE ANY COLLISIONS
        s.setCenter_x(s.getCenter_x() + s.getChange_x());
        collisionList = checkCollisionList(s,walls);
        if(collisionList.size()>0){
            Sprite collided = collisionList.get(0); //GET THE FIRST PLATFORM COLLIDED WITH
            if(s.change_x>0){   //CHANGE IN X > 0 MEANS GOING RIGHT
                s.setRight(collided.getLeft());
            }else if(s.change_x<0){ //CHANGE IN Y < 0 GOING LEFT
                s.setLeft(collided.getRight());
            }
//            s.setChange_x(0);   //TODO REMOVE????  MIGHT IMPROVE RUNNING AND JUMPING
        }
    }

    boolean checkCollision(Sprite s1, Sprite s2){
        boolean noXOverlap = s1.getRight() <= s2.getLeft() || s1.getLeft() >= s2.getRight();
        boolean noYOverlap = s1.getBottom() <= s2.getTop() || s1.getTop() >= s2.getBottom();
        return !noYOverlap && !noXOverlap;
    }

    public ArrayList<Sprite> checkCollisionList(Sprite sprite, ArrayList<Sprite> list){
        ArrayList<Sprite> collision_list = new ArrayList<>();
        for(Sprite p: list){
            if (checkCollision(sprite, p)){
                collision_list.add(p);
            }
        }return collision_list;
    }

    public void keyPressed(){
        if(keyCode==SHIFT){
            MOVE_SPEED = 10;
        }else if(keyCode==RIGHT){
            sprites.get(0).setChange_x(MOVE_SPEED);
        }else if(keyCode==LEFT) {
            sprites.get(0).setChange_x(-MOVE_SPEED);
        }else if(key =='a' && isOnPlatforms(sprites.get(0), platforms)){
            sprites.get(0).setChange_y(-JUMP_SPEED);
        }else if(key =='A' && isOnPlatforms(sprites.get(0), platforms)){
            sprites.get(0).setChange_y(-JUMP_SPEED);
        }else if(keyCode == ENTER && isGameOver) {
            sprites.clear();
            platforms.clear();
            coins.clear();
            spiders.clear();
            score = 0;
            settings();
        }



    }

    public void keyReleased(){
        if(keyCode==SHIFT){
            MOVE_SPEED = 5;
        }else if(keyCode==RIGHT){
            sprites.get(0).setChange_x(0);
        }else if(keyCode==LEFT) {
            sprites.get(0).setChange_x(0);
        }
    }



    public void createPlatforms(String filename){
        PImage redBrickImg = loadImage("data/mapTiles/red_brick.png");
        PImage snowImg = loadImage("data/mapTiles/snow.png");
        PImage brownBrickImg = loadImage("data/mapTiles/brown_brick.png");
        PImage snowWallImg = loadImage("data/mapTiles/snowWall.png");
        PImage lavaImg = loadImage("data/mapTiles/lava.png");

        PImage crateImg = loadImage("data/mapTiles/crate.png");
        PImage goldImg1 = loadImage("data/coin/gold1.png");
        PImage goldImg2 = loadImage("data/coin/gold2.png");
        PImage goldImg3 = loadImage("data/coin/gold3.png");
        PImage goldImg4 = loadImage("data/coin/gold4.png");

        PImage playerStandLeftImg = loadImage("/data/player/player_stand_left.png");
        PImage playerStandRightImg = loadImage("/data/player/player_stand_right.png");
        PImage playerWalkLeft1 = loadImage("/data/player/player_walk_left1.png");
        PImage playerWalkLeft2 = loadImage("/data/player/player_walk_left2.png");
        PImage playerWalkRight1 = loadImage("/data/player/player_walk_right1.png");
        PImage playerWalkRight2 = loadImage("/data/player/player_walk_right2.png");

        PImage spiderWalkLeft1 = loadImage("/data/spider/spider_walk_left1.png");
        PImage spiderWalkLeft2 = loadImage("/data/spider/spider_walk_left2.png");
        PImage spiderWalkLeft3 = loadImage("/data/spider/spider_walk_left3.png");
        PImage spiderWalkRight1 = loadImage("/data/spider/spider_walk_right1.png");
        PImage spiderWalkRight2 = loadImage("/data/spider/spider_walk_right2.png");
        PImage spiderWalkRight3 = loadImage("/data/spider/spider_walk_right3.png");

        coinImages.add(goldImg1);
        coinImages.add(goldImg2);
        coinImages.add(goldImg3);
        coinImages.add(goldImg4);

        playerMoveLeft.add(playerWalkLeft1);
        playerMoveLeft.add(playerWalkLeft2);
        playerStandLeft.add(playerStandLeftImg);
        playerStandRight.add(playerStandRightImg);
        playerMoveRight.add(playerWalkRight1);
        playerMoveRight.add(playerWalkRight2);

        playerJumpLeft.add(playerWalkLeft1);
        playerJumpRight.add(playerWalkRight1);

        enemyMoveLeft.add(spiderWalkLeft1);
        enemyMoveLeft.add(spiderWalkLeft2);
        enemyMoveLeft.add(spiderWalkLeft3);

        enemyMoveRight.add(spiderWalkRight1);
        enemyMoveRight.add(spiderWalkRight2);
        enemyMoveRight.add(spiderWalkRight3);

        String[] lines = loadStrings(filename);
        for(int row = 0; row < lines.length; row++){
            String[] values = split(lines[row], ",");
            for(int col = 0; col < values.length; col++){
                if(values[col].equals("1")){
                    Sprite sprite = new Sprite(this, redBrickImg, SPRITE_SIZE/2 + col * SPRITE_SIZE, SPRITE_SIZE/2 + row * SPRITE_SIZE, 0, 0, SPRITE_SCALE);
                    platforms.add(sprite);
                }if(values[col].equals("2")){
                    Sprite sprite = new Sprite(this, snowImg, SPRITE_SIZE/2 + col * SPRITE_SIZE, SPRITE_SIZE/2 + row * SPRITE_SIZE, 0, 0, SPRITE_SCALE);
                    platforms.add(sprite);
                }if(values[col].equals("22")){
                    Sprite sprite = new Sprite(this, snowWallImg, SPRITE_SIZE/2 + col * SPRITE_SIZE, SPRITE_SIZE/2 + row * SPRITE_SIZE, 0, 0, SPRITE_SCALE);
                    platforms.add(sprite);
                }if(values[col].equals("3")){
                    Sprite sprite = new Sprite(this, brownBrickImg, SPRITE_SIZE/2 + col * SPRITE_SIZE, SPRITE_SIZE/2 + row * SPRITE_SIZE, 0, 0, SPRITE_SCALE);
                    platforms.add(sprite);
                }if(values[col].equals("4")){
                    Sprite sprite = new Sprite(this, crateImg, SPRITE_SIZE/2 + col * SPRITE_SIZE, SPRITE_SIZE/2 + row * SPRITE_SIZE, 0, 0, SPRITE_SCALE);
                    platforms.add(sprite);
                }if(values[col].equals("8")){
                    Sprite sprite = new Sprite(this, lavaImg, SPRITE_SIZE/2 + col * SPRITE_SIZE, SPRITE_SIZE/2 + row * SPRITE_SIZE, 0, 0, SPRITE_SCALE);
                    lavaImages.add(sprite);
                }if(values[col].contains("5")){
                    Coin coin = new Coin(this, coinImages, SPRITE_SCALE);
                    coin.setCenter_x(SPRITE_SIZE/2 + col * SPRITE_SIZE);
                    coin.setCenter_y(SPRITE_SIZE/2 + row * SPRITE_SIZE);
                    coins.add(coin);
                }if(values[col].contains("6")){
                    float boundaryLeft = col * SPRITE_SIZE;
                    float boundaryRight = boundaryLeft + 4 * SPRITE_SIZE;
                    Enemy enemy = new Enemy(this, enemyMoveLeft, enemyMoveRight, 50/72, boundaryLeft, boundaryRight);
                    enemy.setCenter_x(SPRITE_SIZE/2 + col * SPRITE_SIZE);
                    enemy.setCenter_y(SPRITE_SIZE/2 + row * SPRITE_SIZE);
                    spiders.add(enemy);
                }if(values[col].equals("9")) {
                    Player sprite = new Player(platforms, this, this,playerMoveLeft, playerMoveRight, playerStandLeft, playerStandRight, playerJumpLeft,
                            playerJumpRight, PLAYER_SCALE);
                    sprite.setCenter_x(SPRITE_SIZE/2 + col * SPRITE_SIZE);
                    sprite.setCenter_y(SPRITE_SIZE/2 + row * SPRITE_SIZE);
                    playerStartX =(SPRITE_SIZE/2 + col * SPRITE_SIZE);
                    playerStartY =(SPRITE_SIZE/2 + row * SPRITE_SIZE);
                    sprites.add(sprite);
                }
            }
        }
    }
}
