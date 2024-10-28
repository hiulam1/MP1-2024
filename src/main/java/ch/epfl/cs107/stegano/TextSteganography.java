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
 * <b>Task 3.2: </b>Utility class to perform Text Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public class TextSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private TextSteganography(){}

    // ============================================================================================
    // =================================== EMBEDDING BIT ARRAY ====================================
    // ============================================================================================

    /**
     * Embed a bitmap message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedBitArray(int[][] cover, boolean[] message) {
        assert message.length <= cover.length * cover[0].length;
        int[][] newImage = new int[cover.length][cover[0].length];
        for(int i = 0; i < cover.length; i++){
            for(int j = 0; j < cover[0].length; j++){
                newImage[i][j] = cover[i][j];
            }
        }
        for(int i = 0; i < message.length; i++) {
            int row = i / cover[0].length;
            int column = i % cover[0].length;
            newImage[row][column] = embedInLSB(cover[row][column], message[i]);
        }
        return newImage;
    }
    /**
     * Extract a bitmap from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static boolean[] revealBitArray(int[][] image) {
        boolean[] bits = new boolean[image.length * image[0].length];
        int index = 0;
        for(int i = 0; i < image.length; i ++){
            for(int j = 0; j < image[0].length; j ++){
                bits[index++] = Bit.getLSB(image[i][j]);
            }
        }
        return bits;
    }



    // ============================================================================================
    // ===================================== EMBEDDING STRING =====================================
    // ============================================================================================

    /**
     * Embed a String message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedText(int[][] cover, byte[] message) {
        int[][] newImage = new int[cover.length][cover[0].length];
        boolean[] bits = new boolean[message.length * 8];
        for (int i = 0; i < message.length; i++) {
            boolean[] bitArray = toBitArray(message[i]);
            System.arraycopy(bitArray, 0, bits, i * 8, 8);
        }
        newImage = embedBitArray(cover, bits);
        return newImage;
    }
    
    /**
     * Extract a String from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static byte[] revealText(int[][] image) {
        boolean[] bits = revealBitArray(image);
        boolean[] transfer = new boolean[8];
        byte[] values = new byte[bits.length / 8];
        for(int i = 0; i < bits.length / 8; i++){
            for(int j = 0; j < 8; j++){
                System.arraycopy(bits, 8 * i , transfer, j , 8);
            }
            values[i] = Bit.toByte(transfer);
        }
        return values;
    }

}
