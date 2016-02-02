package ro.mta.se.chat.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dani on 11/23/2015.
 */

/**
 * This view will show the information of a user
 */
public class UserEditOptions extends JPanel {


    public UserEditOptions() {
        Dimension size = getPreferredSize();
        size.width = 195;
        size.height = 120;
        setPreferredSize(size);
        setBorder(BorderFactory.createTitledBorder("User Options"));

        setLayout(new GridBagLayout());
    }


}
