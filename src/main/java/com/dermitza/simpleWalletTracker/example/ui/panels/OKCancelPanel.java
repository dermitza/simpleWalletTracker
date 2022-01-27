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


import com.dermitza.simpleWalletTracker.event.LWEventHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.dermitza.simpleWalletTracker.event.LWEventTypes;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @filename OkCancelPanel.java
 * @version 0.1
 *
 * Created: 17/12/2008 rev 0.1 dermitza
 * Edited :
 *
 *
 */
public class OKCancelPanel implements ActionListener{
    
    public static final String OK_TEXT = "OK";
    public static final int RET_OK = 1;
    public static final int RET_CANCEL = 0;
    public static final int RET_THIRD = 2;
    
    private JPanel panel;
    
    private JButton okButton;
    private JButton cancelButton;
    private JButton thirdButton;
    
    private String okText;
    private String thirdButtonText = null;
    
    private int retCommand;
    
    public OKCancelPanel(){
        this(OK_TEXT, null);
    }

    // set thirdButton text to null for no third button
    public OKCancelPanel(String okText, String thirdButtonText){
        this.okText = okText;
        this.thirdButtonText = thirdButtonText;
        retCommand = -1;
        initComponents();
    }
    
    public void setOKText(String text){
        this.okText = text;
        okButton.setText(text);
    }
    
    private void initComponents(){
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        okButton = new JButton(okText);
        okButton.setActionCommand("okButton");
        okButton.addActionListener(this);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancelButton");
        cancelButton.addActionListener(this);

        if(thirdButtonText != null){
            thirdButton = new JButton(thirdButtonText);
            thirdButton.setActionCommand("thirdButton");
            thirdButton.addActionListener(this);
            panel.add(thirdButton);
        }

        panel.add(cancelButton);
        panel.add(okButton);
    }
    
    public JPanel getPanel(){
        return this.panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String c = e.getActionCommand();
        switch (c) {
            case "okButton":
                retCommand = RET_OK;
                break;
            case "cancelButton":
                retCommand = RET_CANCEL;
                break;
            case "thirdButton":
                retCommand = RET_THIRD;
                break;
            default:
                break;
        }
        LWEventHandler.fireEvent(LWEventTypes.UI_OKCANCEL, retCommand);
    }

}
