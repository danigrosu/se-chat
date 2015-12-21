package ro.mta.se.chat.view;

import ro.mta.se.chat.model.CurrentConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * Created by Dani on 12/21/2015.
 */
public class CurrentUserOptions extends JPanel {

    public CurrentUserOptions() {
        Dimension size = getPreferredSize();
        size.width = 195;
        setPreferredSize(size);
        setBorder(BorderFactory.createTitledBorder("You are logged as:"));

        setLayout(new GridBagLayout());

        CurrentConfiguration cc = CurrentConfiguration.getTheConfiguration();

        JLabel labelName = new JLabel("Name: ");
        JTextField textName = new JTextField(10);
        textName.setText(cc.getUsername());


        // ip
        JLabel labelIp = new JLabel("Ip: ");
        JTextField textIp = new JTextField(10);
        textIp.setText(cc.getIp());


        // port
        JLabel labelPort = new JLabel("Port: ");
        JTextField textPort = new JTextField(10);
        textPort.setText(cc.getPort());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textName.getText();
                String ip = textIp.getText();
                String port = textPort.getText();

                // TODO: Edit user data
            }
        });


        GridBagConstraints gc = new GridBagConstraints();

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
        gc.weighty = 10;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        gc.gridy = 3;
        add(editButton, gc);
    }

}
