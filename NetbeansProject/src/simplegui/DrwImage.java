package simplegui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author rolf
 */
public class DrwImage extends AbstractDrawable {

    BufferedImage image;
    int width, height;
    JPanel panel;

    /**
     * This constructor is used by SimpleGUI.
     * @param filename
     * @param posX
     * @param posY
     * @param width if 0: use image width, -1: use panel width, other: scale
     * image
     * @param height if 0: use image height, -1: use panel height, other: scale
     * image
     */
    protected DrwImage(String filename, double posX, double posY, double width, double height, JPanel panel, String name) {
        image = null;
        try {
            image = ImageIO.read(new File(filename));
            this.posX = (int) posX;
            this.posY = (int) posY;
            this.width = (int) width;
            this.height = (int) height;
            this.panel = panel;
            this.name = name;
        } catch (IOException e) {
            System.out.println("simplegui.DrwImage: could not load file " + filename);
        }
    }

    /**
     * This constructor can be used to read an image for processing without
     * displaying. An image read/processed can then be displayed using
     * SimpleGUI.drawImage(img,...);
     *
     * @param filename
     */
    public DrwImage(String filename) {
        this(filename, 0, 0, 0, 0, null, "");
    }

    @Override
    protected void draw(Graphics2D g) {
        if (image != null) {
            int w, h;
            if (width == -1) {
                w = panel.getWidth();
            } else if (width == 0) {
                w = image.getWidth();
            } else {
                w = width;
            }
            if (height == -1) {
                h = panel.getHeight();
            } else if (width == 0) {
                h = image.getHeight();
            } else {
                h = height;
            }

            int dx2 = posX + w;
            int dy2 = posY + h;
            int srcx2 = image.getWidth();
            int srcy2 = image.getHeight();
            g.drawImage(image, posX, posY, dx2, dy2, 0, 0, srcx2, srcy2, null);
        }
    }

    /**
     * Return (unscaled) width of the physical image (i.e. the BufferedImage),
     * NOT the displayed image!
     *
     * @return width
     */
    public int getWidth() {
        return (image.getWidth());
    }

    /**
     * Return (unscaled) height of the physical image (i.e. the BufferedImage),
     * NOT the displayed image!
     *
     * @return height
     */
    public int getHeight() {
        return (image.getHeight());
    }

    /**
     * Set pixel at (x,y) to color (RGB) All r,g,b values in 0..255 Please note
     * that operations on the image do NOT call repaint on the panel. In order
     * to make the effect visible, panel.repaint() has to be called.
     *
     * @param x column (bounds are tested)
     * @param y row (bounds are tested) 0 = top row.
     * @param r (0..255)
     * @param g (0..255)
     * @param b (0..255)
     */
    public void setPixelRGB(int x, int y, int r, int g, int b) {
        if (x < 0 || x >= image.getWidth()) {
            return;
        }
        if (y < 0 || y >= image.getHeight()) {
            return;
        }
        int a = 255; // transparency
        r = Math.min(r,255);
        g = Math.min(g,255);
        b = Math.min(b,255);
        
        int col = ((a & 0xff) << 24) | (r << 16) | (g << 8) | b;
        image.setRGB(x, y, col);
    }
    
    public void setPixelRGB(int x, int y, RGB rgb){
        setPixelRGB(x,y,rgb.r, rgb.g, rgb.b);
    }
    

    /**
     * get RGB value at (x,y). Returns null if x,y is out of bounds.
     * @param x column (bounds are tested)
     * @param y row (bounds are tested) 0 = top row.
     * @return RGB object. Retrieve r,g,b values from respective fields in RGB.
     */
    public RGB getPixelRGB(int x, int y) {
        if (x < 0 || x >= image.getWidth()) {
            return (null);
        }
        if (y < 0 || y >= image.getHeight()) {
            return (null);
        }
        int col = image.getRGB(x, y);
        int r = (col >> 16) & 0xff;
        int g = (col >> 8) & 0xff;
        int b = col & 0xff;
        return(new RGB(r,g,b));
    }
}
