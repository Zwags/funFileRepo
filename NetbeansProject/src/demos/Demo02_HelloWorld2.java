/*
 * Slightly advanced Hello World Demo of SimpleGUI
 * Shows different drawing elements and parameters
 */
package demos;

import java.awt.Color;
import simplegui.SimpleGUI;

/**
 *
 * @author rolf
 */
public class Demo02_HelloWorld2 {

    public static void main(String[] args) {
        // set up SimpleGUI, specified size, GUI elements not displayed
        SimpleGUI sg = new SimpleGUI(230, 300, false);
        sg.setTitle("Demo2");
        sg.centerGUIonScreen();
        // define background and default foreground color
        sg.setBackgroundColor(Color.black);
        sg.setColorAndTransparency(Color.green, 1.0);

        // draw text
        // since no color is specified, the default color will be used
        sg.drawText("Hello World in default color", 10, 30);
        // Draw text in specified color. Transparency 1.0 => opaque
        sg.drawText("Hello World in red", 10, 60, Color.red, 1.0, "");

        // draw some basic elements
        //
        // dot at 30,90, radius = 1. Default color
        sg.drawDot(30, 90, 1);
        // 3 dots with increasing radius, specified color
        sg.drawDot(80, 90, 9, Color.PINK, 1.0, "");
        sg.drawDot(130, 90, 16, Color.ORANGE, 1.0, "");
        sg.drawDot(180, 90, 25, Color.cyan, 1.0, "");
        //
        // filled box, default color/transparency
        sg.drawFilledBox(10, 120, 40, 30);
        // filled box, fully defined
        sg.drawFilledBox(60, 120, 40, 30, Color.red, 1.0, "");
        // empty box (frame), default color/transparency
        sg.drawBox(110, 120, 40, 30);
        // empty box(frame), fully defined, stroke width 5
        sg.drawBox(160, 120, 40, 30, Color.red, 1.0, 5, "");
        //
        // filled oval, default color/transparency
        sg.drawFilledEllipse(10, 160, 40, 30);
        // filled oval, fully defined
        sg.drawFilledEllipse(60, 160, 40, 30, Color.red, 1.0, "");
        // empty oval, default color/transparency
        sg.drawEllipse(110, 160, 40, 30);
        // empty oval, fully defined, stroke width 5
        sg.drawEllipse(160, 160, 40, 30, Color.red, 1.0, 5, "");
        //
        // Lines of different color and stroke size
        // Line: startpoint (x,y), endpoint (x,y)
        sg.drawLine(10, 200, 50, 230);
        // Line: startpoint (x,y), endpoint (x,y), strokewidth
        sg.drawLine(60, 200, 100, 230, 3);
        // Line: startpoint (x,y), endpoint (x,y), color, transparency, strokewidth
        sg.drawLine(110, 200, 150, 230, Color.red, 1.0, 5, "");
        sg.drawLine(160, 200, 200, 230, Color.yellow, 1.0, 8, "");
        //
        // boxes of different transparency
        sg.drawFilledBox(10, 250, 40, 30, Color.red, 1.0, "");
        sg.drawFilledBox(60, 250, 40, 30, Color.red, 0.7, "");
        sg.drawFilledBox(110, 250, 40, 30, Color.red, 0.4, "");
        sg.drawFilledBox(160, 250, 40, 30, Color.red, 0.1, "");
        sg.drawFilledBox(30, 260, 40, 30, Color.green, 0.1, "");
        sg.drawFilledBox(80, 260, 40, 30, Color.green, 0.4, "");
        sg.drawFilledBox(130, 260, 40, 30, Color.green, 0.7, "");
        sg.drawFilledBox(180, 260, 40, 30, Color.green, 1.0, "");
    }
}