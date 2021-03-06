package ro.mta.se.chat.model;

import ro.mta.se.chat.utils.Constants;
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

/**
 * Manages the history
 */
public class MessageHistory {

    String username;

    public MessageHistory(String username) {
        this.username = username;
    }

    /**
     * Adds new message to history
     *
     * @param message Message
     * @param me      if me=true, it is my message
     */
    public void storeNewMessage(String message, boolean me) {

        File theDir = new File(Constants.HISTORY_PATH + this.username);

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


            PrintWriter writer = new PrintWriter(new FileOutputStream(new File(Constants.HISTORY_PATH + this.username + "/" +
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

    /**
     * Retrieves all stored messages
     *
     * @return List of messages
     */
    public List<String> getStoredMessages() {

        List<String> messages;
        try {

            Path path = FileSystems.getDefault().getPath(Constants.HISTORY_PATH + this.username, this.username + ".txt");
            //messages = (LinkedList<String>)(java.nio.file.Files.readAllLines(path));
            messages = java.nio.file.Files.readAllLines(path);

            return messages;
        } catch (Exception e) {

        }

        return null;
    }
}
