package ro.mta.se.chat.controller;

import java.util.EventObject;

/**
 * Created by Dani on 11/23/2015.
 */
public class UserClickedEvent extends EventObject {

    private String user;

    public UserClickedEvent(Object source, String userName) {
        super(source);

        this.user = userName;
    }

    public String getUser() {
        return user;
    }
}
