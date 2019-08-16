import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * This class allows to straighten a text image.
 *
 * @author Simone Sapienza <simoska4@gmail.com>
 * @version 1.0
 */
public class StraightenText {

    private static final int threshold = 180;
    //Flag indicating if we have reached the limit of the image (during analysis)
    private static boolean reached = false;


    /**
     * Main method. Calculate the rotation and return the straightened image.
     * @param image input BufferedImage
     * @return straightened image
     */
    public static BufferedImage straightenText(BufferedImage image){
        reached = false;
        int degree = getInters(image);
        return EditImage.rotateByDegrees(image, degree);
    }


    /*
    * Calculate the line bundle.
    * imageOriginal: input BufferedImage
    * return: degrees of which you need to rotate the image to straighten it
     */
    private static int getInters(BufferedImage imageOriginal) {

        BufferedImage image = preProcessing(imageOriginal);

        Point point1 = new Point();
        Point point2 = new Point();

        int[] rgbData = EditImage.getRGBData(EditImage.thresholdImage(image));

        int max_counter = 20;

        int max_intersect = -1;
        int y1;
        int y2 = 0;
        int x1;

        //Vertical line bundle:
        int count;
        for(x1=0; x1<=image.getWidth(); x1++){
            count=0;
            for (y1 = 0; y1 <= image.getHeight(); y1++) {
                if (y1 < image.getHeight() && y1 >= 0 && ((y1*image.getWidth())+x1)<rgbData.length) {
                    if(((rgbData[(y1*image.getWidth())+x1] >> 16) & 0xFF)<threshold)
                        count++;
                }
            }
            max_intersect = checkMaxIntersect(max_intersect, count, point1, point2, x1, y1, x1, y2);
        }
        //Horizontal line bundle:
        for(y1=0; y1<=image.getHeight(); y1++){
            count=0;
            for (x1 = 0; x1 <= image.getWidth(); x1++) {
                if (y1 <= image.getHeight() && y1 >= 0 && ((y1*image.getWidth())+x1)<rgbData.length) {
                    if(((rgbData[(y1*image.getWidth())+x1] >> 16) & 0xFF)<threshold)
                        count++;
                }
            }
            max_intersect = checkMaxIntersect(max_intersect, count, point1, point2, 0, y1, image.getWidth(), y1);
        }
        //Line bundle:
        for(x1=0; x1<=image.getWidth(); x1++){
            for(int x2=0; x2<=image.getWidth(); x2++) {
                if(x2==image.getWidth()){
                    //right-side
                    max_intersect = getMax_intersect(image, point1, point2, rgbData, max_counter, max_intersect, y1, x2, x1, x1<x2, x1<x2, x1 - x2);
                }
                else if(x2==0){
                    //left-side
                    max_intersect = getMax_intersect(image, point1, point2, rgbData, max_counter, max_intersect, y1, x1, x2, x1 > x2, x1<x2, x1 - x2);
                }
                else {
                    y1 = image.getHeight();
                    y2 = 0;
                    int tot_spostamento_x = x1<x2 ? x2 + image.getWidth() - x1 :  x1 + image.getWidth() - x2;
                    //bundle of parallel lines
                    reached = false;
                    int k=0;
                    do{
                        int x3 = x1<x2 ? x1 - x2 + k : k;
                        int x4 = x1<x2 ? k : x2 - x1 + k;
                        count = line(image, rgbData, y1, y2, x3, x4);
                        max_intersect = checkMaxIntersect(max_intersect, count, point1, point2, x3, y1, x4, y2);
                        k++;
                    }while(!reached && k<(tot_spostamento_x*max_counter));
                }
            }
        }
        return getDegreesFromAscisse((int)point1.getX(), (int)point1.getY(), (int)point2.getX(), (int)point2.getY());
    }


    /*
    * Method that calculates the intersections of a given straight line.
    * image: input BufferedImage
    * rgbData: Array containing all the pixels of the BufferedImage
    * y1: Y coordinates of point 1 to be analyzed
    * y2: Y coordinates of point 2 to be analyzed
    * x3: X coordinates of point 1 to be analyzed
    * x4: X coordinates of point 2 to be analyzed
    * return: number of intersections identified
     */
    private static int line(BufferedImage image,
                             int[] rgbData,
                             int y1,
                             int y2,
                             int x3,
                             int x4){
        return getCount_intersect(image, rgbData, y1, y2, x3, x4);
    }


    /*
    * Method that calculates the number of intersections.
    * image: immagine da analizzare
    * rgbData: Array containing all the pixels of the BufferedImage
    * y1: Y coordinates of point 1 to be analyzed
    * y2: Y coordinates of point 2 to be analyzed
    * x3: X coordinates of point 1 to be analyzed
    * x4: X coordinates of point 2 to be analyzed
    * return: number of intersections identified
    */
    private static int getCount_intersect(BufferedImage image,
                                          int[] rgbData,
                                          int y1,
                                          int y2,
                                          int x3,
                                          int x4) {
        int count=0;
        for (int x = 0; x <= image.getWidth(); x++) {
            int y = (int) ((double) (x - x3) / (double) (x4 - x3) * (double) (y2-y1) + y1);
            if(y==image.getHeight()&&x==image.getWidth())
                reached=true;
            if (y <= image.getHeight() && y >= 0 && ((y*image.getWidth())+x)<rgbData.length) {
                if(((rgbData[(y*image.getWidth())+x] >> 16) & 0xFF)<threshold)
                    count++;
            }
        }
        return count;
    }


    /*
    * Method that calculates the maximum number of intersections.
    * image: input BufferedImage
    * point1: point1 of the line with maximum number of intersections
    * point2: point2 of the line with maximum number of intersections
    * rgbData: Array containing all the pixels of the BufferedImage
    * max_counter: maximum number of cycles to be applied
    * max_intersect: maximum number of intersections identified
    * y1: Y coordinates of point 1
    * x1: X coordinates of point 1
    * x2: X coordinates of point 2
    * b: boolean (verifies that x1 <x2 or x1> x2 - based on the calling function)
    * b2: boolean (verifies that x1 <x2)
    * i: x1-x2
    * return: maximum number of intersections identified so far
     */
    private static int getMax_intersect(BufferedImage image,
                                           Point point1,
                                           Point point2,
                                           int[] rgbData,
                                           int max_counter,
                                           int max_intersect,
                                           int y1,
                                           int x1,
                                           int x2,
                                           boolean b,
                                           boolean b2,
                                           int i) {
        int y2;
        int count;
        for(y2=0; y2<=image.getHeight(); y2++){
            int tot_spostamento_x = b ? x1 + image.getWidth() - x2 : x2 + image.getWidth() - x1;
            //bundle of parallel lines
            reached = false;
            int k=0;
            do{
                int x3 = b2 ? i + k : k;
                int x4 = b2 ? k : -1*i + k;
                count = getCount_intersect(image, rgbData, y1, y2, x3, x4);
                max_intersect = checkMaxIntersect(max_intersect, count, point1, point2, x3, y1, x4, y2);
                k++;
            }while(!reached && k<(tot_spostamento_x*max_counter));
        }
        return max_intersect;
    }


    /*
    * Method that checks if the number of intersections is the maximum identified so far.
    * max_intersect: maximum number of intersections found so far
    * count: number of intersections of the analyzed case
    * point1: point 1 of the line with maximum number of intersections
    * point2: point 2 of the line with maximum number of intersections
    * x1: X coordinates of point1 (if the new point is saved)
    * y1: Y coordinates of point1 (if the new point is saved)
    * x2: X coordinates of point2 (if the new point is saved)
    * y2: Y coordinates of point2 (if the new point is saved)
    * return: maximum number of intersections identified so far
     */
    private static int checkMaxIntersect(int max_intersect,
                                         int count,
                                         Point point1,
                                         Point point2,
                                         int x1,
                                         int y1,
                                         int x2,
                                         int y2){
        if (max_intersect < count) {
            max_intersect = count;
            point1.setLocation(x1, y1);
            point2.setLocation(x2, y2);
        }
        return max_intersect;
    }


    /*
    * Method that returns the angle between the x-axis and a given straight line (from two points).
    * x1: X coordinate of point1
    * y1: Y coordinate of point1
    * x2: X coordinate of point2
    * y2: Y coordinate of point2
    * return: angle formed between the abscissa axis and the two points
     */
    private static int getDegreesFromAscisse(int x1,
                                             int y1,
                                             int x2,
                                             int y2){
        int moltiplicatore = x1<x2 ? 1 : -1;
        int delta_x = (x1 - x2)*moltiplicatore;
        if(delta_x==0)
            return 90;
        int delta_y = (y1 - y2)*moltiplicatore;
        double theta_radians = Math.atan2(delta_y, delta_x);
        return (int)Math.toDegrees(Math.atan(Math.sin(theta_radians)));
    }



    /*
    * Method that pre-processes the image by applying threshold algorithms, resizing and compressing spaces.
    * image: input BufferedImage
    * return: pre-processed BufferedImage
     */
    private static BufferedImage preProcessing(BufferedImage image){
        BufferedImage imageThreshold = EditImage.thresholdImage(image);
        int max_height = 50;
        int max_width = 50;
        double divis = Math.max(imageThreshold.getWidth()/max_width, imageThreshold.getHeight()/max_height);
        if(divis==0)
            divis=1;
        BufferedImage imageResized = EditImage.getScaled(imageThreshold, (int)((double)imageThreshold.getWidth()/divis), (int)((double)imageThreshold.getHeight()/divis));

        return RemoveSpaces.compress(imageResized, false);
    }




}
