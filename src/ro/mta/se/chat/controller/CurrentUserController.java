package ro.mta.se.chat.controller;

import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.communication.PeerToPeerConnection;
import ro.mta.se.chat.model.CurrentConfiguration;
import ro.mta.se.chat.view.CurrentUserOptions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dani on 2/2/2016.
 */
public class CurrentUserController implements ActionListener {

    private CurrentUserOptions currentUserOptions;

    public CurrentUserController() {
        this.currentUserOptions = new CurrentUserOptions();
    }

    /**
     * Adds listeners to currentUserOptions
     */
    public void addListeners() {
        this.currentUserOptions.addListener(this);
    }

    /**
     * Gets the currentUserOptions object
     *
     * @return currentUserOptions
     */
    public CurrentUserOptions getCurrentUserOptions() {
        return this.currentUserOptions;
    }

    /**
     * Action performed
     *
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {

        String name = this.currentUserOptions.getTextName().getText();
        String ip = this.currentUserOptions.getTextIp().getText();
        String port = this.currentUserOptions.getTextPort().getText();

        CurrentConfiguration cc = CurrentConfiguration.getTheConfiguration();

        try {


            CurrentConfiguration.getTheConfiguration().setUsername(name);
            CurrentConfiguration.getTheConfiguration().setIp(ip);
            CurrentConfiguration.getTheConfiguration().setPort(port);

            DatabaseAdapter.editUser(cc.getUsername(), name, ip, port);


            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        int porti = Integer.parseInt(port);
                        if (porti > 1023)
                            PeerToPeerConnection.acceptConnection(porti);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Could not connect on port " + port,
                                "Error on connect", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            t.start();

            JOptionPane.showMessageDialog(null, "Your IP: " + ip + "\nYour port: " + port,
                    "Chat enabled", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Could not connect on port " + port,
                    "Error on connect", JOptionPane.ERROR_MESSAGE);

        }

    }
}
