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
package com.dermitza.simpleWalletTracker.example.ui.panels;

import com.dermitza.simpleWalletTracker.SimpleWalletTrackerMain;
import com.dermitza.simpleWalletTracker.event.LWEvent;
import com.dermitza.simpleWalletTracker.event.LWEventHandler;
import com.dermitza.simpleWalletTracker.util.TimeUtil;
import java.awt.Dimension;
import java.util.HashSet;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import com.dermitza.simpleWalletTracker.event.LWEventTypes;
import com.dermitza.simpleWalletTracker.event.LWEventListener;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @filename InfoPanel.java
 * @version 0.1
 *
 * Created: 12/12/2008 rev 0.1 dermitza Edited :
 *
 * A JPanel implementing a JTextField that listens to and displays assigned
 * events.
 */
public class InfoPanel implements LWEventListener {

    private JPanel panel;
    private JTextArea infoArea;
    private final HashSet<String> eventTypes;
    private TitledBorder border;
    private String title;

    private boolean viewEventType = true;
    private boolean viewUserInfo = true;

    public InfoPanel() {
        eventTypes = new HashSet<>();
    }

    private void initComponents() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        border = new TitledBorder("Message Log");
        if (title != null) {
            border.setTitle(title);
        }
        panel.setBorder(border);

        infoArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret) infoArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        infoArea.setEditable(false);
        infoArea.setText(SimpleWalletTrackerMain.BEG + "\n\n");

        JScrollPane bar = new JScrollPane(infoArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Dimension d = new Dimension(1024, 240);
        bar.setMinimumSize(d);
        bar.setPreferredSize(d);
        bar.setSize(d);

        panel.add(bar);
    }

    public void setViewPreferences(boolean viewEventType, boolean viewUserInfo) {
        this.viewEventType = viewEventType;
        this.viewUserInfo = viewUserInfo;
    }

    /**
     * Set the title of the underlying JPanel. The title is shown on a Titled
     * Border.
     *
     * @param title the title of the underlying JPanel
     */
    public void setTitle(String title) {
        this.title = title;
        if (border != null) {
            border.setTitle(title);
        }
    }

    /**
     * Get the underlying JPanel.
     *
     * @return the underlying JPanel
     */
    public JPanel getPanel() {
        if (panel == null) {
            initComponents();
        }
        return this.panel;
    }

    /**
     * Assign a particular event type to listen to. Event Types are defined in
     * APIEventTypes.java
     *
     * @param eventType The event type to listen to
     * @return true if we are registered as a listener. False if we are already
     * listening on events of that type.
     */
    public boolean listenTo(String eventType) {
        if (eventTypes.add(eventType)) {
            LWEventHandler.addListener(eventType, this);
            //System.out.println("added");
            return true;
        }
        return false;
    }

    /**
     * Stop listening to some event type
     *
     * @param eventType The event type to stop listening to
     * @return true if we have stopped listening to that type of event. False if
     * we were never a listener of that type of event.
     */
    public boolean stopListeningTo(String eventType) {
        if (eventTypes.contains(eventType)) {
            eventTypes.remove(eventType);
            LWEventHandler.removeListener(eventType, this);
            return true;
        }
        return false;
    }

    /**
     * Handle a particular event. This method is called from the
     * APIEventDispatcher on event types we are a listener of. This method
     * overrides the APIEventListener method. In this implementation, all we do
     * is show the events that we are listening to on a GUI window.
     *
     * @param e The event fired.
     */
    @Override
    public void handleEvent(LWEvent e) {
        String temp = infoArea.getText();

        if (e.getEventType().equals(LWEventTypes.WALL_EVT)) {
            temp += e.getUserInfo();
        } else {
            temp += " "
                    + TimeUtil.getTimeStamp("yyyy/MM/dd HH:mm:ss");

            if (viewEventType) {
                temp += " " + e.getEventType();
            }

            if (viewUserInfo) {
                temp += " " + e.getUserInfo();
            }
        }
        temp += "\n";
        infoArea.setText(temp);
    }
}
