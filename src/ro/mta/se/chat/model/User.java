package ro.mta.se.chat.model;

/**
 * Created by Dani on 11/23/2015.
 */
public class User {

    private String id;
    private String name;
    private String ip;
    private String port;

    public User(String id, String name, String ip, String port) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    /**
     * Retrieves the username
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the Ip
     *
     * @return Ip as String
     */
    public String getIp() {
        return ip;
    }

    /**
     * Retrieves the port
     *
     * @return Port as String
     */
    public String getPort() {
        return port;
    }

    /**
     * Retrieves the Id
     *
     * @return Id as String
     */
    public String getId() {
        return id;
    }

    /**
     * Sets a new Id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets a new name
     *
     * @param name Username
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets a new port
     *
     * @param port Port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Sets a new Ip
     *
     * @param ip Ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

}
