package simplegui;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author rolf
 */
public class DrwDot extends AbstractDrawable{
    private int diameter;

    public DrwDot(Color c, double transparency, double posX, double posY, double radius, String name){
        float[] rgb = c.getRGBColorComponents(null);
        this.color = new Color(rgb[0], rgb[1], rgb[2], (float)transparency);
        this.posX = (int)(Math.round(posX-radius));
        this.posY = (int)(Math.round(posY-radius));
        this.diameter = (int)(Math.round(radius*2.0));
        this.name = name;
    }
    
    @Override
    protected void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval(posX, posY, diameter, diameter);
    }  
}
