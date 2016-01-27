package ro.mta.se.chat.utils;

/**
 *
 * Created by Dani on 1/26/2016.
 */
public class DataConversion {
    /**
     * Converts hex to bytes
     * @param s input string
     * @return byte representation of the input hexadecimal
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
