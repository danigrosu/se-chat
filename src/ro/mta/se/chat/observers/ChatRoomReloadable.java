package ro.mta.se.chat.observers;

import ro.mta.se.chat.view.TabComponents;

/**
 * Created by Dani on 1/27/2016.
 */
public class ChatRoomReloadable {
    /**
     * This function is called when a peer sends a message to someone who has already an open connection with him
     *
     * @param username Username
     */
    public static void reload(String username) {
        for (int i = 0; i < MessageObserver.getMessageObserver().pane.getTabCount(); i++) {
            if (MessageObserver.getMessageObserver().pane.getTitleAt(i).equals(username)) {
                MessageObserver.getMessageObserver().pane.setVisible(true);
                MessageObserver.getMessageObserver().pane.getComponentAt(i).setVisible(true);
                MessageObserver.getMessageObserver().pane.revalidate();
                TabComponents.getTabComponents("Chat room").getPane().getComponentAt(i).setVisible(true);
                TabComponents.getTabComponents("Chat room").getPane().getComponentAt(i).revalidate();
            }
        }
    }
}
