package simplegui;
/**
 *
 * @author rolf
 * Declares callback methods for SimpleGUI GUI-elements 
 */
public interface GUIListener {
    public void reactToButton1();
    public void reactToButton2();
    public void reactToSwitch(boolean switchValue);
    public void reactToSlider(int sliderValue);
}
