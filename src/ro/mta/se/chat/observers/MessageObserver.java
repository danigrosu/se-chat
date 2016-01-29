package ro.mta.se.chat.observers;

import ro.mta.se.chat.model.MessageHistory;
import ro.mta.se.chat.view.ChatRoomPanel;
import ro.mta.se.chat.view.TabComponents;

import javax.swing.*;
import java.util.LinkedList;

/**
 *
 * Created by Dani on 1/25/2016.
 */
public class MessageObserver {

    public LinkedList<ChatRoomPanel> list = null;
    public JTabbedPane pane;
    private static MessageObserver messageObserver = null;
    private MessageObserver() {
        this.pane = TabComponents.getTabComponents("Chat room").getPane();
        this.list = TabComponents.getTabComponents("Chat room").getTabList();
    }

    /**
     *
     * @return The only observer for messages that have to exist
     */
    public static MessageObserver getMessageObserver() {
        if (messageObserver == null) {
            System.out.println("Observer created!");
            messageObserver = new MessageObserver();
        }
        return messageObserver;
    }

    public void removeListener(String username, String ip, String port) {
        if (TabComponents.getTabComponents("Chat room").isOpen(ip, Integer.parseInt(port)) != 0)
            ;// TODO
    }

    /**
     * Adds a new observer for a new chat room
     * @param username
     * @param ip
     * @param port
     */
    public void addListener(String username, String ip, String port) {
        if (TabComponents.getTabComponents("Chat room").isOpen(ip, Integer.parseInt(port)) == 0)
            TabComponents.getTabComponents("Chat room").addPartner(username, ip, port);
    }

    /**
     * Notifies a specific panel that a new message has arrived
     * @param ip
     * @param port
     * @param message
     */
    public void notifyView(String username, String ip, String port, String message) {
        int i = 0;
        for (; i < list.size(); i++) {
            ChatRoomPanel chatRoomPanel = list.get(i);
            if (chatRoomPanel.getPort() == Integer.parseInt(port) && chatRoomPanel.getIp().equals(ip)) {
                chatRoomPanel.appendTextAreaPrint(chatRoomPanel.getPartner() + ": " + message);

                MessageHistory mh = new MessageHistory(username);
                mh.storeNewMessage(message, false);

                //TabComponents.getTabComponents("Chat room").getPane().setBackgroundAt(i, UIManager.getColor("textForeground"));

                break;
            }
        }
    }

    public void notifyViewOnLeave(String ip, String port) {
        int i = 0;
        for (; i < list.size(); i++) {
            ChatRoomPanel chatRoomPanel = list.get(i);
            if (chatRoomPanel.getPort() == Integer.parseInt(port) && chatRoomPanel.getIp().equals(ip)) {
                chatRoomPanel.appendTextAreaPrint(chatRoomPanel.getPartner() + " left the chat!");


                break;
            }
        }
    }
}
