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


/*
 * view.TabComponents.java requires one additional file:
 *   controller.ButtonTabComponent.java
 */


package ro.mta.se.chat.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import ro.mta.se.chat.controller.*;


/*
 * Creating and using view.TabComponents example
 */ 
public class TabComponents extends JFrame {

    private static TabComponents tabComponents;

    private final JTabbedPane pane = new JTabbedPane();
    private JMenuItem tabComponentsItem;
    private JMenuItem scrollLayoutItem;


    
    private TabComponents(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        initMenu();        
        add(pane);
        tabComponentsItem.setSelected(true);
        pane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        scrollLayoutItem.setSelected(true);
        setSize(new Dimension(700, 400));
        setLocationRelativeTo(null);
        setVisible(true);

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        //setAlwaysOnTop(true);

    }

    private int exists(String title)
    {
        for( int i = 0; i < pane.getTabCount(); i++)
        {
            if(pane.getTitleAt(i).equals(title))
                return 1;
        }
        return 0;
    }

    public static TabComponents getTabComponents(String title)
    {
        if(tabComponents == null)
        {
            tabComponents = new TabComponents(title);
            tabComponents.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (JOptionPane.showConfirmDialog(tabComponents,
                            "Are you sure to close this window?", "Really Closing?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){

                        tabComponents.dispose();
                        tabComponents = null;
                    }
                    else {
                        tabComponents.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
            });
        }
        return tabComponents;
    }

    
    public void addPartner(String partner, String ip, String port) {
        if(exists(partner) == 1)
            return;
        pane.add(partner, new ChatRoomPanel(partner, ip, port));
        initTabComponent(pane.getTabCount() - 1);
    }
    
    
    private void initTabComponent(int i) {
        pane.setTabComponentAt(i, new ButtonTabComponent(pane));
    }    

    //Setting menu
    
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        //create Options menu
        scrollLayoutItem = new JCheckBoxMenuItem("Set ScrollLayout", true);
        scrollLayoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        scrollLayoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (pane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
                    pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                } else {
                    pane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
                }
            }
        });

        tabComponentsItem = new JCheckBoxMenuItem("Enable closing tabs", true);
        tabComponentsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK));
        tabComponentsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < pane.getTabCount(); i++) {
                    if (tabComponentsItem.isSelected()) {
                        initTabComponent(i);
                    } else {
                        pane.setTabComponentAt(i, null);
                    }
                }
            }
        });

        //JMenuItem resetItem = new JMenuItem("Reset JTabbedPane");
        //resetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK));
        //resetItem.addActionListener(new ActionListener() {
            //public void actionPerformed(ActionEvent e) {
            //    runTest();
            //}
       // });
       
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.add(scrollLayoutItem);
        optionsMenu.add(tabComponentsItem);

        //optionsMenu.add(resetItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }
}

