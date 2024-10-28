package ch.epfl.cs107.crypto;

/**
 * <b>Task 2: </b>Utility class to encrypt a given plain text.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Encrypt {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Encrypt(){}

    // ============================================================================================
    // ================================== CAESAR'S ENCRYPTION =====================================
    // ============================================================================================

    /**
     * Method to encode a byte array message using a single character key
     * the key is simply added to each byte of the original message
     *
     * @param plainText The byte array representing the string to encode
     * @param key the byte corresponding to the char we use to shift
     * @return an encoded byte array
     */
    public static byte[] caesar(byte[] plainText, byte key) {
        byte[] encrypted = new byte[plainText.length];
        for (int i = 0; i < plainText.length; i++) {
            int result = plainText[i] + key;
            encrypted[i] = (byte) result;
        }
        return encrypted;
    }

    // ============================================================================================
    // =============================== VIGENERE'S ENCRYPTION ======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a byte array keyword
     * The keyword is repeated along the message to encode
     * The bytes of the keyword are added to those of the message to encode
     * @param plainText the byte array representing the message to encode
     * @param keyword the byte array representing the key used to perform the shift
     * @return an encoded byte array
     */
    public static byte[] vigenere(byte[] plainText, byte[] keyword) {
        byte[] encrypted = new byte[plainText.length];
        for (int i = 0; i < plainText.length; i++) {
            int modulo = i % keyword.length;
            encrypted[i] = (byte) (plainText[i] + keyword[modulo]);
        }
        return encrypted;
    }

    // ============================================================================================
    // =================================== CBC'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method applying a basic chain block counter of XOR without encryption method.
     * @param plainText the byte array representing the string to encode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return an encoded byte array
     */

    public static byte[] cbc(byte[] plainText, byte[] iv) {

        int blockSize = iv.length;
        int numberBlocks = plainText.length / blockSize;
        int remainingBytes = plainText.length % blockSize;

        byte[] encrypted = new byte[plainText.length];
        byte[] previousPad = iv.clone();

        for (int block = 0; block < numberBlocks ; block++) {
            int beg = block * blockSize;

            byte[] currentPlainBlock = new byte[blockSize];
            System.arraycopy(plainText, beg, currentPlainBlock, 0, blockSize);

            byte[] currentEncodedBlock = oneTimePad(currentPlainBlock, previousPad);
            previousPad = currentEncodedBlock;
            System.arraycopy(currentEncodedBlock, 0, encrypted, beg, blockSize);
        }
        if (remainingBytes > 0) {
            int beg = blockSize * numberBlocks;

            byte[] currentPlainBlock = new byte[remainingBytes];
            System.arraycopy(plainText, beg, currentPlainBlock, 0, remainingBytes);
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
     * Method to encode a byte array using a XOR with a single byte long key
     * @param plainText the byte array representing the string to encode
     * @param key the byte we will use to XOR
     * @return an encoded byte array
     */
    public static byte[] xor(byte[] plainText, byte key) {
        byte[] encrypted = new byte[plainText.length];
        for (int i = 0; i < plainText.length; i++) {
            encrypted[i] = (byte) (plainText[i]^key);
        }

        return encrypted;
    }

    // ============================================================================================
    // =================================== ONETIME'S PAD ENCRYPTION ===============================
    // ============================================================================================

    /**
     * Method to encode a byte array using a one-time pad of the same length.
     *  The method XOR them together.
     * @param plainText the byte array representing the string to encode
     * @param pad the one-time pad
     * @return an encoded byte array
     */
    public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
        assert plainText.length == pad.length: "pad length must be the same as message length";
        assert pad != null: "pad cannot be null";
        byte[] encrypted = new byte[plainText.length];
        for (int i = 0; i < plainText.length; i++) {
            encrypted[i] = (byte) (plainText[i]^pad[i]);
        }

        return encrypted;
    }

    /**
     * Method to encode a byte array using a one-time pad
     * @param plainText Plain text to encode
     * @param pad Array containing the used pad after the execution
     * @param result Array containing the result after the execution
     */
    public static void oneTimePad(byte[] plainText, byte[] pad, byte[] result) {
        result = oneTimePad(plainText, pad);
    }

}