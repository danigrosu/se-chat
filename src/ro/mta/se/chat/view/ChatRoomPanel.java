package ro.mta.se.chat.view;

import ro.mta.se.chat.communication.PeerToPeerConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * Created by Dani on 11/24/2015.
 */
public class ChatRoomPanel extends JPanel {

    String partner;
    String ip;
    String port;

    Socket peerSocket = null;

    JTextArea textAreaPrint;
    JButton buttonSend;
    JTextArea textArea;

    PeerToPeerConnection p2p = null;

    public ChatRoomPanel(String partner, String ip, String port)
    {
        try {
            this.partner = partner;
            this.ip = ip;
            this.port = port;

            this.setLayout(new BorderLayout());

            JPanel up = new JPanel();
            up.setLayout(new BorderLayout());


            textAreaPrint = new JTextArea(15, 30);
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
            bottom.add(buttonSend, BorderLayout.EAST);

            buttonSend.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (textArea.getText().equals("")) {
                        Toolkit.getDefaultToolkit().beep();
                    } else {

                        // TODO: send message
                        p2p.sendText(textArea.getText(),peerSocket);

                        textAreaPrint.append("Me: " + textArea.getText() + "\n");
                        textArea.setText("");
                    }
                }
            });

            this.add(bottom, BorderLayout.SOUTH);
            this.add(up, BorderLayout.NORTH);


            p2p = new PeerToPeerConnection();
            peerSocket = p2p.connectToPeer(ip, Integer.parseInt(port));

            // TODO: listen for new messages


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void connectToPartner() {
        p2p = new PeerToPeerConnection();
        peerSocket = p2p.connectToPeer(ip, Integer.parseInt(port));
        System.out.println("peerSocket:" + peerSocket.getPort());
    }

    public void setSocket(Socket s) {
        this.peerSocket = s;
    }

    public String getPartner() {
        return this.partner;
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return Integer.parseInt(this.port);

    }

    public void appendTextAreaPrint(String text) {
        textAreaPrint.append(text + "\n");
    }



}
