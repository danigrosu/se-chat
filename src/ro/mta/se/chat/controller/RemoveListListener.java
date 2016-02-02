package ro.mta.se.chat.controller;

import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;
import ro.mta.se.chat.view.FriendsList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dani on 2/2/2016.
 */
public class RemoveListListener implements ActionListener {

    private FriendsList friendsList;

    public RemoveListListener(FriendsList friendsList) {
        this.friendsList = friendsList;
    }

    public void actionPerformed(ActionEvent e) {
        //This method can be called only if
        //there's a valid selection
        //so go ahead and remove whatever's selected.

        String name = this.friendsList.getListModel().getElementAt(this.friendsList.getList().getSelectedIndex()).toString();

        int index = this.friendsList.getList().getSelectedIndex();
        this.friendsList.getListModel().remove(index);

        try {

            DatabaseAdapter.removeUser(name);

            int size = this.friendsList.getListModel().getSize();

            if (size == 0) { //Nobody's left, disable firing.
                this.friendsList.getRemoveButton().setEnabled(false);

            } else { //Select an index.
                if (index == this.friendsList.getListModel().getSize()) {
                    //removed item in last position
                    index--;
                }

                this.friendsList.getList().setSelectedIndex(index);
                this.friendsList.getList().ensureIndexIsVisible(index);
            }
        } catch (Exception ex) {
            Logger.log(Level.ERROR, "User remove failed", ex.getMessage());
        }

    }
}
