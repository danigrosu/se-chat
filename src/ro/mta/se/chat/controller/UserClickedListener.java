package ro.mta.se.chat.controller;

import java.util.EventListener;

/**
 *
 * Created by Dani on 11/23/2015.
 */
public interface UserClickedListener extends EventListener {
    void userClickedEventOccurred(UserClickedEvent event);
}
