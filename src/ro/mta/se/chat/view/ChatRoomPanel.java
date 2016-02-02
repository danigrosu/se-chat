package ro.mta.se.chat.view;

import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;


/**
 * Created by Dani on 11/24/2015.
 */
public class ChatRoomPanel extends JPanel {

    String partner;
    String ip;
    String port;
    int i;

    JTextArea textArea;
    JTextArea textAreaPrint;
    JButton buttonSend;
    JButton loadHistory;
    JButton sendFile;


    //PeerToPeerConnection p2p = new PeerToPeerConnection();

    /**
     * @param partner
     * @param ip
     * @param port
     */
    public ChatRoomPanel(String partner, String ip, String port) {
        try {
            this.partner = partner;
            this.ip = ip;
            this.port = port;

            this.setLayout(new BorderLayout());

            JPanel up = new JPanel();
            up.setLayout(new BorderLayout());


            textAreaPrint = new JTextArea(15, 30);

            DefaultCaret caret = (DefaultCaret) textAreaPrint.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            textAreaPrint.setLineWrap(true);
            textAreaPrint.setWrapStyleWord(true);
            up.add(textAreaPrint, BorderLayout.WEST);
            up.add(new JScrollPane(textAreaPrint));
            textAreaPrint.setEditable(false);

            JPanel bottom = new JPanel();
            bottom.setLayout(new BorderLayout());

            textArea = new JTextArea(2, 20);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            bottom.add(textArea, BorderLayout.WEST);
            bottom.add(new JScrollPane(textArea));

            buttonSend = new JButton("SEND");
            loadHistory = new JButton("History");
            sendFile = new JButton("File");
            bottom.add(loadHistory, BorderLayout.WEST);
            bottom.add(sendFile, BorderLayout.EAST);
            this.add(buttonSend, BorderLayout.CENTER);
            this.add(bottom, BorderLayout.SOUTH);
            this.add(up, BorderLayout.NORTH);

        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());
        }

    }

    /**
     * @return the partner in this room panel
     */
    public String getPartner() {
        return this.partner;
    }

    /**
     * @return ip of partner
     */
    public String getIp() {
        return this.ip;
    }

    /**
     * @return port of partner
     */
    public int getPort() {
        return Integer.parseInt(this.port);

    }

    public String getPortS() {
        return this.port;

    }


    /**
     * @param text
     */
    public void appendTextAreaPrint(String text) {

        textAreaPrint.append(text + "\n");
        textAreaPrint.setCaretPosition(textAreaPrint.getDocument().getLength());
    }

    /**
     * When partner leaves, close this room. You can't send him messages any more
     */
    public void closeThisRoom() {
        TabComponents.getTabComponents("Chat Room").getPane().remove(this);
        LinkedList<ChatRoomPanel> list = TabComponents.getTabComponents("Chat Room").getTabList();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIp().equals(ip) && list.get(i).getPortS().equals(port)) {
                list.remove(i);
            }
        }
    }

    /**
     * Adds listener to some objects
     *
     * @param listener Listener
     */
    public void addActionListener(ActionListener listener) {
        this.sendFile.addActionListener(listener);
        this.buttonSend.addActionListener(listener);
        this.loadHistory.addActionListener(listener);
    }

    public JButton getButtonSend() {
        return this.buttonSend;
    }

    public JButton getLoadHistory() {
        return this.loadHistory;
    }

    public JButton getSendFile() {
        return this.sendFile;
    }

    public JTextArea getTextArea() {
        return this.textArea;
    }

    public JTextArea getTextAreaPrint() {
        return this.textAreaPrint;
    }

}
