package demos;

import simplegui.SimpleGUI;

/**
 *
 * @author Rolf
 */
public class Demo06_MouseInput_Sequential {
    public static void main(String []args){
        SimpleGUI sg = new SimpleGUI(false);
        while(true){
            int[]xy = sg.waitForMouseClick();
            sg.drawDot(xy[0], xy[1], 5);
        }
    }
}
