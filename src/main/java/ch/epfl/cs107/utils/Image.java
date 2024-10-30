package ch.epfl.cs107.utils;

import ch.epfl.cs107.Helper;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 1.3: </b>Utility class to manipulate ARGB images
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Image {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Image(){}

    // ============================================================================================
    // ==================================== PIXEL MANIPULATION ====================================
    // ============================================================================================

    /**
     * Build a given pixel value from its respective components
     *
     * @param alpha alpha component of the pixel
     * @param red red component of the pixel
     * @param green green component of the pixel
     * @param blue blue component of the pixel
     * @return packed value of the pixel
     */
    public static int argb(byte alpha, byte red, byte green, byte blue){
        int argb;
        argb = (Byte.toUnsignedInt(alpha) << 24) | (Byte.toUnsignedInt(red) << 16) | (Byte.toUnsignedInt(green) << 8) | (Byte.toUnsignedInt(blue));
        return argb;
    }

    /**
     * Extract the alpha component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the alpha component of the pixel
     */
    public static byte alpha(int pixel){
        byte alpha;
        alpha = (byte) ((pixel >> 24) & 255);
        return alpha;
    }

    /**
     * Extract the red component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the red component of the pixel
     */
    public static byte red(int pixel){
        byte red;
        red = (byte) ((pixel >> 16) & 255);
        return red;
    }

    /**
     * Extract the green component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the green component of the pixel
     */
    public static byte green(int pixel){
        byte green;
        green = (byte) ((pixel >> 8) & 255);
        return green;
    }

    /**
     * Extract the blue component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the blue component of the pixel
     */
    public static byte blue(int pixel){
        byte blue;
        blue = (byte) (pixel & 255);
        return blue;
    }

    /**
     * Compute the gray scale of the given pixel
     *
     * @param pixel packed value of the pixel
     * @return gray scaling of the given pixel
     */
    public static int gray(int pixel){
        int average = Byte.toUnsignedInt(red(pixel)) + Byte.toUnsignedInt(green(pixel)) + Byte.toUnsignedInt(blue(pixel));
        return average / 3;
    }

    /**
     * Compute the binary representation of a given pixel.
     *
     * @param gray gray scale value of the given pixel
     * @param threshold when to consider a pixel white
     * @return binary representation of a pixel
     */
    public static boolean binary(int gray, int threshold){
        assert threshold >= 0 && threshold <= 255 : "threshold must be between 0 and 255";
        assert gray >= 0 && gray <= 255 : "gray must be between 0 and 255";
        return gray >= threshold;
    }

    // ============================================================================================
    // =================================== IMAGE MANIPULATION =====================================
    // ============================================================================================

    /**
     * Build the gray scale version of an ARGB image
     *
     * @param image image in ARGB format
     * @return the gray scale version of the image
     */
    public static int[][] toGray(int[][] image){
        assert image != null: "image cannot be empty";
        int[][] grayScale = new int[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                grayScale[i][j] = Image.gray(image[i][j]);
            }
        }
        return grayScale;
    }

    /**
     * Build the binary representation of an image from the gray scale version
     *
     * @param image Image in gray scale representation
     * @param threshold Threshold to consider
     * @return binary representation of the image
     */
    public static boolean[][] toBinary(int[][] image, int threshold){
        assert image != null: "image cannot be empty";
        boolean[][] binary = new boolean[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                binary[i][j] = binary(image[i][j], threshold);
            }
        }
        return binary;
    }

    /**
     * Build an ARGB image from the gray-scaled image
     * @implNote The result of this method will a gray image, not the original image
     * @param image grayscale image representation
     * @return <b>gray ARGB</b> representation
     */
    public static int[][] fromGray(int[][] image){
        assert image != null: "image cannot be empty";
        int[][] colored = new int[image.length][image[0].length];
        byte alpha = (byte) 255;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                int color = image[i][j];
                int pixel = argb(alpha, (byte) color, (byte) color, (byte) color);
                colored[i][j] = pixel;
            }
        }
        return colored;
    }

    /**
     * Build an ARGB image from the binary image
     * @implNote The result of this method will a black and white image, not the original image
     * @param image binary image representation
     * @return <b>black and white ARGB</b> representation
     */
    public static int[][] fromBinary(boolean[][] image){
        assert image != null: "image cannot be empty";
        int[][] result = new int[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j]) {
                    byte alpha = (byte)255;
                    byte red = (byte)255;
                    byte green = (byte)255;
                    byte blue = (byte)255;
                    result[i][j] = argb(alpha, red, green, blue);
                } else {
                    byte alpha = (byte)255;
                    byte red = (byte)0;
                    byte green = (byte)0;
                    byte blue = (byte)0;
                    result[i][j] = argb(alpha, red, green, blue);
                }
            }
        }
        return result;
    }

}
