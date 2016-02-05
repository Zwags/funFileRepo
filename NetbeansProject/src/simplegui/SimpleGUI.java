package simplegui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author rolf
 */
public class SimpleGUI extends Thread implements ActionListener, ChangeListener, KeyListener {
    // GUI elements

    private JTextField text;
    private JFrame frame;
    private DrawPanel panel;
    private JButton button1, button2, timerSwitch;
    private JRadioButton radioButton;
    private JSlider slider;
    private Container GUIElements; // container for all GUI elements except panel
    private LinkedList<GUIListener> guiListeners;
    private boolean button1pressed, button2pressed;
    private LinkedList<KeyboardListener> keyboardListeners;
    private StringBuffer keyBufferEV = new StringBuffer(); // for event based key handling
    private StringBuffer keyBufferSQ = new StringBuffer(); // for sequential key handling
    private boolean enterSQ = false;
    // timer
    private final long DEFAULTPAUSETIME = 1000;
    private boolean timerRunning = false;    // timer run state
    private long SLEEPYTIME;    // timer pause value in ms
    private boolean timerIsInitialized = false;
    private LinkedList<TimerListener> timerListeners;
    private ReentrantLock timerLock = new ReentrantLock();
    // drawing
    private Color defaultColor = Color.BLACK;
    private double defaultTransparency = 1.0; //opaque
    private boolean doRepaint = true;
    private boolean keyPressed = false;

    public SimpleGUI() {
        this(640, 480, true);  // call constructor with width and height
    }

    public SimpleGUI(int width, int height) {
        this(width, height, true);
    }

    public SimpleGUI(boolean createGUIElements) {
        this(640, 480, createGUIElements);
    }

    public SimpleGUI(int width, int height, boolean createGUIElements) {
        guiListeners = new LinkedList<GUIListener>();
        keyboardListeners = new LinkedList<KeyboardListener>();
        timerListeners = new LinkedList<TimerListener>();
        frame = new JFrame("SimpleGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        panel = new DrawPanel(width, height);
        text = new JTextField();
        text.setFocusable(false);
        text.setEditable(false);
        frame.add(text, "North");
        if (createGUIElements) {
            button1 = new JButton("Button1");
            button1.setFocusable(false);
            button1.setActionCommand("1");
            button2 = new JButton("Button2");
            button2.setFocusable(false);
            button2.setActionCommand("2");
            radioButton = new JRadioButton("Switch");
            radioButton.setFocusable(false);
            radioButton.setActionCommand("3");
            slider = new JSlider(0, 100, 50);
            slider.setFocusable(false);
            slider.setPaintTicks(true);
            slider.setMajorTickSpacing(50);
            slider.setMinorTickSpacing(10);
            timerSwitch = new JButton("Timer off");
            timerSwitch.setFocusable(false);
            timerSwitch.setActionCommand("TIMER");
            timerSwitch.setOpaque(true);
            GUIElements = new Container();
            GUIElements.setLayout(new GridLayout(2, 1));
            Container south0 = new Container();
            south0.setLayout(new GridLayout(1, 3));
            Container south1 = new Container();
            south1.setLayout(new GridLayout(1, 2));
            south0.add(button1);
            south0.add(button2);
            south0.add(radioButton);
            south1.add(slider);
            south1.add(timerSwitch);
            GUIElements.add(south0);
            GUIElements.add(south1);
            frame.add(GUIElements, "South");
            frame.setFocusable(true);
            // registration
            timerSwitch.addActionListener(this);
            button1.addActionListener(this);
            button2.addActionListener(this);
            radioButton.addActionListener(this);
            slider.addChangeListener(this);
        }
        frame.addKeyListener(this);
        frame.add(panel, "Center");
        frame.pack();
        frame.requestFocus();
        frame.setVisible(true);
    }

    // =========================================================================
    // Draw methods
    /**
     * Plot a dot
     *
     * @param x center x
     * @param y center y
     * @param radius
     * @param c color
     * @param transparency alpha channel (0.0=transparent ... 1.0=opaque)
     * @param name name in drawable list
     */
    public void drawDot(double x, double y, double radius, Color c, double transparency, String name) {
        panel.addDrawable(new DrwDot(c, transparency, x, y, radius, name));
        if (doRepaint) {
            repaintPanel();
        }
    }

    /**
     * Plot dot with default color & transparency
     *
     * @param x
     * @param y
     * @param radius
     */
    public void drawDot(double x, double y, double radius) {
        drawDot(x, y, radius, defaultColor, defaultTransparency, "");
    }

    /**
     * Draw a non-filled box (a frame)
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width
     * @param height
     * @param c color
     * @param transparency
     * @param name
     */
    public void drawBox(double x, double y, double width, double height, Color c, double transparency, int strokeWidth, String name) {
        panel.addDrawable(new DrwBox(c, transparency, x, y, width, height, strokeWidth, name, false));
        if (doRepaint) {
            repaintPanel();
        }
    }

    /**
     * Draw a non-filled box (a frame), default color and transparency
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width
     * @param height
     */
    public void drawBox(double x, double y, double width, double height) {
        drawBox(x, y, width, height, defaultColor, defaultTransparency, 1, "");
    }

    /**
     * Draw a filled box
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width
     * @param height
     * @param c color
     * @param transparency
     * @param name
     */
    public void drawFilledBox(double x, double y, double width, double height, Color c, double transparency, String name) {
        panel.addDrawable(new DrwBox(c, transparency, x, y, width, height, 1, name, true));
        if (doRepaint) {
            repaintPanel();
        }
    }

    /**
     * Draw a filled box, default color and transparency
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width
     * @param height
     */
    public void drawFilledBox(double x, double y, double width, double height) {
        drawFilledBox(x, y, width, height, defaultColor, defaultTransparency, "");
    }

    /**
     * Draw an Ellipse
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width
     * @param height
     * @param c color
     * @param transparency
     * @param name
     */
    public void drawEllipse(double x, double y, double width, double height, Color c, double transparency, int strokeWidth, String name) {
        panel.addDrawable(new DrwOval(c, transparency, x, y, width, height, strokeWidth, name, false));
        if (doRepaint) {
            repaintPanel();
        }
    }

    /**
     * Draw an Ellipse, default color and transparency
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width
     * @param height
     */
    public void drawEllipse(double x, double y, double width, double height) {
        drawEllipse(x, y, width, height, defaultColor, defaultTransparency, 1, "");
    }

    /**
     * Draw a filled Ellipse
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width
     * @param height
     * @param c color
     * @param transparency
     * @param name
     */
    public void drawFilledEllipse(double x, double y, double width, double height, Color c, double transparency, String name) {
        panel.addDrawable(new DrwOval(c, transparency, x, y, width, height, 1, name, true));
        if (doRepaint) {
            repaintPanel();
        }
    }

    /**
     * Draw a filled Ellipse, default color and transparency
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width
     * @param height
     */
    public void drawFilledEllipse(double x, double y, double width, double height) {
        drawFilledEllipse(x, y, width, height, defaultColor, defaultTransparency, "");
    }

    /**
     * Draw Line
     *
     * @param xStart
     * @param yStart
     * @param xEnd
     * @param yEnd
     * @param c
     * @param transparency
     * @param strokeWidth
     * @param name
     */
    public void drawLine(double xStart, double yStart, double xEnd, double yEnd, Color c, double transparency, int strokeWidth, String name) {
        panel.addDrawable(new DrwLine(c, transparency, xStart, yStart, xEnd, yEnd, name, strokeWidth));
        if (doRepaint) {
            repaintPanel();
        }
    }

    /**
     * Draw Line
     *
     * @param xStart
     * @param yStart
     * @param xEnd
     * @param yEnd
     * @param strokeWidth
     */
    public void drawLine(double xStart, double yStart, double xEnd, double yEnd, int strokeWidth) {
        drawLine(xStart, yStart, xEnd, yEnd, defaultColor, defaultTransparency, strokeWidth, "");
    }

    /**
     * Draw Line, default stroke width = 1
     *
     * @param xStart
     * @param yStart
     * @param xEnd
     * @param yEnd
     */
    public void drawLine(double xStart, double yStart, double xEnd, double yEnd) {
        drawLine(xStart, yStart, xEnd, yEnd, defaultColor, defaultTransparency, 1, "");
    }

    /**
     * Draw image. Width and height can be specified. Setting width(height) to
     * -1 adjusts the width(height) to the panel, setting width(height) to 0,
     * uses the image's original width(height).
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param name
     */
    public void drawImage(DrwImage image, double x, double y, double width, double height, String name) {
        image.posX = (int) x;
        image.posY = (int) y;
        image.width = (int) width;
        image.height = (int) height;
        image.name = name;
        image.panel = panel;
        panel.addDrawable(image);
        if (doRepaint) {
            repaintPanel();
        }
    }

    /**
     * Draw image. Width and height can be specified. Setting width(height) to
     * -1 adjusts the width(height) to the panel, setting width(height) to 0,
     * uses the image's original width(height).
     *
     * @param filename
     * @param x
     * @param y
     * @param width
     * @param height
     * @param name
     */
    public void drawImage(String filename, double x, double y, double width, double height, String name) {
        DrwImage d = new DrwImage(filename, x, y, width, height, panel, name);
        drawImage(d, x, y, width, height, name);
    }

    /**
     * Draw Image, size = image size
     *
     * @param filename
     * @param x
     * @param y
     */
    public void drawImage(String filename, double x, double y) {
        drawImage(filename, x, y, 0, 0, "");
    }

    /**
     * Draw Text
     *
     * @param text
     * @param x
     * @param y
     * @param c
     * @param transparency
     * @param name
     */
    public void drawText(String text, double x, double y, Color c, double transparency, String name) {
        panel.addDrawable(new DrwText(c, transparency, x, y, text, name));
        if (doRepaint) {
            repaintPanel();
        }
    }

    /**
     * Draw Text, default color and transparency
     *
     * @param text
     * @param x
     * @param y
     */
    public void drawText(String text, double x, double y) {
        drawText(text, x, y, defaultColor, defaultTransparency, "");
    }

//    /**
//     * draw a 2 dimensional double matrix  (not implemented yet)
//     *
//     * @param matrix
//     * @param x
//     * @param y
//     * @param pixelScale side length in pixels for each matrix cell
//     * @param c color of the brightest element. other colors are interpolated
//     * from black
//     * @param name
//     */
//    public void drawMatrix(double[][] matrix, double x, double y, int pixelScale, Color c, String name) {
//        DrwMatrix dm = new DrwMatrix(matrix, c, defaultTransparency, x, y, pixelScale, name);
//        panel.addDrawable(dm);
//        if (doRepaint) {
//            repaintPanel();
//        }
//    }
//
//    /**
//     * draw matrix with default values (color = white, cell size = 2pxl)
//     *
//     * @param matrix
//     * @param x
//     * @param y
//     */
//    public void drawMatrix(double[][] matrix, double x, double y) {
//        drawMatrix(matrix, x, y, 2, Color.white, null);
//    }

    // =========================================================================
    // General Control
    /**
     * Repaint the panel. Explicit repaint. By default, all drawing methods do a
     * repaint. This can be switched off (for animation, anti flickering), and
     * be replaced by an explicit repaint call. See also: setAutoRepaint()
     */
    public void repaintPanel() {
        panel.repaint();
    }

    /**
     * By default, all drawing methods perform an immediate repaint. This
     * behavior can be controlled here. See also: repaintPanel().
     *
     * @param repaintOn
     */
    public void setAutoRepaint(boolean repaintOn) {
        doRepaint = repaintOn;
    }

    /**
     * Set background color
     *
     * @param c background color
     */
    public void setBackgroundColor(Color c) {
        panel.background = c;
        panel.repaint();
    }

    /**
     * set default values for color and transparency. draw commands, which do
     * not specify color/transparency, will use these values. Changing the
     * default values will only affect newly created drawables.
     *
     * @param c
     * @param transparency
     */
    public void setColorAndTransparency(Color c, double transparency) {
        // just to be safe, not a shallow copy...
        defaultColor = new Color(c.getRed(), c.getGreen(), c.getBlue());
        defaultTransparency = transparency;
    }

    /**
     * Remove specific drawable from panel. Removes first drawable with given
     * name.
     *
     * @param name name of drawable to erase. Names can be specified in draw
     * commands.
     * @return returns true if a drawable was removed.
     */
    public boolean eraseSingleDrawable(String name) {
        boolean ret = panel.eraseDrawable(name);
        if (doRepaint) {
            repaintPanel();
        }
        return (ret);
    }

    /**
     * Remove all specified drawables from panel. In contrast to
     * eraseSingleDrawable this method removes all drawables with a given name.
     * This can be used to remove groups of drawables.
     *
     * @param name name of drawable to erase. Names can be specified in draw
     * commands.
     */
    public void eraseAllDrawables(String name) {
        while (panel.eraseDrawable(name)) {
        }
        if (doRepaint) {
            panel.repaint();
        }
    }

    /**
     * Clear panel, remove all drawables.
     */
    public void eraseAllDrawables() {
        panel.clearPanel();
        if (doRepaint) {
            panel.repaint();
        }
    }

    /**
     * get width of panel
     *
     * @return width of panel
     */
    public int getWidth() {
        return (panel.getWidth());
    }

    /**
     * get height of panel
     *
     * @return height of panel
     */
    public int getHeight() {
        return (panel.getHeight());
    }

    /**
     * Read first AbstractDrawable <name> from drawable list. Returns null if
     * unsuccessful.
     *
     * @param name
     * @return drawable or null
     */
    public AbstractDrawable getDrawable(String name) {
        return (panel.getDrawable(name));
    }

    /**
     * Return list of all drawables specified by name. Typically used for group
     * processing, e.g. animation
     *
     * @param name
     * @return list of drawables
     */
    public LinkedList<AbstractDrawable> getDrawables(String name) {
        return (panel.getDrawables(name));
    }

    /**
     * Read first image <name> from drawable list. Returns null if unsuccessful.
     *
     * @param name
     * @return image or null
     */
    public DrwImage getImage(String name) {
        DrwImage im = null;
        try {
            im = (DrwImage) getDrawable(name);
        } catch (Exception e) {
            im = null;
        }
        return (im);
    }

    // =========================================================================
    // GUI element related methods
    /**
     * Print a string in textfield. This does NOT print on the panel. To print
     * on the panel, use drawText()
     *
     * @param s
     */
    public void print(String s) {
        if (text != null) {
            text.setText(s);
        }
    }

    /**
     * Set Title on Window Frame
     *
     * @param s
     */
    public void setTitle(String s) {
        frame.setTitle(s);
    }

    /**
     * Set location of SimpleGUI on screen.
     *
     * @param x
     * @param y
     */
    public void setLocationOnScreen(int x, int y) {
        frame.setLocation(x, y);
    }

    /**
     *
     */
    public void centerGUIonScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    /**
     *
     */
    public void maximizeGUIonScreen() {
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Pause program: pauses the Thread of calling method, usually the main
     * thread. This is just a convenience method.
     *
     * @param pauseTime
     */
    public void pauseProgram(long pauseTime) {
        try {
            Thread.sleep(pauseTime);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Get slider value without registering (synchronous).
     *
     * @return slider value (between 0 and 100)
     */
    public int getSliderValue() {
        return (slider.getValue());
    }

    /**
     * retrieve radio button value without registering, switched on
     * (true)/off(false), synchronous version
     *
     * @return radio button switch-state
     */
    public boolean getSwitchValue() {
        return (radioButton.isSelected());
    }

    /**
     * Synchronous query of button1. Waits until button is pressed. It's not
     * advisable to use buttons this way. Better use event based methods.
     */
    public void waitForButton1() {
        button1pressed = false; //reset
        while (!button1pressed) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * Synchronous query of button2. Waits until button is pressed. It's not
     * advisable to use buttons this way. Better use event based methods.
     */
    public void waitForButton2() {
        button2pressed = false; //reset
        while (!button2pressed) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * Set the text-display for GUI elements
     */
    public void labelButton1(String s) {
        button1.setText(s);
    }

    public void labelButton2(String s) {
        button2.setText(s);
    }

    public void labelSwitch(String s) {
        radioButton.setText(s);
    }

    /**
     * register to this SimpleGUI. Registered listeners will be called on
     * actions from button1, button2, switch, slider.
     *
     * @param listener
     */
    public void registerToGUI(GUIListener listener) {
        guiListeners.addLast(listener);
    }

    @Override
    /**
     * Button callback
     */
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("TIMER")) {
            timerRunning = !timerRunning; // this variable is queried by the timer thread
            if (timerRunning) {
                timerStart();
            } else {
                timerPause();
            }
        } else {
            // Buttons
            int b = (new Integer(s)).intValue();
            if (b == 1) {
                button1pressed = true;
            }
            if (b == 2) {
                button2pressed = true;
            }
            for (GUIListener l : guiListeners) {
                switch (b) {
                    case 1:
                        l.reactToButton1();
                        break;
                    case 2:
                        l.reactToButton2();
                        break;
                    case 3:
                        l.reactToSwitch(radioButton.isSelected());
                        break;
                }
            }
        }
    }

    @Override
    /**
     * Slider callback
     */
    public void stateChanged(ChangeEvent e) {
        int v = slider.getValue();
        for (GUIListener l : guiListeners) {
            l.reactToSlider(v);
        }
    }

    // =========================================================================
    // Animation methods
    /**
     * Move a single drawable specified by name, to absolute screen position To
     * move a group of drawables, please see animMoveRelative() As in all
     * animation methods (animXXXX), repaint is not called.
     *
     * @param x
     * @param y
     * @param name
     */
    public void animMoveTo(double x, double y, String name) {
        AbstractDrawable d = panel.getDrawable(name);
        d.moveTo((int) x, (int) y);
    }

    /**
     * Move a single drawable specified by name, to absolute screen position To
     * move a group of drawables, please see animMoveRelative() As in all
     * animation methods (animXXXX), repaint is not called.
     *
     * @param x
     * @param y
     * @param name
     */
    public void animMoveRel(double x, double y, String name) {
        AbstractDrawable d = panel.getDrawable(name);
        d.moveRel((int) x, (int) y);
    }

    /**
     * Move all drawables specified by name, relative to their current position.
     *
     * @param dx
     * @param dy
     * @param name
     */
    public void animMoveAllRel(double dx, double dy, String name) {
        LinkedList<AbstractDrawable> l = getDrawables(name);
        for (AbstractDrawable d : l) {
            d.moveRel((int) dx, (int) dy);
        }
    }

    // =========================================================================
    // Timer methods
    /**
     * start timer with default time, or restart with previously defined time
     */
    public void timerStart() {
        long time = (timerIsInitialized ? SLEEPYTIME : DEFAULTPAUSETIME);
        timerStart(time);
    }

    /**
     *
     * start/restart timer thread
     *
     * @param SLEEPYTIME pause time
     */
    public void timerStart(long SLEEPYTIME) {
        this.SLEEPYTIME = SLEEPYTIME;
        timerRunning = true;
        if (!timerIsInitialized) {
            timerIsInitialized = true;
            this.start();
        }
        try {
            timerSwitch.setBackground(Color.green);
            timerSwitch.setText("Timer ON (" + SLEEPYTIME + ")");
        } catch (Exception e) {
        }
    }

    /**
     * Pause Timer. To restart, use timerStart();
     */
    public void timerPause() {
        timerRunning = false;
        try {
            timerSwitch.setBackground(Color.red);
            timerSwitch.setText("Timer PAUSED");
        } catch (Exception e) {
        }
        ;
    }

    /**
     * Register a TimerListener to the timer. To remove from timer, use
     * removeFromTimer(t)
     *
     * @param t the listener
     */
    public void registerToTimer(TimerListener t) {
        timerLock.lock();
        timerListeners.addLast(t);
        timerLock.unlock();
    }

    /**
     * Cancel a registration to timer. Removes Listener from timer-list.
     *
     * @param t the listener to be removed
     */
    public void removeFromTimer(TimerListener t) {
        timerLock.lock();
        timerListeners.remove(t);
        timerLock.unlock();
    }

    @Override
    /**
     * Timer Thread If timerRunning flag is true, every registered TimerListener
     * is called.
     */
    public void run() {
        while (true) {
            long time = System.currentTimeMillis();
            // call registered listeners
            if (timerRunning) {
                timerLock.lock();
                for (TimerListener t : timerListeners) {
                    t.reactToTimer(time);
                }
                timerLock.unlock();
            }
            // timing
            long timeNeeded = System.currentTimeMillis() - time;
            long pauseTime = Math.max(1, SLEEPYTIME - timeNeeded);
            try {
                Thread.sleep(pauseTime);
            } catch (InterruptedException ex) {
            }
        }
    }

    // =========================================================================
    // Keyboard handling
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        boolean enterEV = false;
        boolean validEV = false;
        switch (code) {
            case KeyEvent.VK_ENTER:
                text.setText(text.getText() + " ");
                enterEV = true;
                enterSQ = true;
                break;
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_BACK_SPACE:
                if (keyBufferEV.length() > 0 && keyBufferSQ.length() > 0) {
                    text.setText(text.getText().substring(0, text.getText().length() - 1));
                    keyBufferEV.deleteCharAt(keyBufferEV.length() - 1);
                    keyBufferSQ.deleteCharAt(keyBufferSQ.length() - 1);
                }
                break;
            case KeyEvent.VK_SHIFT:
                // nothing
                break;
            default:
                text.setText(text.getText() + e.getKeyChar());
                keyBufferEV.append(e.getKeyChar());
                keyBufferSQ.append(e.getKeyChar());
                validEV = true;
        }
        keyPressed = true;

        // Event based key-handling:
        // handle registered listeners
        String inpText = "";
        if (enterEV) {
            inpText = keyBufferEV.toString();
            keyBufferEV.setLength(0);
        }
        for (KeyboardListener k : keyboardListeners) {
            if (enterEV) {
                k.reactToKeyboardSingleKey("#"); // "enter" is submtted as '#'
                k.reactToKeyboardEntry(inpText);
            } else if (validEV) {
                k.reactToKeyboardSingleKey(e.getKeyChar() + "");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * wait until <enter> is pressed, return all input. This is a non-event
     * based keyboard routine! It is advised to use event based key-input!
     *
     * @return string
     */
    public String keyReadString(boolean eraseText) {
        if (eraseText) {
            text.setText("");
        }
        text.setText(text.getText() + "> ");
        String textstring = ".";
        while (!enterSQ) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
        }
        enterSQ = false;
        String inpText = keyBufferSQ.toString();
        keyBufferSQ.setLength(0);
        return (inpText);
    }

    public String keyReadString() {
        return (keyReadString(false));
    }

    /**
     * wait until a single key is pressed, return all input. This is a non-event
     * based keyboard routine! It is advised to use event based key-input!
     *
     * @return char
     */
    public char keyReadChar(boolean eraseText) {
        keyPressed = false;
        while (!keyPressed) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
        }
        String textstring = text.getText();
        return (textstring.charAt(textstring.length() - 1));
    }

    public char keyReadChar() {
        return (keyReadChar(false));
    }

    /**
     * wait for double-format number input. Invalid format input is caught, the
     * user is prompted to re-enter. This is a non-event based keyboard routine!
     * It is advised to use event based key-input!
     *
     * @return char
     */
    public double keyReadDouble(boolean eraseText) {
        boolean ok = false;
        double inpNumber = 0.0;
        while (!ok) {
            try {
                String inp = keyReadString();
                inpNumber = (new Double(inp)).doubleValue();
                ok = true;
            } catch (NumberFormatException n) {
                print("try again");
            }
        }
        return (inpNumber);
    }

    public double keyReadDouble() {
        return (keyReadDouble(false));
    }

    /**
     * register a KeyboardListener to SimpleGUI object.
     *
     * @param kl
     */
    public void registerToKeyboard(KeyboardListener kl) {
        keyboardListeners.addLast(kl);
    }

    // =========================================================================
    // mouse handling
    // the draw panel is a jawa.awt.event.mouseListener. All core-event handling
    // is done there.
    /**
     * wait for mouse click. Sequential version. It is advised to use the event
     * based version! This version has a wait loop, causing a delay of 100ms
     *
     * @return int [] x,y of mouse position in draw panel
     */
    public int[] waitForMouseClick() {
        panel.mouseClicked = false;
        while (!panel.mouseClicked) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        int ret[] = {panel.mouseX, panel.mouseY};
        return (ret);
    }

    /**
     * register to mouse
     *
     * @param l
     */
    public void registerToMouse(SGMouseListener l) {
        panel.mouseListeners.add(l);
    }

    public static void main(String[] args) {
        SimpleGUI sg = new SimpleGUI();
        sg.setBackgroundColor(Color.BLUE);
        DrwImage im = new DrwImage("red.png");
        int h = im.getHeight();
        int w = im.getWidth();
        while (true) {
            sg.eraseAllDrawables();
            sg.drawImage("red.png", sg.getSliderValue() * 2, 100);
            sg.pauseProgram(50);
        }
    }
}
