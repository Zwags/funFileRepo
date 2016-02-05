package simplegui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author rolf
 */
public class DrwBox extends AbstractDrawable {
    private int width, height;
    private boolean filled;
    private BasicStroke stroke;

    public DrwBox(Color c, double transparency, double posX, double posY, double width, double height, int strokeWidth,String name, boolean filled) {
        float[] rgb = c.getRGBColorComponents(null);
        this.color = new Color(rgb[0], rgb[1], rgb[2], (float) transparency);
        this.posX = (int) (posX);
        this.posY = (int) (posY);
        this.width = (int) (width);
        this.height = (int) (height);
        stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        this.name = name;
        this.filled = filled;
    }

    @Override
    protected void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(stroke);
        if (filled) {
            g.fillRect(posX, posY, width, height);
        } else {
            g.drawRect(posX, posY, width, height);
        }
    }
}
