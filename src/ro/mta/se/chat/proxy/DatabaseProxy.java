package ro.mta.se.chat.proxy;

import ro.mta.se.chat.model.CurrentConfiguration;

/**
 * Created by Dani on 1/28/2016.
 */
public class DatabaseProxy {

    public boolean isConnected() {
        if (CurrentConfiguration.getTheConfiguration().getConnected())
            return true;
        return false;
    }
}
