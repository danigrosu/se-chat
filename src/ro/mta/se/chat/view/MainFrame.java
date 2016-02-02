package ro.mta.se.chat.view;

import ro.mta.se.chat.controller.CurrentUserController;
import ro.mta.se.chat.observers.MessageObserver;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dani on 11/22/2015.
 */


public class MainFrame extends JFrame {

    /**
     * The list of friends on the left side of main frame
     */
    private FriendsList friendsList;
    /**
     * When click on a user friend, this JPanel will show up
     */
    private UserEditOptions userOptions;
    private CurrentUserOptions currentUserOptions;


    /**
     * Default class constructor
     */
    public MainFrame() {
        super("LiveChat");
        setSize(400, 500);
        setVisible(true);
        setResizable(false);
        setLocation(100, 100);

    }

    /**
     * Class constructor
     *
     * @param title This will be the title of the main frame
     */
    public MainFrame(String title) {
        super(title);

        // Set layout manager
        setLayout(new BorderLayout());

        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLocation(100, 100);

        friendsList = new FriendsList();
        Dimension size = getPreferredSize();
        size.width = 200;
        size.height = 400;
        friendsList.setPreferredSize(size);

        userOptions = new UserEditOptions();
        //currentUserOptions = new CurrentUserOptions();

        CurrentUserController userController = new CurrentUserController();
        userController.addListeners();
        currentUserOptions = userController.getCurrentUserOptions();

        Container c = getContentPane();

        c.add(friendsList, BorderLayout.WEST);
        c.add(userOptions, BorderLayout.SOUTH);
        c.add(currentUserOptions, BorderLayout.EAST);
    }


    /**
     * @return JPanel for user options
     */
    public JPanel getUserOptions() {
        return userOptions;
    }

    /**
     * @return the friendlist
     */
    public FriendsList getFriendsList() {
        return friendsList;
    }

}
