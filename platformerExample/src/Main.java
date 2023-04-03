import processing.core.PApplet;

public class Main {

    public static void main(String[] args) {
        String[] processingArgs = {"MySketch"};
        GameEngine mySketch = new GameEngine();
        PApplet.runSketch(processingArgs, mySketch);
    }
}