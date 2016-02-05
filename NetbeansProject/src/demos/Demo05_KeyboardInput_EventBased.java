package demos;

import simplegui.KeyboardListener;
import simplegui.SimpleGUI;

/**
 *
 * @author rolf
 */
public class Demo05_KeyboardInput_EventBased implements KeyboardListener {

    int radiusID; // determines which radius to change
    double radius1, radius2;
    private static SimpleGUI sg;

    public Demo05_KeyboardInput_EventBased() {
        radius1 = radius2 = 10.0;
        radiusID = 0;
    }

    @Override
    public void reactToKeyboardEntry(String input) {
        try {
            // convert string to double
            // (this might throw a NumberException format. That's why
            // this block is surrounded by try/catch)
            double radius = (new Double(input)).doubleValue();
            if (radiusID == 0) {
                radius1 = radius;
            } else {
                radius2 = radius;
            }
            // toggle radii
            radiusID = (radiusID + 1) % 2;
            sg.print("Radius " + (radiusID + 1) + " >");
            // compute top left corner of bounding box
            double startX = (sg.getWidth() - radius1) / 2;
            double startY = (sg.getHeight() - radius2) / 2;
            // draw
            sg.eraseAllDrawables();
            sg.drawEllipse(startX, startY, (int) radius1, (int) radius2);
        } catch (NumberFormatException e) {
            sg.print("Wrong format. Radius " + (radiusID + 1) + " >");
        }
    }

    @Override
    public void reactToKeyboardSingleKey(String input) {
    }

    public static void main(String[] args) {
        sg = new SimpleGUI(false);
        Demo05_KeyboardInput_EventBased demo = new Demo05_KeyboardInput_EventBased();
        sg.registerToKeyboard(demo);
        sg.print("Enter radii> ");
    }
}
