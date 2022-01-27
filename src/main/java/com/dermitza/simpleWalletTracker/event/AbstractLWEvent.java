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
 * @filename AbstractLWEvent.java
 * @version 0.1
 *
 * Created: 23/03/2008 rev 0.1 dermitza
 * Edited :
 *
 *
 */
public abstract class AbstractLWEvent {
    
    protected String type       =  null;
    protected boolean cancelled = false;
    protected boolean inQueue   = false;
    
   /**
    * Create a new event
    * 
    * @param eventType
    */
    public AbstractLWEvent(String eventType) {
        this.type = eventType;
    }
    
    /**
     * Not allowing no-arg constructor
     *
     */
    private AbstractLWEvent() {}
    
    
    /**
    * Cancel the event
    */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * Flag the event as queued
     */
     public void queueEvent() {
         this.inQueue = true;
     }
    
   /**
    * Check, whether the event has been cancelled
    * 
    * @return       true, if the event has been cancelled, false otherwise
    */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Check, whether the event already is in a queue
     * 
     * @return       true, if the event is queued, false otherwise
     */
     public boolean isQueued() {
         return this.inQueue;
     }
    
   /**
    * Get the type of the event
    * 
    * @return   the event type
    */
    public String getEventType() {
        return this.type;
    }
}
