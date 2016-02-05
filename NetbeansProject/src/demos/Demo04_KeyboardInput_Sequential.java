package demos;

import simplegui.SimpleGUI;

/**
 *
 * @author rolf
 */
public class Demo04_KeyboardInput_Sequential {

    public static void main(String []args) {
        SimpleGUI sg = new SimpleGUI(false);
        while (true) {
            sg.print("Enter the radii");
            // read radii
            double radius1 = sg.keyReadDouble();
            double radius2 = sg.keyReadDouble();
            // compute top left corner of bounding box
            double startX = (sg.getWidth() - radius1) / 2;
            double startY = (sg.getHeight() - radius2) / 2;
            // draw
            sg.eraseAllDrawables();
            sg.drawEllipse(startX, startY, (int)radius1, (int)radius2);
        }
    }
}
