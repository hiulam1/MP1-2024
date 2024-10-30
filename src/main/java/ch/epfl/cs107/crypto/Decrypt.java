package ch.epfl.cs107.crypto;

import ch.epfl.cs107.Helper;

import javax.crypto.Cipher;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 2: </b>Utility class to decrypt a given cipher text.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Decrypt {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Decrypt(){}

    // ============================================================================================
    // ================================== CAESAR'S ENCRYPTION =====================================
    // ============================================================================================

    /**
     * Method to decode a byte array message using a single character key
     * <p>
     * @param cipher Cipher message to decode
     * @param key Key to decode with
     * @return decoded message
     */
    public static byte[] caesar(byte[] cipher, byte key) {
        byte[] decrypted = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i++) {
            int result = cipher[i] - key;

            decrypted[i] = (byte) result;
        }

        return decrypted;
    }

    // ============================================================================================
    // =============================== VIGENERE'S ENCRYPTION ======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a byte array keyword
     * @param cipher Cipher message to decode
     * @param keyword Key to decode with
     * @return decoded message
     */
    public static byte[] vigenere(byte[] cipher, byte[] keyword) {
        byte[] decrypted = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i++) {
            int modulo = i % keyword.length;
            decrypted[i] = (byte) (cipher[i] - keyword[modulo]);
        }
        return decrypted;

    }

    // ============================================================================================
    // =================================== CBC'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to decode cbc-encrypted ciphers
     * @param cipher message to decode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return decoded message
     */
    public static byte[] cbc(byte[] cipher, byte[] iv) {
        int blockSize = iv.length;
        int numberBlocks = cipher.length / blockSize;
        int remainingBytes = cipher.length % blockSize;

        byte[] encrypted = new byte[cipher.length];
        byte[] previousPad = iv.clone();

        for (int block = 0; block < numberBlocks ; block++) {
            int beg = block * blockSize;

            byte[] currentPlainBlock = new byte[blockSize];
            System.arraycopy(cipher, beg, currentPlainBlock, 0, blockSize);

            byte[] currentEncodedBlock = oneTimePad(currentPlainBlock, previousPad);
            previousPad = currentEncodedBlock;
            System.arraycopy(currentEncodedBlock, 0, encrypted, beg, blockSize);
        }
        if (remainingBytes > 0) {
            int beg = blockSize * numberBlocks;

            byte[] currentPlainBlock = new byte[remainingBytes];
            System.arraycopy(cipher, beg, currentPlainBlock, 0, remainingBytes);
            byte[] pad = new byte[remainingBytes];

            System.arraycopy(previousPad, 0, pad, 0, remainingBytes);

            byte[] finalEncrypted = oneTimePad(currentPlainBlock, pad);

            System.arraycopy(finalEncrypted, 0, encrypted, beg, remainingBytes);
        }
        return encrypted;

    }

    // ============================================================================================
    // =================================== XOR'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to decode xor-encrypted ciphers
     * @param cipher text to decode
     * @param key the byte we will use to XOR
     * @return decoded message
     */
    public static byte[] xor(byte[] cipher, byte key) {
        byte[] decrypted = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i++) {
            decrypted[i] = (byte) (cipher[i] ^ key);
        }

        return decrypted;
    }

    // ============================================================================================
    // =================================== ONETIME'S PAD ENCRYPTION ===============================
    // ============================================================================================

    /**
     * Method to decode otp-encrypted ciphers
     * @param cipher text to decode
     * @param pad the one-time pad to use
     * @return decoded message
     */
    public static byte[] oneTimePad(byte[] cipher, byte[] pad) {
        assert cipher.length == pad.length: "pad length must be the same as message length";
        byte[] decrypted = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i++) {
            decrypted[i] = (byte) (cipher[i] ^ pad[i]);
        }

        return decrypted;
    }

}
