package ro.mta.se.chat;

import ro.mta.se.chat.controller.*;

import javax.swing.*;

/**
 * Created by Dani on 11/22/2015.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                LoginController loginController = new LoginController();
                loginController.addListeners();

            }
        });
    }
}
