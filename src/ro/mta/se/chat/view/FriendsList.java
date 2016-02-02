/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ro.mta.se.chat.view;
//package controller;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.EventListenerList;

import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.controller.*;
import ro.mta.se.chat.model.User;
import ro.mta.se.chat.model.XmlDbParser;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;

/* view.FriendsList.java requires no other files. */
public class FriendsList extends JPanel implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;

    private static final String addString = "Add";
    private static final String removeString = "Remove";
    private JButton removeButton;
    private JTextField friendName;
    private JTextField ipText;
    private JTextField portText;


    private EventListenerList listenerList = new EventListenerList();

    public FriendsList() {
        super(new BorderLayout());


        try {
            ArrayList<User> friends = DatabaseAdapter.getAllFriends();

            listModel = new DefaultListModel();

            for (int i = 0; i < friends.size(); i++) {
                listModel.addElement(friends.get(i).getName());
            }


            //Create the list and put it in a scroll pane.
            list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);
            list.addListSelectionListener(this);
            list.setVisibleRowCount(5);
            JScrollPane listScrollPane = new JScrollPane(list);

            JButton addButton = new JButton(addString);

            AddListListener addListener = new AddListListener(addButton, this);
            addButton.setActionCommand(addString);
            addButton.addActionListener(addListener);
            addButton.setEnabled(false);

            removeButton = new JButton(removeString);
            removeButton.setActionCommand(removeString);
            removeButton.addActionListener(new RemoveListListener(this));

            JPanel friendPanel = new JPanel();
            friendPanel.setLayout(new BoxLayout(friendPanel, BoxLayout.LINE_AXIS));

            friendName = new JTextField(20);
            friendName.addActionListener(addListener);
            friendName.getDocument().addDocumentListener(addListener);
            JLabel lab1 = new JLabel("Name:");
            String name = listModel.getElementAt(list.getSelectedIndex()).toString();

            friendPanel.add(lab1);
            friendPanel.add(friendName);

            JPanel ipPanel = new JPanel();
            ipPanel.setLayout(new BoxLayout(ipPanel,
                    BoxLayout.LINE_AXIS));

            ipText = new JTextField(15);
            //ipText.addActionListener(addListener);
            //ipText.getDocument().addDocumentListener(addListener);
            JLabel lab2 = new JLabel("IP:");

            ipPanel.add(lab2);
            ipPanel.add(ipText);

            JPanel portPanel = new JPanel();
            portPanel.setLayout(new BoxLayout(portPanel, BoxLayout.LINE_AXIS));

            portText = new JTextField(6);
            JLabel lab3 = new JLabel("Port:");

            portPanel.add(lab3);
            portPanel.add(portText);


            //Create a panel that uses BoxLayout.
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
            buttonPane.add(removeButton);
            buttonPane.add(Box.createHorizontalStrut(5));
            buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
            buttonPane.add(Box.createHorizontalStrut(5));
            buttonPane.add(friendPanel);
            buttonPane.add(ipPanel);
            buttonPane.add(portPanel);


            buttonPane.add(addButton);
            buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            add(listScrollPane, BorderLayout.CENTER);
            add(buttonPane, BorderLayout.PAGE_END);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * @param event
     */
    public void fireUserClickedEvent(UserClickedEvent event) {
        Object[] listeners = listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == UserClickedListener.class) {
                ((UserClickedListener) listeners[i + 1]).userClickedEventOccurred(event);
            }
        }
    }

    /**
     * @param listener
     */
    public void addUserClickedListener(UserClickedListener listener) {
        listenerList.add(UserClickedListener.class, listener);
    }

    /**
     * @param listener
     */
    public void removeUserClickedListener(UserClickedListener listener) {
        listenerList.remove(UserClickedListener.class, listener);
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                //No selection, disable remove button.
                removeButton.setEnabled(false);

            } else {
                //Selection, enable the remove button.
                removeButton.setEnabled(true);
            }
        }
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public JList getList() {
        return list;
    }

    public JTextField getFriendName() {
        return this.friendName;
    }

    public JTextField getIpText() {
        return this.ipText;
    }

    public JTextField getPortText() {
        return this.portText;
    }

    public JButton getRemoveButton() {
        return this.removeButton;
    }


}
