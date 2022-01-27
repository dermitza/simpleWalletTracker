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
package com.dermitza.simpleWalletTracker.event.examples;

import com.dermitza.simpleWalletTracker.event.AbstractLWEvent;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @filename LWEvent.java
 * @version 0.1
 *
 * Created: 23/03/2008 rev 0.1 dermitza
 * Edited :
 *
 *
 */
public class LWEvent extends AbstractLWEvent{
    
    public static final int DEFAULT_CONTEXT  = -1;
    public static final int DEFAULT_USERINFO = -1;
    
    private int context;
    private int userInfo;
    
    /** Creates a new instance of LWAPIEvent
     * @param type 
     */
    public LWEvent(String type) {
        this(type, DEFAULT_CONTEXT, DEFAULT_USERINFO);
    }
    
    public LWEvent(String type, int context){
        this(type, context, DEFAULT_USERINFO);
    }
    
    public LWEvent(String type, int context, int userInfo){
        super(type);
        this.context = context;
        this.userInfo = userInfo;
    }

    public int getContext() {
        return this.context;
    }

    public int getUserInfo() {
        return this.userInfo;
    }
    
}
