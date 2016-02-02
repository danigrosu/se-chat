package ro.mta.se.chat.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.server.ExportException;

/**
 * Created by Dani on 1/26/2016.
 */
public class DataConversion {
    /**
     * Converts hex to bytes
     *
     * @param s input string
     * @return byte representation of the input hexadecimal
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Converts byte array to base64 format
     *
     * @param input input byte array
     * @return base64 String
     */
    public static String byteArrayToBase64(byte[] input) {
        return Base64.encode(input);
    }

    /**
     * Converts base64 format to byte array
     *
     * @param input base64 String
     * @return byte array
     * @throws Exception
     */
    public static byte[] base64ToByteArray(String input) throws Exception {
        return Base64.decode(input);
    }

    /**
     * Retrieves the bytes of a file
     *
     * @param infile filename
     * @return byte array
     */
    public static byte[] getFileBytes(String infile) {
        File f = new File(infile);
        int sizecontent = ((int) f.length());
        byte[] data = new byte[sizecontent];
        try {
            FileInputStream freader = new FileInputStream(f);
            freader.read(data, 0, sizecontent);
            freader.close();
            return data;
        } catch (IOException ioe) {
            Logger.log(Level.ERROR, DataConversion.class.toString(), ioe);
            return null;
        }
    }
}
