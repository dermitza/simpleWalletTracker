/**
 * This file is part of simpleWalletTracker. Copyright (C) 2022 K. Dermitzakis
 *
 * simpleWalletTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * simpleWalletTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.dermitza.simpleWalletTracker.example.ui;



import com.dermitza.simpleWalletTracker.event.LWEventHandler;
import com.dermitza.simpleWalletTracker.timer.tasks.ExitTask;
import com.dermitza.simpleWalletTracker.example.ui.panels.InfoPanel;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.dermitza.simpleWalletTracker.event.LWEventTypes;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @filename MainUI.java
 * @version 0.1
 *
 * Created: 02/03/2009 rev 0.1 dermitza
 * Edited :
 *
 *
 */
public class MainUI implements WindowListener{

    private JFrame mainFrame;
    private JPanel panel;
    private InfoPanel infoPanel;

    public MainUI() {
    }

    private void initMainFrame() {
        
        mainFrame = new JFrame("walletTracker_v0.03");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(this);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        
        infoPanel = new InfoPanel();
        infoPanel.listenTo(LWEventTypes.NET_STA_EVT);
        infoPanel.listenTo(LWEventTypes.WALL_EVT);
        infoPanel.listenTo(LWEventTypes.EXIT_EVT);
        infoPanel.setViewPreferences(false, true);

        panel.add(topPanel);
        panel.add(infoPanel.getPanel());
        
        mainFrame.add(panel);
        mainFrame.pack();
    }
    
    public void showFrames(){
        if(mainFrame == null){
            initMainFrame();
        }
        
        mainFrame.setVisible(true);
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
        // TODO, clean shutdown by stopping runner
        LWEventHandler.fireEvent(LWEventTypes.EXIT_EVT);
        //wait.. and exit
        ExitTask t = new ExitTask();
        t.startTask(1000);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }
}
