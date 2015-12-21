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

/* view.FriendsList.java requires no other files. */
public class FriendsList extends JPanel
        implements ListSelectionListener {
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

        ArrayList<User> friends = new XmlDbParser().getAllFriends();

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
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());

        JPanel friendPanel = new JPanel();
        friendPanel.setLayout(new BoxLayout(friendPanel, BoxLayout.LINE_AXIS));

        friendName = new JTextField(20);
        friendName.addActionListener(addListener);
        friendName.getDocument().addDocumentListener(addListener);
        JLabel lab1 = new JLabel("Name: ");
        String name = listModel.getElementAt(list.getSelectedIndex()).toString();

        friendPanel.add(lab1);
        friendPanel.add(friendName);

        JPanel ipPanel = new JPanel();
        ipPanel.setLayout(new BoxLayout(ipPanel,
                BoxLayout.LINE_AXIS));

        ipText = new JTextField(15);
        //ipText.addActionListener(addListener);
        //ipText.getDocument().addDocumentListener(addListener);
        JLabel lab2 = new JLabel("IP: ");

        ipPanel.add(lab2);
        ipPanel.add(ipText);

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new BoxLayout(portPanel, BoxLayout.LINE_AXIS));

        portText = new JTextField(6);
        JLabel lab3 = new JLabel("Port: ");

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



    }


    public void fireUserClickedEvent(UserClickedEvent event){
        Object[] listeners = listenerList.getListenerList();

        for(int i=0;i<listeners.length;i++){
            if(listeners[i] == UserClickedListener.class){
                ((UserClickedListener)listeners[i+1]).userClickedEventOccurred(event);
            }
        }
    }

    public void addUserClickedListener( UserClickedListener listener){
        listenerList.add(UserClickedListener.class, listener);
    }

    public void removeUserClickedListener( UserClickedListener listener){
        listenerList.remove(UserClickedListener.class, listener);
    }


    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    //This listener is shared by the text field and the add button.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = friendName.getText();
            String ip = ipText.getText();
            String port = portText.getText();

            //model.User didn't type in a unique name...
            if (name.equals("") || ip.equals("") || port.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                friendName.requestFocusInWindow();
                friendName.selectAll();



                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            listModel.insertElementAt(friendName.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(friendName.getText());

            //Reset the text field.
            friendName.requestFocusInWindow();
            friendName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);

            DatabaseAdapter.addUser(name, ip, port);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
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

    public JList getList()
    {
        return list;
    }



}
