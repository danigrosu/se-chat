package ro.mta.se.chat.adapters;

import ro.mta.se.chat.model.User;
import ro.mta.se.chat.model.XmlDbParser;

import java.util.ArrayList;

/**
 * Created by Dani on 12/21/2015.
 */
public class DatabaseAdapter {


    public static void addUser(String username, String ip, String port) throws Exception {

        XmlDbParser xml = new XmlDbParser();
        xml.addUser(new User("0", username, ip, port));
    }

    public static String getUserIp(String username) throws Exception {
        XmlDbParser xml = new XmlDbParser();
        User user = xml.getUserData(username);
        return user.getIp();
    }

    public static String getUserPort(String username) throws Exception {
        XmlDbParser xml = new XmlDbParser();
        User user = xml.getUserData(username);
        return user.getPort();
    }

    public static String getUserId(String username) throws Exception {
        XmlDbParser xml = new XmlDbParser();
        User user = xml.getUserData(username);
        return user.getId();
    }

    public static void editUser(String username, String newUsername, String ip, String port) throws Exception {
        XmlDbParser xml = new XmlDbParser();
        xml.editUser(username, newUsername, ip, port);
    }

    public static User getUserData(String username) throws Exception {
        XmlDbParser xml = new XmlDbParser();
        return xml.getUserData(username);
    }

    public static ArrayList<User> getAllFriends() throws Exception {
        XmlDbParser xml = new XmlDbParser();
        return xml.getAllFriends();
    }

    public static void removeUser(String username) throws Exception {
        XmlDbParser xml = new XmlDbParser();
        xml.removeUser(username);
    }
}
