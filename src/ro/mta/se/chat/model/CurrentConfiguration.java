package ro.mta.se.chat.model;

/**
 * Created by Dani on 12/21/2015.
 */
public class CurrentConfiguration {
    private String username;
    private String ip;
    private String port;
    private boolean connected;
    private static CurrentConfiguration theConfiguration = null;

    private CurrentConfiguration(String username, String ip, String port) {
        this.username = username;
        this.ip = ip;
        this.port = port;
    }

    private CurrentConfiguration() {
        this.connected = true;
    }

    public static CurrentConfiguration getTheConfiguration() {
        if (theConfiguration == null) {
            theConfiguration = new CurrentConfiguration();
        }
        return theConfiguration;
    }

    public static CurrentConfiguration getTheConfiguration(String username, String ip, String port) {
        if (theConfiguration == null)
            theConfiguration = new CurrentConfiguration(username, ip, port);
        else {
            theConfiguration.username = username;
            theConfiguration.ip = ip;
            theConfiguration.port = port;
        }
        return theConfiguration;
    }

    public void setConnected() {
        theConfiguration.connected = true;
    }

    public boolean getConnected () {
        return theConfiguration.connected;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public static void setUsername(String username) {
        if (theConfiguration != null)
            theConfiguration.username = username;
    }

    public static void setIp(String ip) {
        if (theConfiguration != null)
            theConfiguration.ip = ip;
    }

    public static void setPort(String port) {
        if (theConfiguration != null)
            theConfiguration.port = port;
    }
}
