package ro.mta.se.chat.controller;

import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.controller.crypto.RSAKeysManager;
import ro.mta.se.chat.model.CurrentConfiguration;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;
import ro.mta.se.chat.view.Login;
import ro.mta.se.chat.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dani on 2/1/2016.
 */
public class LoginController implements ActionListener {

    private Login login;

    /**
     * Class constructor
     */
    public LoginController() {
        this.login = new Login();
    }

    /**
     * Adds listeners for login view
     */
    public void addListeners() {
        this.login.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            if (e.getSource() == login.getLoginButton()) {

                String username = login.getTextName().getText();
                String password = new String(login.getTextPass().getPassword());

                if (RSAKeysManager.login(username, password)) {

                    CurrentConfiguration currentConfiguration = CurrentConfiguration.getTheConfiguration();
                    currentConfiguration.setConnected();
                    currentConfiguration.setPassword(password);
                    currentConfiguration.setUsername(username);
                    currentConfiguration.setPrivateKey(
                            RSAKeysManager.getPrivateKey(RSAKeysManager.getHash(username, password), username));
                    currentConfiguration.setIp(DatabaseAdapter.getUserIp(username));
                    currentConfiguration.setPort(DatabaseAdapter.getUserPort(username));

                    login.dispose();

                    //JFrame frame = new MainFrame("LiveChat");
                    MainFrameController mainFrameController = new MainFrameController();


                } else {
                    JOptionPane.showMessageDialog(null, "Authentication failed!");
                    throw new Exception("Authentication failed!");

                }
            } else if (e.getSource() == login.getSignUpButton()) {

                String username = login.getTextName2().getText();
                String pass1 = new String(login.getTextPass2().getPassword());
                String pass2 = new String(login.getTextPass3().getPassword());


                if (pass1.equals(pass2) && !username.contains("-")) {

                    RSAKeysManager.createLoginToken(username, pass1);
                    DatabaseAdapter.addUser(username, "0.0.0.0", "45000");

                    JOptionPane.showMessageDialog(null, "You can now login!");

                } else
                    JOptionPane.showMessageDialog(null, "Registration failed!");
            }

        } catch (Exception ex) {
            Logger.log(Level.ERROR, "Exception occurred", ex.getMessage());
        }
    }
}
