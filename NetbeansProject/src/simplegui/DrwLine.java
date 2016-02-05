package simplegui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author rolf
 */
public class DrwLine extends AbstractDrawable {
    private int posXend, posYend;
    private BasicStroke stroke;

    public DrwLine(Color c, double transparency, double posX, double posY, double posXe, double posYe, String name, int strokeWidth) {
        float[] rgb = c.getRGBColorComponents(null);
        this.color = new Color(rgb[0], rgb[1], rgb[2], (float) transparency);
        this.posX = (int) (posX);
        this.posY = (int) (posY);
        this.posXend = (int)posXe;
        this.posYend = (int)posYe;
        this.name = name;
        stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    }

    @Override
    protected void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(stroke);
        g.drawLine(posX, posY, posXend, posYend);
    }
    
    
    @Override
    protected void moveTo(int x, int y){
        int dx = x-posX;
        int dy = y - posY;
        super.moveTo(x,y);
        posXend += dx;
        posYend += posY;
    }
}
