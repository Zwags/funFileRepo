package demos;

import simplegui.SGMouseListener;
import simplegui.SimpleGUI;

/**
 *
 * @author rolf
 */
public class Demo07_MouseInput_EventBased implements SGMouseListener {

    SimpleGUI sg;

    public Demo07_MouseInput_EventBased() {
        sg = new SimpleGUI(false);
        sg.registerToMouse(this);
    }

    @Override
    public void reactToMouseClick(int x, int y) {
        sg.drawDot(x, y, 5);
    }

    public static void main(String args[]) {
        new Demo07_MouseInput_EventBased();
    }
}
