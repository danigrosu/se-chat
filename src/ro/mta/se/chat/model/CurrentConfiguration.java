package ro.mta.se.chat.model;

/**
 *
 * Created by Dani on 12/21/2015.
 */
public class CurrentConfiguration {
    private String username;
    private String ip;
    private String port;
    private byte[] sessionKey;
    private static CurrentConfiguration theConfiguration = null;

    private CurrentConfiguration(String username, String ip, String port, byte[] sessionKey){
        this.username = username;
        this.ip = ip;
        this.port = port;
        this.sessionKey = sessionKey;
    }

    public static CurrentConfiguration getTheConfiguration(){
        return theConfiguration;
    }

    public static CurrentConfiguration getTheConfiguration(String username, String ip, String port, byte[] sessionKey){
        theConfiguration = new CurrentConfiguration(username, ip, port, sessionKey);
        return theConfiguration;
    }

    public String getUsername(){
        return username;
    }
    public String getIp(){
        return ip;
    }
    public String getPort(){
        return port;
    }
    public byte[] getSessionKey(){
        return sessionKey;
    }
}
