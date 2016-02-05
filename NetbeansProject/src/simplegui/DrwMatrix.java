// NOT IMPLEMENTED YET!
// UNDER CONSTRUCTION


//package simplegui;
//
//import java.awt.Color;
//import java.awt.Graphics2D;
//
///**
// *
// * @author rolf
// */
//public class DrwMatrix extends AbstractDrawable {
//
//    private double[][] data;
//    double dataMin;
//    double dataScale;
//    private int pxlScale; //box edge-length for each cell
//
//    public DrwMatrix(double [][]data, Color col, double transparency, double posX, double posY, int scale, String name) {
//        
//        float[] rgb = col.getRGBColorComponents(null);
//        this.color = new Color(rgb[0], rgb[1], rgb[2], (float) transparency);
//        this.posX = (int) (posX);
//        this.posY = (int) (posY);
//        this.pxlScale = scale;
//        this.name = name;
//        dataMin = Double.POSITIVE_INFINITY;
//        double dataMax = Double.NEGATIVE_INFINITY;
//        for (int r = 0; r < data.length; r++) {
//            for (int c = 0; c < data[r].length; c++) {
//                if (data[r][c] > dataMax) {
//                    dataMax = data[r][c];
//                }
//                if (data[r][c] < dataMin) {
//                    dataMin = data[r][c];
//                }
//            }
//        }
//        if (dataScale == 0.0) {
//            dataScale = 1.0;
//        }
//        dataScale = 1/(dataMax - dataMin);
//    }
//
//    @Override
//    protected void draw(Graphics2D g) {
//        float[] rgb = color.getRGBColorComponents(null);
//        for (int r = 0; r < data.length; r++) {
//            for (int c = 0; c < data[r].length; c++) {
//                int x = c * pxlScale + posX;
//                int y = r * pxlScale + posY;
//                double brightness = (data[r][c]-dataMin)*dataScale;
//                rgb[0]*=brightness;
//                rgb[1]*=brightness;
//                rgb[2]*=brightness;
//                Color bc=new Color((int)(Math.round(rgb[0])),(int)(Math.round(rgb[1])),(int)(Math.round(rgb[2])));
//                g.setColor(bc);
//                g.fillRect(x, y, pxlScale, pxlScale);
//            }
//        }
//    }
//}
