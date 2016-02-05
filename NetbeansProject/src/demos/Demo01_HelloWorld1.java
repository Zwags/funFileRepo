/*
 * The simplest Hello World Demo of SimpleGUI
 */
package demos;

import java.awt.Color;
import simplegui.SimpleGUI;

/**
 *
 * @author rolf
 */
public class Demo01_HelloWorld1 {

    public static void main(String[] args) {
        // set up SimpleGUI, default size, GUI elements displayed (but not used)
        SimpleGUI sg = new SimpleGUI();
        sg.setTitle("Demo1: Hello World");
        // use sg to draw the text.
        sg.drawText("Hello World!", 300, 240);   
    }
}
