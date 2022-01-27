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
package com.dermitza.simpleWalletTracker.event;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @param <T>
 * @filename LWEvent.java
 * @version 0.1
 *
 * Created: 23/03/2008 rev 0.1 dermitza
 * Edited :
 *
 *
 */
public class LWEvent<T> extends AbstractLWEvent{
    
    private int context = 0;
    private String userInfo = null;
    private T attachment = null;
    private boolean hasAttachment;
    
    /** Creates a new instance of APIEvent
     * @param type
     * @param context
     * @param userInfo */
    public LWEvent(String type, int context, String userInfo) {
        super(type);
        this.context = context;
        this.userInfo = userInfo;
        this.hasAttachment = false;
    }
    
    public LWEvent(String type, int context){
        this(type, context, null);
    }
    
    public LWEvent(String type){
        this(type, 0, null);
    }
    
    public LWEvent(String type, String userInfo){
        this(type, 0, userInfo);
    }
    
    public int getContext(){
        return this.context;
    }
    
    public String getUserInfo(){
        return this.userInfo;
    }
    
    public T getAttachment(){
        return attachment;
    }
    
    public void attach(T t){
        this.attachment = t;
        hasAttachment = true;
    }
    
    public void unattach(){
        this.attachment = null;
        hasAttachment = false;
    }
    
    public boolean hasAttachment(){
        return this.hasAttachment;
    }
}
