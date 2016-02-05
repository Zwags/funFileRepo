package simplegui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JPanel;

/**
 *
 * @author rolf
 */
public class DrawPanel extends JPanel implements MouseListener {

    private LinkedList<AbstractDrawable> drawables;
    private ReentrantLock drawLock;
    protected Color background = Color.WHITE;
    protected int mouseX, mouseY;
    protected boolean mouseClicked = false;
    protected LinkedList<SGMouseListener> mouseListeners = new LinkedList<SGMouseListener>();

    /**
     * Constructor
     *
     * @param width
     * @param height
     */
    protected DrawPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        drawables = new LinkedList<AbstractDrawable>();
        drawLock = new ReentrantLock();
        this.addMouseListener(this);
    }

    /**
     * Add a drawable to the panel.
     *
     * @param d
     */
    protected void addDrawable(AbstractDrawable d) {
        drawLock.lock();
        drawables.addLast(d);
        drawLock.unlock();
    }

    /**
     * Erase all drawables
     */
    protected void clearPanel() {
        drawLock.lock();
        drawables.clear();
        drawLock.unlock();
    }

    /**
     * Erase specified drawable
     *
     * @param name name of drawable to be removed.
     * @return returns true if drawable was found
     */
    protected boolean eraseDrawable(String name) {
        drawLock.lock();
        AbstractDrawable dr = null;
        for (AbstractDrawable d : drawables) {
            try {
                if (d.name.equals(name)) {
                    dr = d;
                    break;
                }
            } catch (NullPointerException e) {
                //name string == null
            }
        }
        if (dr != null) {
            drawables.remove(dr);
        }
        drawLock.unlock();
        return (dr != null);
    }

    /**
     * get first occurrence of AbstractDrawable <name> returns a shallow copy of
     * the drawable! direct access!
     *
     * @param name
     * @return AbstractDrawable or null
     */
    protected AbstractDrawable getDrawable(String name) {
        drawLock.lock();
        AbstractDrawable dr = null;
        for (AbstractDrawable d : drawables) {
            if (d.name.equals(name)) {
                dr = d;
                break;
            }
        }
        drawLock.unlock();
        return (dr);
    }

    /**
     * return all drawables specified by name the drawables returned are shallow
     * copies.
     *
     * @param name
     * @return list of all drawables
     */
    protected LinkedList<AbstractDrawable> getDrawables(String name) {
        LinkedList<AbstractDrawable> outList = new LinkedList<AbstractDrawable>();
        drawLock.lock();
        AbstractDrawable dr = null;
        for (AbstractDrawable d : drawables) {
            if (d.name.equals(name)) {
                outList.add(d);
            }
        }
        drawLock.unlock();
        return (outList);
    }

    @Override
    /**
     * Call draw method of all drawables
     */
    public void paintComponent(Graphics g) {
        int sizeX = getWidth();
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        drawLock.lock();
        for (AbstractDrawable d : drawables) {
            d.draw(g2d);
        }
        drawLock.unlock();
    }

    // mouse methods
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseClicked = true;

        // event based
        for (SGMouseListener m : mouseListeners) {
            m.reactToMouseClick(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
