package ro.mta.se.chat.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * Created by Dani on 11/24/2015.
 */
public class ChatRoomPanel extends JPanel {

    String partner;
    String ip;
    String port;

    JTextArea textAreaPrint;
    JButton buttonSend;
    JTextArea textArea;
    public ChatRoomPanel(String partner, String ip, String port)
    {

        this.partner = partner;
        this.ip = ip;
        this.port = port;

        this.setLayout(new BorderLayout());

        JPanel up = new JPanel();
        up.setLayout(new BorderLayout());


        textAreaPrint = new JTextArea(15,30);
        textAreaPrint.setLineWrap(true);
        textAreaPrint.setWrapStyleWord(true);
        up.add(textAreaPrint,BorderLayout.WEST);
        up.add(new JScrollPane(textAreaPrint));
        textAreaPrint.setEditable(false);



        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());

        textArea = new JTextArea(2,20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        bottom.add(textArea, BorderLayout.WEST);
        bottom.add(new JScrollPane(textArea));

        buttonSend = new JButton("SEND");
        bottom.add(buttonSend, BorderLayout.EAST);

        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textArea.getText().equals("")) {
                    Toolkit.getDefaultToolkit().beep();
                }
                else {
                    textAreaPrint.append("Me: " + textArea.getText() + "\n");
                    textArea.setText("");
                }
            }
        });

        this.add(bottom,BorderLayout.SOUTH);
        this.add(up, BorderLayout.NORTH);
    }
}
