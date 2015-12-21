package ro.mta.se.chat.model;

/**
 *
 * Created by Dani on 11/23/2015.
 */
public class User {

    private String id;
    private String name;
    private String ip;
    private  String port;

    public User(String id,String name, String ip, String port)
    {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public User()
    {
        this.id = "";
        this.name = "";
        this.ip = "";
        this.port = "";
    }

    public String getName()
    {
        return name;
    }

    public  String getIp()
    {
        return ip;
    }

    public String getPort()
    {
        return  port;
    }

    public String getId()
    {
        return  id;
    }

}
