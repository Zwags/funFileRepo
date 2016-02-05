/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import java.awt.Color;
import simplegui.SimpleGUI;
import simplegui.TimerListener;

/**
 *
 * @author rolf
 */
public class Demo10_BouncingBalls implements TimerListener {

    double[][] positionXY;      // [][0]position x, [][1]: position y
    double[][] velocityXY;      // [][0]velocity x, [][1]: velocity y in pixel/s
    boolean[] bounced;          // true if ball bounced
    final double RADIUS = 25;
    final long SLEEPYTIME = 33; // about 30 fps
    double gravity;             // will be read from slider
    int numberOfBalls;
    SimpleGUI simpleGUI;
    long previousTime = 0;

    /**
     * Constructor
     *
     * @param n number of balls
     */
    public Demo10_BouncingBalls(int n) {
        // store number of balls.
        numberOfBalls = n;

        // Create a SimpleGUI with default size, GUI elements on.
        simpleGUI = new SimpleGUI();

        // colors etc.
        simpleGUI.setBackgroundColor(Color.ORANGE);
        simpleGUI.setColorAndTransparency(Color.red, 0.4);

        // set auto repaint to false. This avoids flickering!
        // repaint will be explicitly called in reactToTimer().
        simpleGUI.setAutoRepaint(false);

        // create n balls with random position and velocity
        positionXY = new double[numberOfBalls][2];
        velocityXY = new double[numberOfBalls][2];
        bounced = new boolean[numberOfBalls];
        for (int i = 0; i < numberOfBalls; i++) {
            positionXY[i][0] = Math.random() * simpleGUI.getWidth();
            positionXY[i][1] = Math.random() * simpleGUI.getHeight();
            velocityXY[i][0] = Math.random() * 200 - 100;
            velocityXY[i][1] = 0.0;
            bounced[i] = false;
        }

        // register to SimpleGUI timer
        simpleGUI.registerToTimer(this);

        // start timer
        simpleGUI.timerStart(SLEEPYTIME);

        // that's it for here, the rest is done in the timer callback method.
    }

    @Override
    /**
     * Timer Callback Method
     */
    public void reactToTimer(long time) {
        // initialize time variable in first call or if 
        // time disfference is significantly larger than SLEEPYTIME
        // (in this case, the timer was switched to pause)
        if (time - previousTime > SLEEPYTIME * 2) {
            previousTime = time;
        }

        // -----------------------------------------
        // ball physics
        // -----------------------------------------
        // time difference to previous call
        double dt = (time - previousTime) / 1000.0;
        // store time
        previousTime = time;

        // gravity value
        gravity = simpleGUI.getSliderValue() * 8.0;

        // move all balls
        for (int i = 0; i < numberOfBalls; i++) {
            // linear movement: s = s+v*dt
            positionXY[i][0] += velocityXY[i][0] * dt;
            positionXY[i][1] += velocityXY[i][1] * dt;
            bounced[i] = false;

            // check boundaries, bounce off left, right and floor
            // left
            if (positionXY[i][0] - RADIUS < 0) {
                positionXY[i][0] = RADIUS; // physically incorrect, but visually ok
                velocityXY[i][0] = -velocityXY[i][0];
                bounced[i] = true;
            }
            // right
            if (positionXY[i][0] + RADIUS > simpleGUI.getWidth()) {
                positionXY[i][0] = simpleGUI.getWidth() - RADIUS;
                velocityXY[i][0] = -velocityXY[i][0]; // physically incorrect, but visually ok
                bounced[i] = true;
            }
            // floor
            if (positionXY[i][1] + RADIUS > simpleGUI.getHeight()) {
                positionXY[i][1] = simpleGUI.getHeight() - RADIUS;
                velocityXY[i][1] = -velocityXY[i][1]; // physically incorrect, but visually ok
                // guarantee a minimum velocity
                if (velocityXY[i][1] > -100) {
                    velocityXY[i][1] = -100 - Math.random() * 10.0;
                }
                bounced[i] = true;
            }

            // GRAVITY: vy = vy+g*dt
            velocityXY[i][1] += gravity * dt;
        }

        // -----------------------------------------
        // ball graphics
        // -----------------------------------------
        // erase previously drawn elements
        simpleGUI.eraseAllDrawables();

        // create new balls at newly computed positions
        for (int i = 0; i<numberOfBalls; i++) {
            if (!bounced[i]) {
                // not bouncing, use default color
                simpleGUI.drawDot(positionXY[i][0], positionXY[i][1], RADIUS);
            } else {
                // bouncing: specify color
                // ball becomes opaque for a short moment
                simpleGUI.drawDot(positionXY[i][0], positionXY[i][1], RADIUS, Color.red, 1.0, "");
            }
        }

        // explicit call to repaint!
        // this is necessary, since we switched off the auto repaint to avoid
        // flickering.
        simpleGUI.repaintPanel();
    }

    public static void main(String args[]) {
        new Demo10_BouncingBalls(10);
    }
}
