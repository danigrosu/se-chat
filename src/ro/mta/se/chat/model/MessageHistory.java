package ro.mta.se.chat.model;

import ro.mta.se.chat.utils.Level;
import sun.reflect.annotation.ExceptionProxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dani on 1/28/2016.
 */
public class MessageHistory {
    public static final String HISTORY_PATH = "docs/history/";

    String username;

    public MessageHistory(String username) {
        this.username = username;
    }

    public void storeNewMessage(String message, boolean me) {

        File theDir = new File(HISTORY_PATH + this.username);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + this.username);
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }

        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //get current date time with Date()
            Date date = new Date();
            System.out.println(dateFormat.format(date));


            PrintWriter writer = new PrintWriter(new FileOutputStream(new File(HISTORY_PATH + this.username + "/" +
                    this.username + ".txt"), true));
            if (me)
                writer.println(CurrentConfiguration.getTheConfiguration().getUsername() + ": " +
                        message + " : " + dateFormat.format(date));
            else
                writer.println(username + ": " + message + " : " + dateFormat.format(date));
            writer.close();
        } catch (Exception e) {
            ro.mta.se.chat.utils.Logger.log(Level.ERROR, "Exception occurred", e);
        }

    }

    public List<String> getStoredMessages() {

        List<String> messages ;
        try {

            Path path = FileSystems.getDefault().getPath(HISTORY_PATH + this.username, this.username + ".txt");
            //messages = (LinkedList<String>)(java.nio.file.Files.readAllLines(path));
            messages = java.nio.file.Files.readAllLines(path);

            return messages;
        }
        catch (Exception e) {

        }

        return null;
    }
}
