/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import java.awt.Color;
import simplegui.GUIListener;
import simplegui.SimpleGUI;
import simplegui.TimerListener;

/**
 *
 * @author rolf
 */
public class Demo08_ReactToGUI implements GUIListener, TimerListener {

    SimpleGUI sg;
    int posX, posY, radius = 200;
    Color c;
    double transparency = 1.0;

    private Demo08_ReactToGUI() {
        // setup SimpleGUI
        // 640x480, show GUIelements, black background
        sg = new SimpleGUI(640, 480, true);
        sg.setTitle("GUI Element Demo");
        sg.print("Try all GUI Elements!");
        sg.setBackgroundColor(Color.black);
        // re-label GUI elements
        sg.labelButton1("Change Color");
        sg.labelButton2("Change Position");
        sg.labelSwitch("Fill on/off");

        // initialize
        posX = 320;
        posY = 240;
        c = Color.GREEN;

        // initialize graphics
        drawBackground();
        draw();

        // important: REGISTER TO SimpleGUI !
        sg.registerToGUI(this);
        sg.registerToTimer(this);
        sg.timerStart(30);
    }

    /**
     * create a checker board in the background
     */
    public void drawBackground() {
        boolean dark = true;
        for (int c = 0; c < 640; c += 20) {
            dark = (c % 40 == 0);
            for (int r = 0; r < 480; r += 20) {
                dark = !dark;
                Color col = (dark ? Color.darkGray : Color.LIGHT_GRAY);
                sg.drawFilledBox(c + 1, r + 1, 18, 18, col, 1.0, "");
            }
        }

    }

    /**
     * erase panel and draw an ellipse
     */
    private void draw() {
        // erase the previous circle, do not remove background boxes!
        sg.eraseSingleDrawable("TheCircle");
        // renew circle
        if (sg.getSwitchValue()) {
            sg.drawFilledEllipse(posX - radius, posY - radius, radius * 2, radius * 2, c, transparency, "TheCircle");
        } else {
            sg.drawEllipse(posX - radius, posY - radius, radius * 2, radius * 2, c, transparency, 5, "TheCircle");
        }
    }

    // -------------------------------------------------------------------------
    // Callback methods for GUIListener
    // These MUST be implemented in order to be a GUIListener!
    // Whenever the respective GUI element in the SimpleGUI is activated,
    // one of these methods is called. These methods contain the core functionality
    // of the demo program.
    @Override
    public void reactToButton1() {
        // change color
        // random r,g,b values (55..255)
        int r = (int) (Math.random() * 200 + 55);
        int g = (int) (Math.random() * 200 + 55);
        int b = (int) (Math.random() * 200 + 55);
        c = new Color(r, g, b);
        draw();
    }

    @Override
    public void reactToButton2() {
        // change position
        // compute a random position and redraw
        posX = (int) (Math.random() * (640 - 2 * radius)) + radius;
        posY = (int) (Math.random() * (480 - 2 * radius)) + radius;
        draw();
    }

    @Override
    public void reactToSwitch(boolean switchValue) {
        // there's nothing to do here, except to re-draw
        draw();
    }

    @Override
    public void reactToSlider(int sliderValue) {
        // change transparency
        transparency = sliderValue / 100.0;
        draw();
    }

    @Override
    public void reactToTimer(long time) {
        radius = (int)Math.abs(200*(Math.sin(time/1000.0)));
        draw();
    }

    /**
     * Main
     *
     * @param args
     */
    public static void main(String args[]) {
        new Demo08_ReactToGUI();
    }
}
