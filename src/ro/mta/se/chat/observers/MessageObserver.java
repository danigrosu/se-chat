package ro.mta.se.chat.observers;

import ro.mta.se.chat.view.ChatRoomPanel;
import ro.mta.se.chat.view.TabComponents;

import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * Created by Dani on 1/25/2016.
 */
public class MessageObserver {

    public LinkedList<ChatRoomPanel> list = null;
    private static MessageObserver messageObserver = null;

    private MessageObserver() {
        this.list = TabComponents.getTabComponents("Chat room").getTabList();
    }

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

    public void addListener(String username, String ip, String port, Socket socket, String aesKey) {
        if (TabComponents.getTabComponents("Chat room").isOpen(ip, Integer.parseInt(port)) == 0)
            TabComponents.getTabComponents("Chat room").addPartner(username, ip, port, socket, aesKey);
    }

    public void notifyView(String ip, String port, String message) {
        int i = 0;
        //System.out.println(list.size());
        for (; i < list.size(); i++) {
            ChatRoomPanel chatRoomPanel = list.get(i);
            //System.out.println(chatRoomPanel.getPartner());
            //System.out.println("P:" + ip + "  CRP:" + chatRoomPanel.getIp());
            //System.out.println("P:" + port + "  CRP:" + chatRoomPanel.getPort());
            if (chatRoomPanel.getPort() == Integer.parseInt(port) && chatRoomPanel.getIp().equals(ip)) {
                chatRoomPanel.appendTextAreaPrint(chatRoomPanel.getPartner() + ": " + message);
                break;
            }
        }
    }
}
