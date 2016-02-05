package demos;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import simplegui.SimpleGUI;

/**
 *
 * @author rolf
 */
public class Demo03_BasicDrawing {

    public static void main(String[] args) {
        SimpleGUI sg = new SimpleGUI(800, 600, false);
        sg.setTitle("Demo3");
        sg.maximizeGUIonScreen(); // full screen!
        sg.pauseProgram(200); // give frame time to expand to max., before we continue
        sg.setBackgroundColor(Color.BLACK);
        // Let's draw 100 lines and boxes.
        // Since we draw lots of elements here, we will switch off auto repaint.
        // The default, auto repaint on, draws the entire screen after each single
        // newly added element, which in our case would take too much time.
        // We want the screen repainted only once, after all elements are created.
        // This will be achieved with the commands
        // setAutoRepaint(false)  ... to switch off the repainting
        // and repaintPanel() ... to repaint once.
        sg.setAutoRepaint(false); 
        for (int counter = 0; counter<1000; counter++){
            addLineAndBox(counter, sg);
        }
        sg.repaintPanel();
    }

    private static void addLineAndBox(int counter, SimpleGUI sg) {
        int w = sg.getWidth()/2;
        int h = sg.getHeight()/2;
        double cd = counter / 50.0;
        // line, start and endpoint
        // startpoints and endpoints describe 2 different curves
        double sx = Math.sin(cd * 0.7) * w + w;
        double sy = Math.cos(cd * 0.9) * h/4 + h;
        double ex = Math.sin(cd) * w/4 + w;
        double ey = Math.cos(cd*0.8) * h+h;
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
}
