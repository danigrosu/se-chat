package ro.mta.se.chat.model;

import java.security.PrivateKey;

/**
 * Created by Dani on 12/21/2015.
 */

/**
 * Information about logged user
 */
public class CurrentConfiguration {
    private String username;
    private String ip;
    private String port;
    private String password;
    private PrivateKey privateKey;
    private boolean connected;
    private static CurrentConfiguration theConfiguration = null;

    private CurrentConfiguration(String username, String password, String ip, String port) {
        this.connected = true;
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }

    private CurrentConfiguration() {
        this.connected = true;
    }

    /**
     * Gets the current configuration
     *
     * @return current configuration
     */
    public static CurrentConfiguration getTheConfiguration() {
        if (theConfiguration == null) {
            theConfiguration = new CurrentConfiguration();
        }
        return theConfiguration;
    }

    public void setConnected() {
        theConfiguration.connected = true;
    }

    public boolean getConnected() {
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

    public void setUsername(String username) {
        if (theConfiguration != null)
            theConfiguration.username = username;
    }

    public void setIp(String ip) {
        if (theConfiguration != null)
            theConfiguration.ip = ip;
    }

    public void setPort(String port) {
        if (theConfiguration != null)
            theConfiguration.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (theConfiguration != null)
            theConfiguration.password = password;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        if (theConfiguration != null)
            theConfiguration.privateKey = privateKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
