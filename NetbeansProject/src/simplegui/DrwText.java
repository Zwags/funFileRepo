package simplegui;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author rolf
 */
public class DrwText extends AbstractDrawable{
    String text = "";
    protected DrwText(Color c, double transparency, double posX, double posY, String text, String name){
        this.posX = (int)posX;
        this.posY = (int)posY;
        float[] rgb = c.getRGBColorComponents(null);
        this.color = new Color(rgb[0], rgb[1], rgb[2], (float) transparency);
        this.name = name;
        this.text = text;
    }

    @Override
    protected void draw(Graphics2D g) {
        g.setColor(color);
        g.drawString(text, posX, posY);
    }
}
