package ro.mta.se.chat.view;

import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * Created by Dani on 12/2/2015.
 */
public class Login extends JFrame {

    private JButton loginButton;
    private JButton signUpButton;

    private JTextField textName;
    private JPasswordField textPass;

    private JTextField textName2;
    private JPasswordField textPass2;
    private JPasswordField textPass3;



    public Login()
    {
        super("LiveChat login/sign up");
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

            textName = new JTextField(10);
            textPass = new JPasswordField(10);

            textName.setText("Dani");
            textPass.setText("asics");

            loginButton = new JButton("Login");

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

            textName2 = new JTextField(10);
            textPass2 = new JPasswordField(10);
            textPass3 = new JPasswordField(10);

            signUpButton = new JButton("Sign Up");

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

    public void addActionListener(ActionListener listener) {
        this.loginButton.addActionListener(listener);
        this.signUpButton.addActionListener(listener);
    }

    public JButton getLoginButton() {
        return this.loginButton;
    }

    public JButton getSignUpButton() {
        return this.signUpButton;
    }

    public JTextField getTextName() {
        return this.textName;
    }

    public JTextField getTextName2() {
        return this.textName2;
    }

    public JPasswordField getTextPass() {
        return this.textPass;
    }

    public JPasswordField getTextPass2() {
        return this.textPass2;
    }

    public JPasswordField getTextPass3() {
        return this.textPass3;
    }
}
