package simplegui;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author rolf
 */
public abstract class AbstractDrawable {
    String name = "";
    public Color color;
    public int posX, posY;
    
    // abstract methods
    protected abstract void draw(Graphics2D g);
    
    // implemented methods
    protected void moveTo(int x, int y){
        posX = x;
        posY = y;
    }

    protected void moveRel(int dx, int dy){
        moveTo(posX+dx, posY+dy);
    }
}
