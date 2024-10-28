package ch.epfl.cs107.stegano;

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
 * <b>Task 3.1: </b>Utility class to perform Image Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ImageSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private ImageSteganography(){}

    // ============================================================================================
    // ================================== EMBEDDING METHODS =======================================
    // ============================================================================================

    /**
     * Embed an ARGB image on another ARGB image (the cover)
     * @param cover Cover image
     * @param argbImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedARGB(int[][] cover, int[][] argbImage, int threshold){
        int[][] grayImage = Image.toGray(argbImage);
        int[][] newImage = embedGray(cover, grayImage, threshold);
        return newImage;
    }
    /**
     * Embed a Gray scaled image on another ARGB image (the cover)
     * @param cover Cover image
     * @param grayImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedGray(int[][] cover, int[][] grayImage, int threshold){
        boolean[][] binary = Image.toBinary(grayImage, threshold);
        int[][] newImage = embedBW(cover, binary);
        return newImage;
    }

    /**
     * Embed a binary image on another ARGB image (the cover)
     * @param cover Cover image
     * @param load Embedded image
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedBW(int[][] cover, boolean[][] load){
        assert (load.length <= cover.length) && (load[0].length <= cover[0].length);
        int[][] bits = new int[cover.length][cover[0].length];
        for(int i = 0; i < cover.length; i++){
            for(int j = 0; j < cover[0].length; j++){
                bits[i][j] = cover[i][j];
            }
        }
        for(int i = 0; i < load.length; i++){
            for(int j = 0; j < load[0].length; j++){
                bits[i][j] = Bit.embedInLSB(cover[i][j], load[i][j]);
            }
        }
        return bits;
    }

    // ============================================================================================
    // =================================== REVEALING METHODS ======================================
    // ============================================================================================

    /**
     * Reveal a binary image from a given image
     * @param image Image to reveal from
     * @return binary representation of the hidden image
     */
    public static boolean[][] revealBW(int[][] image) {
        boolean[][] bits = new boolean[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                bits[i][j] = Bit.getLSB(image[i][j]);
            }
        }
        return bits;
    }

}
