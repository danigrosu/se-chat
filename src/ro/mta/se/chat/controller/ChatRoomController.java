package ro.mta.se.chat.controller;

import ro.mta.se.chat.adapters.HistoryAdapter;
import ro.mta.se.chat.communication.PeerToPeerConnection;
import ro.mta.se.chat.utils.DataConversion;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;
import ro.mta.se.chat.view.ChatRoomPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Dani on 2/1/2016.
 */
public class ChatRoomController implements ActionListener {

    private ChatRoomPanel chatRoomPanel;
    private int i;

    /**
     * Class constructor
     *
     * @param username USername
     * @param ip       Ip
     * @param port     Port
     */
    public ChatRoomController(String username, String ip, String port) {
        this.chatRoomPanel = new ChatRoomPanel(username, ip, port);
    }

    /**
     * Function that adds listeners to chat room panel
     */
    public void addListeners() {
        this.chatRoomPanel.addActionListener(this);
    }

    /**
     * Gets the chat room panel
     *
     * @return chat room panel
     */
    public ChatRoomPanel getChatRoomPanel() {
        return this.chatRoomPanel;
    }

    /**
     * Action performed when buttons are pressed
     *
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.chatRoomPanel.getButtonSend()) {
            try {

                if (this.chatRoomPanel.getTextArea().getText().equals("")) {
                    Toolkit.getDefaultToolkit().beep();
                } else {

                    // TODO: send message
                    PeerToPeerConnection.sendText(this.chatRoomPanel.getTextArea().getText(), this.chatRoomPanel.getIp()
                            , this.chatRoomPanel.getPortS(), this.chatRoomPanel.getPartner());

                    this.chatRoomPanel.getTextAreaPrint().append("Me: " +
                            this.chatRoomPanel.getTextArea().getText() + "\n");
                    this.chatRoomPanel.getTextAreaPrint().setCaretPosition(
                            this.chatRoomPanel.getTextAreaPrint().getDocument().getLength());
                    this.chatRoomPanel.getTextArea().setText("");
                }
            } catch (Exception ex) {
                this.chatRoomPanel.getTextAreaPrint().append(ex.getMessage() + "\n");
                this.chatRoomPanel.getTextAreaPrint().setCaretPosition(
                        this.chatRoomPanel.getTextAreaPrint().getDocument().getLength());
                this.chatRoomPanel.getTextArea().setText("");
            }
        } else if (e.getSource() == chatRoomPanel.getLoadHistory()) {
            try {
                java.util.List<String> messages = HistoryAdapter.getStoredMessages(this.chatRoomPanel.getPartner());

                if (messages != null)
                    for (int i = 0; i < messages.size(); i++) {
                        this.chatRoomPanel.getTextAreaPrint().append(messages.get(i) + "\n");
                        this.chatRoomPanel.getTextAreaPrint().setCaretPosition(
                                this.chatRoomPanel.getTextAreaPrint().getDocument().getLength());
                    }
            } catch (Exception ex) {
                Logger.log(Level.ERROR, "Exception occurred", ex);
            }
        } else if (e.getSource() == chatRoomPanel.getSendFile()) {

            try {

                this.chatRoomPanel.getSendFile().setEnabled(false);
                this.chatRoomPanel.getButtonSend().setEnabled(false);

                JFileChooser openFile = new JFileChooser();
                if (openFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = openFile.getSelectedFile();

                    byte[] fileData = DataConversion.getFileBytes(file.getAbsolutePath());

                    String fileBase64Data = DataConversion.byteArrayToBase64(fileData);

                    // TODO


                    int len = fileBase64Data.length();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            i = 0;
                            int chunk = 512; // era 512
                            java.util.Timer timer = new java.util.Timer();
                            try {
                                PeerToPeerConnection.sendFileHead(file.getName(), chatRoomPanel.getIp(),
                                        chatRoomPanel.getPortS(), chatRoomPanel.getPartner());

                                Thread.sleep(1000);
                                timer.schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                chatRoomPanel.getTextAreaPrint().append("Sending [" + file.getName() + "] ... " +
                                                        String.format("%.3f", ((float) (i + 1) / len) * 100)
                                                        + " %\n");
                                            }
                                        },
                                        0,
                                        2000
                                );

                                for (; i < len; i += chunk) {
                                    if ((i + chunk) < len)
                                        PeerToPeerConnection.sendFile(fileBase64Data.substring(i, i + chunk),
                                                chatRoomPanel.getIp(), chatRoomPanel.getPortS(),
                                                chatRoomPanel.getPartner());
                                    else
                                        PeerToPeerConnection.sendFile(fileBase64Data.substring(i, len),
                                                chatRoomPanel.getIp(), chatRoomPanel.getPortS(),
                                                chatRoomPanel.getPartner());


                                }
                                timer.cancel();
                                chatRoomPanel.getTextAreaPrint().append("Sent: 100%\n");

                                chatRoomPanel.getSendFile().setEnabled(true);
                                chatRoomPanel.getButtonSend().setEnabled(true);

                                PeerToPeerConnection.sendFileTail(file.getName(), chatRoomPanel.getIp(),
                                        chatRoomPanel.getPortS(),
                                        chatRoomPanel.getPartner());
                                Thread.sleep(1000);
                            } catch (Exception ex) {
                                timer.cancel();
                                Logger.log(Level.ERROR, "File sending error", ex.getMessage());
                            }

                        }
                    }).start();


                } else {
                    chatRoomPanel.getSendFile().setEnabled(true);
                    chatRoomPanel.getButtonSend().setEnabled(true);
                }
            } catch (Exception ex) {

                Logger.log(Level.ERROR, "Exception occurred", ex.getMessage());
            }

        }
    }
}
