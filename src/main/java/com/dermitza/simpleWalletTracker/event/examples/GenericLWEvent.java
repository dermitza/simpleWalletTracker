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
 * @param <T1>
 * @param <T2>
 * @filename GenericLWEvent.java
 * @version 0.1
 *
 * Created: 23/03/2008 rev 0.1 dermitza
 * Edited :
 *
 *
 */
public class GenericLWEvent<T1,T2> extends AbstractLWEvent{
    
    private T1 context = null;
    private T2 userInfo = null;
    
    /** Creates a new instance of LWEvent
     * @param type
     * @param context
     * @param userInfo 
     */
    public GenericLWEvent(String type, T1 context, T2 userInfo) {
        super(type);
        this.context = context;
        this.userInfo = userInfo;
    }
    
    public GenericLWEvent(String type, T1 context){
        this(type, context, null);
    }
    
    public GenericLWEvent(String type){
        this(type, null, null);
    }
    
    public T1 getContext(){
        return this.context;
    }
    
    public T2 getUserInfo(){
        return this.userInfo;
    }
}