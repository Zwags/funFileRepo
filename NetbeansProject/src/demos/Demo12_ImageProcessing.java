package demos;

import java.awt.Color;
import simplegui.DrwImage;
import simplegui.RGB;
import simplegui.SimpleGUI;

/**
 *
 * @author rolf
 */
public class Demo12_ImageProcessing {

    public static void main(String[] args) {
        // Read 9 Images. We will process directly on the images,
        // hence we need physically different copies.
        DrwImage image1 = new DrwImage("testImage.jpg");
        DrwImage image2 = new DrwImage("testImage.jpg");
        DrwImage image3 = new DrwImage("testImage.jpg");
        DrwImage image4 = new DrwImage("testImage.jpg");
        DrwImage image5 = new DrwImage("testImage.jpg");
        DrwImage image6 = new DrwImage("testImage.jpg");
        DrwImage image7 = new DrwImage("testImage.jpg");
        DrwImage image8 = new DrwImage("testImage.jpg");
        DrwImage image9 = new DrwImage("testImage.jpg");


        // panel dimensions. Needs to fit 3x3 images plus a 2 pixel space in between
        int height = image1.getHeight() * 3;
        int width = image1.getWidth() * 3;

        // create a sized SimpleGUI, no GUI elements
        SimpleGUI sg = new SimpleGUI(width + 4, height + 4, false);
        sg.setBackgroundColor(new Color(0, 80, 128));

        // PROCESS IMAGES 2-9, image1 stays unprocessed.
        flipHorizontally(image2, image1);
        darken(image3);
        toGrayscale(image4);
        centerFocus(image5);
        invert(image6);
        dominantColor(image7);
        shuffle(image8);
        quantize(image9);

        // DRAW IMAGES
        int w, h;
        w = h = 0;   // 0,0 => use original image size
        // top row, left to right
        sg.drawImage(image1, 0, 0, w, h, "");
        sg.drawImage(image2, width / 3 + 2, 0, w, h, "");
        sg.drawImage(image3, width * 2 / 3 + 4, 0, w, h, "");
        // middle row, left to right
        int y = height / 3 + 2;
        sg.drawImage(image4, 0, y, w, h, "");
        sg.drawImage(image5, width / 3 + 2, y, w, h, "");
        sg.drawImage(image6, width * 2 / 3 + 4, y, w, h, "");
        // bottom row, left to right
        y = height * 2 / 3 + 4;
        sg.drawImage(image7, 0, y, w, h, "");
        sg.drawImage(image8, width / 3 + 2, y, w, h, "");
        sg.drawImage(image9, width * 2 / 3 + 4, y, w, h, "");
    }

    // -------------------------------------------------------------------------
    // image processing methods
    /**
     * Invert image. Changes image in situ.
     *
     * @param image
     */
    private static void invert(DrwImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGB rgb = image.getPixelRGB(x, y);
                image.setPixelRGB(x, y, 255 - rgb.r, 255 - rgb.g, 255 - rgb.b);
            }
        }
    }

    /**
     * Darken image: divide all rgb values by 3. Changes image in situ.
     *
     * @param image
     */
    private static void darken(DrwImage image) {
        int factor = 4;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGB rgb = image.getPixelRGB(x, y);
                image.setPixelRGB(x, y, rgb.r / factor, rgb.g / factor, rgb.b / factor);
            }
        }
    }

    /**
     * Convert to grayscale. Sets r=g=b=brightness. The brightness in an RGB
     * object is computed as (r+g+b)/3; Changes image in situ.
     *
     * @param image
     */
    private static void toGrayscale(DrwImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGB rgb = image.getPixelRGB(x, y);
                image.setPixelRGB(x, y, rgb.brightness, rgb.brightness, rgb.brightness);
            }
        }
    }

    /**
     * Darken pixels depending on distance from center (centered Gauss curve)
     * Changes image in situ.
     *
     * @param image
     */
    private static void centerFocus(DrwImage image) {
        double twoSigmaSquare = 10 * Math.min(image.getHeight(), image.getWidth());
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;
        for (int x = 0; x < image.getWidth(); x++) {
            double dxS = (x - centerX) * (x - centerX);
            for (int y = 0; y < image.getHeight(); y++) {
                double dyS = (y - centerY) * (y - centerY);
                double dist = dxS + dyS;
                double factor = Math.exp(-dist / twoSigmaSquare);
                RGB rgb = image.getPixelRGB(x, y);
                image.setPixelRGB(x, y, (int) (rgb.r * factor), (int) (rgb.g * factor), (int) (rgb.b * factor));
            }
        }
    }

    /**
     * flip image horizontally. Flips data from src into target image. source
     * remains unchanged. Source and target must be of same size. Changes target
     * in situ.
     *
     * @param image
     */
    private static void flipHorizontally(DrwImage target, DrwImage src) {
        int width = target.getWidth();
        for (int x = 0; x < target.getWidth(); x++) {
            for (int y = 0; y < target.getHeight(); y++) {
                RGB rgb = src.getPixelRGB(x, y);
                target.setPixelRGB(width - x, y, rgb.r, rgb.g, rgb.b);
            }
        }
    }

    /**
     * Dominant color filter: each pixel is set to max. r,g or b, depending on
     * the dominant value. Examples: rgb(100,30,50) => (255,0,0) rgb(100,30,100)
     * => (255,0,255) Changes image in situ.
     *
     * @param image
     */
    private static void dominantColor(DrwImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGB rgb = image.getPixelRGB(x, y);
                int max = Math.max(Math.max(rgb.r, rgb.g), rgb.b);
                int r = (rgb.r == max ? 255 : 0);
                int g = (rgb.g == max ? 255 : 0);
                int b = (rgb.b == max ? 255 : 0);
                image.setPixelRGB(x, y, r, g, b);
            }
        }
    }

    /**
     * shuffle pixels. Randomly select pixel-pairs and swap their colors
     */
    private static void shuffle(DrwImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int number = (int) (w * h);
        for (int i = 0; i < number; i++) {
            int srcX = (int) (Math.random() * w);
            int srcY = (int) (Math.random() * h);
            int trgtX = (int) (Math.random() * w);
            int trgtY = (int) (Math.random() * h);
            RGB rgb = image.getPixelRGB(srcX, srcY);
            image.setPixelRGB(trgtX, trgtY, rgb.r, rgb.g, rgb.b);
        }
    }

    /**
     * quantize pixel colors 8 bit => 1 bit. Randomly select pixel-pairs and
     * swap their colors
     */
    private static void quantize(DrwImage image) {
        int factor = 4;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGB rgb = image.getPixelRGB(x, y);
                int r = ((rgb.r & 0x80) > 0 ? 255 : 0);
                int g = ((rgb.g & 0x80) > 0 ? 255 : 0);
                int b = ((rgb.b & 0x80) > 0 ? 255 : 0);
                image.setPixelRGB(x, y, r,g,b);
            }
        }
    }
}
