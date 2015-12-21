package ro.mta.se.chat.adapters;

import ro.mta.se.chat.model.User;
import ro.mta.se.chat.model.XmlDbParser;

/**
 *
 * Created by Dani on 12/21/2015.
 */
public class DatabaseAdapter {

    public static void addUser(String username, String ip, String port){
        XmlDbParser xml = new XmlDbParser();
        xml.addUser(new User("0",username,ip,port));
    }

    public static String getUserIp(String username){
        XmlDbParser xml = new XmlDbParser();
        User user = xml.getUserData(username);
        return user.getIp();
    }

    public static String getUserPort(String username){
        XmlDbParser xml = new XmlDbParser();
        User user = xml.getUserData(username);
        return user.getPort();
    }

    public static String getUserId(String username){
        XmlDbParser xml = new XmlDbParser();
        User user = xml.getUserData(username);
        return user.getId();
    }
}
