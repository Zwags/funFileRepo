package demos;

import java.awt.Color;
import simplegui.AbstractDrawable;
import simplegui.SimpleGUI;
import simplegui.TimerListener;

/**
 *
 * @author rolf
 */
public class Demo11_GroupAnimation implements TimerListener {

    SimpleGUI sg;

    public Demo11_GroupAnimation() {
        sg = new SimpleGUI(640,480,false);
        sg.setTitle("Simple Animation, Group of Drawables");
        createBackground();
        createCar();
        sg.registerToTimer(this);
        sg.timerStart(50);
    }

    private void createBackground() {
        sg.drawFilledBox(0, 0, 640, 180, Color.blue, 1.0, "");
        sg.drawFilledBox(0, 320, 640, 160, Color.green, 1.0, "");

    }

    private void createCar() {
        sg.drawBox(20, 240, 40, 20, Color.red, 1, 3, "car");
        sg.drawFilledBox(0, 259, 120, 20, Color.red, 1, "car");
        sg.drawDot(15, 279, 10, Color.black, 1, "car");
        sg.drawDot(85, 279, 10, Color.black, 1, "car");
    }

    @Override
    public void reactToTimer(long time) {
        AbstractDrawable car1 = sg.getDrawable("car");
        if (car1.posX > 640) {
            sg.animMoveAllRel(-640, 0, "car");
        } else {
            sg.animMoveAllRel(4, 0, "car");
        }
        sg.repaintPanel(); // important!
    }

    public static void main(String[] args) {
        new Demo11_GroupAnimation();
    }
}
