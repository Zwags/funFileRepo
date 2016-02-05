package demos;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import simplegui.SimpleGUI;
import simplegui.TimerListener;

/**
 *
 * @author rolf
 */
public class Demo09_BasicDrawingWithTimer implements TimerListener {

    SimpleGUI sg;
    int counter = 0;

    public Demo09_BasicDrawingWithTimer(long pauseTime, int x, int y) {
        sg = new SimpleGUI(800, 600, false);
        sg.setTitle("Demo4: Animated Drawing");
        sg.setLocationOnScreen(x, y);
        sg.setBackgroundColor(Color.BLACK);
        sg.registerToTimer(this);
        sg.timerStart(pauseTime);
    }

    private static void addLineAndBox(int counter, SimpleGUI sg) {
        int w = sg.getWidth() / 2;
        int h = sg.getHeight() / 2;
        double cd = counter / 50.0;
        // line, start and endpoint
        // startpoints and endpoints describe 2 different curves
        double sx = Math.sin(cd * 0.7) * w + w;
        double sy = Math.cos(cd * 0.9) * h / 4 + h;
        double ex = Math.sin(cd) * w / 4 + w;
        double ey = Math.cos(cd * 0.8) * h + h;
        // color. changing r,g,b in sin-function with different frequencies
        int r = (int) (Math.sin(cd) * 127 + 128);
        int g = (int) (Math.sin(cd * 0.9) * 127 + 128);
        int b = (int) (Math.sin(cd * 1.2) * 127 + 128);
        Color c = new Color(r, g, b);
        sg.drawLine(sx, sy, ex, ey, c, 1.0, 1, "");
        //
        // transparent box
        // defined by start and endpoint. 
        // draw only every 5th time
        if (counter % 5 != 0) {
            return;
        }
        // swap start and end coordinates such that sx,sy is top left point
        if (sx > ex) {
            double d = sx;
            sx = ex;
            ex = d;
        }
        if (sy > ey) {
            double d = sy;
            sy = ey;
            ey = d;
        }
        sg.drawFilledBox(sx, sy, ex - sx, ey - sy, c, 0.05, "");
    }

    @Override
    public void reactToTimer(long time) {
        if (counter % 400 == 0) {
            sg.eraseAllDrawables();
        }
        addLineAndBox(counter, sg);
        counter++;
    }

    public static void main(String[] args) {
        // this will run in the background:
        // arguments: pausetime, position on screen (x,y)
        new Demo09_BasicDrawingWithTimer(30, 0,0);

        // so here you have time to do other things!
        // for example, start more animations, even with different speed!
        // (please think how much effort this would be without the timer
        // utilization!)
        new Demo09_BasicDrawingWithTimer(20,100,100);
        new Demo09_BasicDrawingWithTimer(40,200,200);
        new Demo09_BasicDrawingWithTimer(50,300,300);
    }
}
