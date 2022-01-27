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
 * @filename LWEventHandler.java
 * @version 0.1

 Created: 19/03/2008 rev 0.1 dermitza
 Edited :


 An EventHandler is a lightweight encapsulation of the LWEventDispatcher and
 LWEvent classes. It allows for easy creation and manipulation of events.
 *
 */
public class LWEventHandler implements LWEventTypes{
    
    private static final LWEventDispatcher ed = LWEventDispatcher.getInstance();
    
    private LWEventHandler() {} /* Disallow instantiation */
    
    public static void addListener(String eventType, LWEventListener listener){
        addListener(eventType, listener, false);
    }
    
    public static void addListener(String eventType, LWEventListener listener,
            boolean autoRemove){
        ed.addListener(eventType, listener, autoRemove);
    }
    
    public static void removeListener(String eventType, LWEventListener listener){
        ed.removeEventListener(eventType, listener);
    }
    
    public static LWEventDispatcher getEventDispatcher(){
        return ed;
    }
    
    //-------------------------- NORMAL EVENTS --------------------------------
    
    public static void fireEvent(String eventType, int context, String userInfo){
        ed.triggerEvent(eventType, false, context, userInfo);
    }
    
    public static void fireEvent(String eventType, int context){
        ed.triggerEvent(eventType, false, context);
    }
    
    public static void fireEvent(String eventType){
        ed.triggerEvent(eventType);
    }
    
    public static void fireEvent(){
        ed.triggerEvent(DEF_EVT);
    }
    
    //--------------------------- EVENTS WITH ATTACHMENTS ---------------------
    
    public static <T> void attachAndFire(String eventType, int context, String userInfo, T attachment){
        LWEvent<T> e = new LWEvent<>(eventType, context, userInfo);
        e.attach(attachment);
        ed.triggerEvent(e);
    }
    
    public static <T> void attachAndFire(String eventType, int context, T attachment){
        LWEvent<T> e = new LWEvent<>(eventType, context);
        e.attach(attachment);
        ed.triggerEvent(e);
    }
    
    public static <T> void attachAndFire(String eventType, T attachment){
        LWEvent<T> e = new LWEvent<>(eventType);
        e.attach(attachment);
        ed.triggerEvent(e);
    }
    
    public static <T> void attachAndFire(T attachment){
        LWEvent<T> e = new LWEvent<>(DEF_EVT);
        e.attach(attachment);
        ed.triggerEvent(e);
    }
}
