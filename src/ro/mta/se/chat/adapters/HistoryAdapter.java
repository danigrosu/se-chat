package ro.mta.se.chat.adapters;

import ro.mta.se.chat.model.MessageHistory;

import java.util.List;

/**
 * Created by Dani on 1/28/2016.
 */
public class HistoryAdapter {
    public static final String HISTORY_PATH = "docs/history/";
    public static List<String> getStoredMessages(String username) {
        return new MessageHistory(username).getStoredMessages();
    }
}
