package ro.mta.se.chat.view;

import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.communication.PeerToPeerConnection;
import ro.mta.se.chat.controller.crypto.AESManager;
import ro.mta.se.chat.model.CurrentConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dani on 12/21/2015.
 */
public class CurrentUserOptions extends JPanel {

    private JButton startListening;
    private JTextField textName;
    private JTextField textIp;
    private JTextField textPort;

    public CurrentUserOptions() {
        Dimension size = getPreferredSize();
        size.width = 195;
        setPreferredSize(size);
        setBorder(BorderFactory.createTitledBorder("You are logged as:"));

        setLayout(new GridBagLayout());

        CurrentConfiguration cc = CurrentConfiguration.getTheConfiguration();

        JLabel labelName = new JLabel("Name: ");
        textName = new JTextField(10);
        textName.setText(cc.getUsername());


        // ip
        JLabel labelIp = new JLabel("Ip: ");
        textIp = new JTextField(10);
        textIp.setText(cc.getIp());


        GridBagConstraints gc = new GridBagConstraints();

        // port
        JLabel labelPort = new JLabel("Port: ");
        textPort = new JTextField(10);
        textPort.setText(cc.getPort());

        startListening = new JButton("Start listening");

        // first column
        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;
        add(labelName, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        add(labelIp, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        add(labelPort, gc);


        // second column
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.gridx = 1;
        gc.gridy = 0;
        add(textName, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        add(textIp, gc);

        gc.gridx = 1;
        gc.gridy = 2;
        add(textPort, gc);

        // final
        gc.weighty = 15;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        gc.gridy = 3;
        add(startListening, gc);
    }

    public void addListener(ActionListener listener) {
        this.startListening.addActionListener(listener);
    }

    public JButton getStartListening() {
        return this.startListening;
    }

    public JTextField getTextName() {
        return this.textName;
    }

    public JTextField getTextIp() {
        return this.textIp;
    }

    public JTextField getTextPort() {
        return this.textPort;
    }

}
