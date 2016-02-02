package ro.mta.se.chat.controller;

import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.view.FriendsList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dani on 2/2/2016.
 */

public class AddListListener implements ActionListener, DocumentListener {
    private boolean alreadyEnabled = false;
    private JButton button;

    private DefaultListModel listModel;
    private JList list;

    private FriendsList friendsList;

    public AddListListener(JButton button, FriendsList friendsList) {
        this.friendsList = friendsList;
        this.listModel = friendsList.getListModel();
        this.list = friendsList.getList();
        this.button = button;
    }

    /**
     * Required by ActionListener.
     *
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        String name = this.friendsList.getFriendName().getText();
        String ip = this.friendsList.getIpText().getText();
        String port = this.friendsList.getPortText().getText();

        try {
            //model.User didn't type in a unique name...
            if (name.equals("") || ip.equals("") || port.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                this.friendsList.getFriendName().requestFocusInWindow();
                this.friendsList.getFriendName().selectAll();


                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            listModel.insertElementAt(this.friendsList.getFriendName().getText(), index);

            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(friendName.getText());

            //Reset the text field.
            this.friendsList.getFriendName().requestFocusInWindow();
            this.friendsList.getFriendName().setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);

            DatabaseAdapter.addUser(name, ip, port);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * This method tests for string equality. You could certainly
     * get more sophisticated about the algorithm.  For example,
     * you might want to ignore white space and capitalization.
     *
     * @param name of friend
     * @return boolean value
     */
    protected boolean alreadyInList(String name) {
        return listModel.contains(name);
    }

    /**
     * Required by DocumentListener.
     *
     * @param e DocumentEvent
     */
    public void insertUpdate(DocumentEvent e) {
        enableButton();
    }

    /**
     * Required by DocumentListener.
     *
     * @param e DocumentEvent
     */
    public void removeUpdate(DocumentEvent e) {
        handleEmptyTextField(e);
    }

    /**
     * Required by DocumentListener.
     *
     * @param e DocumentEvent
     */
    public void changedUpdate(DocumentEvent e) {
        if (!handleEmptyTextField(e)) {
            enableButton();
        }
    }

    /**
     * Enables the button
     */
    private void enableButton() {
        if (!alreadyEnabled) {
            button.setEnabled(true);
        }
    }

    /**
     * Test if text field is empty
     *
     * @param e document event
     * @return boolean value
     */
    private boolean handleEmptyTextField(DocumentEvent e) {
        if (e.getDocument().getLength() <= 0) {
            button.setEnabled(false);
            alreadyEnabled = false;
            return true;
        }
        return false;
    }
}
