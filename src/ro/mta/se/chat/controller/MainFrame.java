package ro.mta.se.chat.controller;

import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.communication.PeerToPeerConnection;
import ro.mta.se.chat.controller.crypto.AESManager;
import ro.mta.se.chat.model.CurrentConfiguration;
import ro.mta.se.chat.model.User;
import ro.mta.se.chat.model.XmlDbParser;
import ro.mta.se.chat.observers.MessageObserver;
import ro.mta.se.chat.view.CurrentUserOptions;
import ro.mta.se.chat.view.FriendsList;
import ro.mta.se.chat.view.TabComponents;
import ro.mta.se.chat.view.UserEditOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;

/**
 * Created by Dani on 11/22/2015.
 */

//package controller;
//package view;
//package model;

public class MainFrame extends JFrame {

    private FriendsList friendsList;
    private UserEditOptions userOptions = new UserEditOptions();
    private CurrentUserOptions currentUserOptions = new CurrentUserOptions();

    private static MessageObserver messageObserver = null;

    public MainFrame() {

    }

    public MainFrame(String title) {
        super(title);

        // Set layout manager
        setLayout(new BorderLayout());


        friendsList = new FriendsList();
        Dimension size = getPreferredSize();
        size.width = 200;
        size.height = 400;
        friendsList.setPreferredSize(size);

        //userOptions = new view.UserEditOptions();

        friendsList.addUserClickedListener(new UserClickedListener() {
            public void userClickedEventOccurred(UserClickedEvent event) {
                String userClicked = event.getUser();

                userOptions.removeAll();

                User clickedUser = DatabaseAdapter.getUserData(userClicked);

                //userOptionPanel.setVisible(false);
                // name

                JLabel labelName = new JLabel("Name: ");
                JTextField textName = new JTextField(10);
                textName.setText(userClicked);


                // ip
                JLabel labelIp = new JLabel("Ip: ");
                JTextField textIp = new JTextField(10);
                textIp.setText(clickedUser.getIp());


                // port
                JLabel labelPort = new JLabel("Port: ");
                JTextField textPort = new JTextField(10);
                textPort.setText(clickedUser.getPort());

                JButton editButton = new JButton("Edit");
                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = textName.getText();
                        String ip = textIp.getText();
                        String port = textPort.getText();

                        // TODO: Edit user data
                        DatabaseAdapter.editUser(userClicked, username, ip, port);

                        for (int i = 0; i < friendsList.getListModel().size(); i++) {

                            String oldUsername = friendsList.getListModel().get(i).toString();

                            if (oldUsername.equals(userClicked)) {
                                friendsList.getListModel().remove(i);
                                friendsList.getListModel().add(i, username);
                                friendsList.revalidate();
                                break;
                            }
                        }

                    }
                });

                GridBagConstraints gc = new GridBagConstraints();

                // first column
                gc.anchor = GridBagConstraints.LAST_LINE_END;
                gc.weightx = 0.5;
                gc.weighty = 0.5;

                gc.gridx = 0;
                gc.gridy = 0;
                userOptions.add(labelName, gc);

                gc.gridx = 0;
                gc.gridy = 1;
                userOptions.add(labelIp, gc);

                gc.gridx = 0;
                gc.gridy = 2;
                userOptions.add(labelPort, gc);


                // second column
                gc.anchor = GridBagConstraints.LAST_LINE_START;
                gc.gridx = 1;
                gc.gridy = 0;
                userOptions.add(textName, gc);

                gc.gridx = 1;
                gc.gridy = 1;
                userOptions.add(textIp, gc);

                gc.gridx = 1;
                gc.gridy = 2;
                userOptions.add(textPort, gc);

                // final
                gc.weighty = 10;
                gc.anchor = GridBagConstraints.FIRST_LINE_START;
                gc.gridx = 1;
                gc.gridy = 3;
                userOptions.add(editButton, gc);

                userOptions.revalidate();

            }
        });

        friendsList.getList().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 1) {

                    friendsList.fireUserClickedEvent(new UserClickedEvent(this, list.getSelectedValue().toString()));

                } else {
                    if (evt.getClickCount() == 2) {
                        // Double-click detected
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                TabComponents chatBox = TabComponents.getTabComponents("Chat room");
                                String partner = list.getSelectedValue().toString();

                                PeerToPeerConnection.connectToPeer(partner, DatabaseAdapter.getUserIp(partner),
                                        Integer.parseInt(DatabaseAdapter.getUserPort(partner)));


                                chatBox.setVisible(true);

                                System.out.println("Connect to " + DatabaseAdapter.getUserIp(partner) + ":" +
                                        DatabaseAdapter.getUserPort(partner));
                            }
                        });
                    }
                }

            }
        });

        //friendsList.setUserOptionPanel(userOptions);

        Container c = getContentPane();

        c.add(friendsList, BorderLayout.WEST);
        c.add(userOptions, BorderLayout.SOUTH);
        c.add(currentUserOptions, BorderLayout.EAST);
    }

    public JPanel getUserOptions() {
        return userOptions;
    }

    public FriendsList getFriendsList() {
        return friendsList;
    }

}
