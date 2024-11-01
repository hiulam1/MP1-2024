package ch.epfl.cs107.utils;

import ch.epfl.cs107.Helper;

import java.nio.charset.StandardCharsets;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;


/**
 * <b>Task 1.2: </b>Utility class to manipulate texts ({@link String}) objects.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Text {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Text(){}

    // ============================================================================================
    // ==================================== STRING MANIPULATION ===================================
    // ============================================================================================

    /**
     * Convert a given <b>String</b> into a <b>byte[]</b> following the <b>UTF-8</b> convention
     * @param str String to convert
     * @return bytes representation of the String in the <b>UTF-8</b> format
     */
    public static byte[] toBytes(String str){

        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Convert a given String into a boolean array representation
     * @implNote the bit array should be based on an <b>UTF-8</b> representation
     * @param str String to convert
     * @return <b>UTF-8</b> representation of the string in the <b>bit array</b> format
     */
    public static boolean[] toBitArray(String str){
        assert str != null: "string must not be empty";
        byte[] byteArray = toBytes(str);
        int index = 0;
        boolean[] bitArray = new boolean[byteArray.length * 8];
        boolean[] bits = new boolean[8];
        for (byte b : byteArray) {
            int n = Byte.toUnsignedInt(b);
            if (n - b == 256){
                for (int i = 0; i < 8; i++){
                    bits[7 - i] = Bit.getXthBit(b, i);
                }
            } else{
                bits = Bit.toBitArray(b);
            }
            System.arraycopy(bits, 0, bitArray, index, bits.length);
            index += bits.length;
        }
        return bitArray;
    }
    /**
     * Convert a given <b>byte[]</b> into a <b>String</b> following the <b>UTF-8</b> convention
     * @param bytes String in the byte array format
     * @return String representation using the {@link String} type
     */
    public static String toString(byte[] bytes) {

        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Convert a given bit array to a String
     * @param bitArray <b>UTF-8</b> compatible bit array
     * @return <b>UTF-8 String</b> representation of the bit array
     */
    public static String toString(boolean[] bitArray) {
        assert bitArray != null: "bit array cannot be empty";
        assert bitArray.length % 8 == 0: "bit array must be divisible by 8";
        byte[] byteArray = new byte[bitArray.length / 8];
        // stock every 8 bit into a bit array
        // then convert to byte using toByte
        // stock byte in byte array
        // convert byte array to string
        boolean[][] oneByte = new boolean[bitArray.length / 8][8];
        for (int j = 0; j < byteArray.length; j++) {
            for (int i = 0; i < 8; i++){
                oneByte[j][i] = bitArray[8 * j + i];
            }
            byteArray[j] = Bit.toByte(oneByte[j]);
        }
        return Text.toString(byteArray);
    }
}
