package ro.mta.se.chat.model;

import ro.mta.se.chat.utils.DataConversion;

import java.io.FileOutputStream;

/**
 * Created by Dani on 1/29/2016.
 */
public class FileWriter {
    public static final String DEFAULT_DIRECTORY = "docs/files/";

    /**
     * Writes data to disk
     *
     * @param filename file name
     * @param base64   base64 format of data
     * @throws Exception
     */
    public static void write(String filename, String base64) throws Exception {
        FileOutputStream fis = new FileOutputStream(DEFAULT_DIRECTORY + filename);
        fis.write(DataConversion.base64ToByteArray(base64));
        fis.close();
    }
}
