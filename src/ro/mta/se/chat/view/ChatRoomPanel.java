package ro.mta.se.chat.view;

import ro.mta.se.chat.adapters.HistoryAdapter;
import ro.mta.se.chat.communication.PeerToPeerConnection;
import ro.mta.se.chat.model.MessageHistory;
import ro.mta.se.chat.utils.DataConversion;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;
import sun.misc.IOUtils;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;


/**
 * Created by Dani on 11/24/2015.
 */
public class ChatRoomPanel extends JPanel {

    String partner;
    String ip;
    String port;
    int i;

    JTextArea textAreaPrint;
    JButton buttonSend;
    JTextArea textArea;

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

            DefaultCaret caret = (DefaultCaret)textAreaPrint.getCaret();
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
            JButton loadHistory = new JButton("History");
            JButton sendFile = new JButton("File");
            bottom.add(loadHistory, BorderLayout.WEST);
            bottom.add(sendFile, BorderLayout.EAST);
            this.add(buttonSend, BorderLayout.CENTER);

            sendFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    try {

                        sendFile.setEnabled(false);
                        buttonSend.setEnabled(false);

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
                                    int chunk = 512;

                                    try {
                                        PeerToPeerConnection.sendFileHead(file.getName(), ip, port, partner);

                                        java.util.Timer timer = new java.util.Timer();
                                        timer.schedule(
                                                new java.util.TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        textAreaPrint.append("Sent: " + ((float)(i + 1)/len) * 100 + "%\n");
                                                    }
                                                },
                                                0,
                                                2000
                                        );

                                        for (; i < len; i += chunk) {
                                            if ((i + chunk) < len)
                                                PeerToPeerConnection.sendFile(fileBase64Data.substring(i, i + chunk), ip, port, partner);
                                            else
                                                PeerToPeerConnection.sendFile(fileBase64Data.substring(i, len), ip, port, partner);


                                        }
                                        timer.cancel();
                                        textAreaPrint.append("Sent: 100%\n");

                                        sendFile.setEnabled(true);
                                        buttonSend.setEnabled(true);

                                        PeerToPeerConnection.sendFileTail(file.getName(), ip, port, partner);
                                    }
                                    catch (Exception ex) {
                                        Logger.log(Level.ERROR,"File sending error", ex.getMessage());
                                    }

                                }
                            }).start();


                        }
                        else {
                            sendFile.setEnabled(true);
                            buttonSend.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        Logger.log(Level.ERROR, "Exception occurred", ex);
                    }
                }
            });

            buttonSend.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    try {

                        if (textArea.getText().equals("")) {
                            Toolkit.getDefaultToolkit().beep();
                        } else {

                            // TODO: send message
                            PeerToPeerConnection.sendText(textArea.getText(), ip, port, partner);

                            textAreaPrint.append("Me: " + textArea.getText() + "\n");
                            textAreaPrint.setCaretPosition(textAreaPrint.getDocument().getLength());
                            textArea.setText("");
                        }
                    } catch (Exception ex) {
                        textAreaPrint.append(ex.getMessage() + "\n");
                        textAreaPrint.setCaretPosition(textAreaPrint.getDocument().getLength());
                        textArea.setText("");
                    }
                }
            });

            loadHistory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        java.util.List<String> messages = HistoryAdapter.getStoredMessages(partner);

                        if (messages != null)
                            for (int i = 0; i < messages.size(); i++) {
                                textAreaPrint.append(messages.get(i) + "\n");
                                textAreaPrint.setCaretPosition(textAreaPrint.getDocument().getLength());
                            }
                    } catch (Exception ex) {
                        Logger.log(Level.ERROR, "Exception occurred", ex);
                    }
                }
            });

            this.add(bottom, BorderLayout.SOUTH);
            this.add(up, BorderLayout.NORTH);


            //p2p = new PeerToPeerConnection();
            //peerSocket = p2p.connectToPeer(ip, Integer.parseInt(port));

            // TODO: listen for new messages


        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e);
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

    /**
     * @param text
     */
    public void appendTextAreaPrint(String text) {

        textAreaPrint.append(text + "\n");
        textAreaPrint.setCaretPosition(textAreaPrint.getDocument().getLength());
    }

}
