package ro.mta.se.chat.view;

import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.controller.MainFrame;
import ro.mta.se.chat.controller.crypto.AESManager;
import ro.mta.se.chat.controller.crypto.RSAKeysManager;
import ro.mta.se.chat.model.CurrentConfiguration;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * Created by Dani on 12/2/2015.
 */
public class Login extends JFrame {

    public Login()
    {
        try {
            setLayout(new BorderLayout());
            setName("Login");

            setSize(310, 340);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
            setVisible(true);
            setResizable(false);

            BufferedImage myPicture = ImageIO.read(new File("docs/livechat.jpg"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setSize(75,25);
            add(picLabel, BorderLayout.NORTH);

            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridBagLayout());

            GridBagConstraints gc = new GridBagConstraints();

            JLabel labelName = new JLabel("Name ");
            JLabel labelPass = new JLabel("Password ");

            JTextField textName = new JTextField(10);
            JPasswordField textPass = new JPasswordField(10);

            textName.setText("Dani");
            textPass.setText("asics");

            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                try {
                    if (RSAKeysManager.login(textName.getText(), String.valueOf(textPass.getPassword()))) {

                        CurrentConfiguration currentConfiguration = CurrentConfiguration.getTheConfiguration();
                        currentConfiguration.setConnected();

                        CurrentConfiguration.getTheConfiguration(textName.getText(), DatabaseAdapter.getUserIp(
                                textName.getText()), DatabaseAdapter.getUserPort(textName.getText()));

                        JFrame frame = new MainFrame("LiveChat");
                        frame.setSize(400, 500);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setVisible(true);
                        frame.setResizable(false);
                        frame.setLocation(100, 100);
                        dispose();


                    } else {
                        JOptionPane.showMessageDialog(null, "Authentication failed!");
                        throw new Exception("Authentication failed!");

                    }

                }
                catch (Exception ex) {
                    Logger.log(Level.ERROR,"Exception occurred",ex);
                }

                }
            });
            // first column
            gc.anchor = GridBagConstraints.LAST_LINE_END;
            gc.weightx = 0.5;
            gc.weighty = 0.5;

            gc.gridx = 0;
            gc.gridy = 0;
            loginPanel.add(labelName, gc);

            gc.gridx = 0;
            gc.gridy = 1;
            loginPanel.add(labelPass, gc);


            // second column
            gc.anchor = GridBagConstraints.LAST_LINE_START;
            gc.gridx = 1;
            gc.gridy = 0;
            loginPanel.add(textName, gc);

            gc.gridx = 1;
            gc.gridy = 1;
            loginPanel.add(textPass, gc);


            // final
            gc.weighty = 10;
            gc.anchor = GridBagConstraints.FIRST_LINE_START;
            gc.gridx = 1;
            gc.gridy = 3;
            loginPanel.add(loginButton, gc);
            loginPanel.setVisible(true);

            add(loginPanel, BorderLayout.CENTER);


            JPanel signUpPanel = new JPanel();
            signUpPanel.setLayout(new GridBagLayout());

            JLabel labelName2 = new JLabel("Name ");
            JLabel labelPass2 = new JLabel("Password ");
            JLabel labelPass3 = new JLabel("Password again ");

            JTextField textName2 = new JTextField(10);
            JPasswordField textPass2 = new JPasswordField(10);
            JPasswordField textPass3 = new JPasswordField(10);

            JButton signUpButton = new JButton("Sign Up");
            signUpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newUsername = textName2.getText();
                    String newPassword = String.valueOf(textPass2.getPassword());

                    if(newUsername.contains("-")) {
                        return;
                    }

                    RSAKeysManager.createLoginToken(newUsername, newPassword);
                }
            });


            // first column
            gc.anchor = GridBagConstraints.LAST_LINE_END;
            gc.weightx = 0.5;
            gc.weighty = 0.5;

            gc.gridx = 0;
            gc.gridy = 0;
            signUpPanel.add(labelName2, gc);

            gc.gridx = 0;
            gc.gridy = 1;
            signUpPanel.add(labelPass2, gc);

            gc.gridx = 0;
            gc.gridy = 2;
            signUpPanel.add(labelPass3, gc);


            // second column
            gc.anchor = GridBagConstraints.LAST_LINE_START;
            gc.gridx = 1;
            gc.gridy = 0;
            signUpPanel.add(textName2, gc);

            gc.gridx = 1;
            gc.gridy = 1;
            signUpPanel.add(textPass2, gc);

            gc.gridx = 1;
            gc.gridy = 2;
            signUpPanel.add(textPass3, gc);


            // final
            gc.weighty = 10;
            gc.anchor = GridBagConstraints.FIRST_LINE_START;
            gc.gridx = 1;
            gc.gridy = 3;
            signUpPanel.add(signUpButton, gc);
            signUpPanel.setVisible(true);

            add(signUpPanel, BorderLayout.SOUTH);

        }
        catch (Exception e)
        {
            Logger.log(Level.ERROR, "Exception occurred", e);
        }

    }
}
