/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepixaliztion;

import java.awt.Color;
import simplegui.*;
import simplegui.DrwImage;
import simplegui.RGB;
import simplegui.SimpleGUI;

/**
 *
 * @author tud05722
 * Zachary Wagner
 */
public class ImagePixaliztion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Please put in a number to pixelize.");
        //Read in the user input, move onto then move onto the user image
        //double userInput = sg.keyReadDouble(); remember it takes in an int and makes it a double
        DrwImage im = new DrwImage("randimage.jpg");
        SimpleGUI sg = new SimpleGUI(im.getWidth(),im.getHeight());
        sg.drawImage(im, 0, 0, im.getWidth(), im.getHeight(), null);
        int changeSize = (int) sg.keyReadDouble(); // How much offset the grid will have between points, denoted by user
        for (int rowOne = 0; rowOne < 1100; rowOne+= changeSize) { // This for loop will draw the grid
            for (int colOne = 0; colOne < 611; colOne+= changeSize) {
                    sg.drawDot(colOne, rowOne, 1);
                 

                
            }
        }

        for (int rowTwo = 0; rowTwo < im.getHeight(); rowTwo+= changeSize) { // This for loop creates a box over the grid spaces from the above loop
            for (int colTwo = 0; colTwo < im.getWidth(); colTwo+= changeSize) {
                    RGB newColor = makeAverageColor(im, colTwo, rowTwo, changeSize);

                    int redAvg = newColor.r;
                    int greenAvg = newColor.g;
                    int blueAvg = newColor.b;

                    Color newAvgColor = new Color(redAvg, greenAvg, blueAvg); // Creates the new color using the rgb numbers
                    sg.drawFilledBox(colTwo, rowTwo, changeSize, rowTwo, newAvgColor, 1, ""); // Makes the box in the determined area
                }
            }

            //Color c = new Color(r,g,b)  this is how to make a new color
        }

    public static RGB makeAverageColor(DrwImage img, int xStart, int yStart, int gridSize) {
        RGB avgRGB = new RGB();
        RGB nowRGB = new RGB(); //Gets the RGB being used currently
        int pixelCount = 0;
        for (int pix = yStart; pix < (yStart + gridSize)-1; pix++) {
            for (int pixels = xStart; pixels < (xStart + gridSize)-1; pixels++) {
                nowRGB = img.getPixelRGB(xStart, yStart);
                avgRGB.r += nowRGB.r;
                avgRGB.g += nowRGB.g;
                avgRGB.b += nowRGB.b;
                pixelCount++;

            }
        }
        avgRGB.r /= (gridSize - 1) * (gridSize - 1);
        avgRGB.g /= (gridSize - 1) * (gridSize - 1);
        avgRGB.b /= (gridSize - 1) * (gridSize - 1);

        return avgRGB;

    }
}
