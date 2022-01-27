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

import java.util.ArrayList;

/**
 * Storage class for queued events
 * 
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
class LWEventQueue{
    
   /**
    * All queued events
    */
    ArrayList<LWEvent> events = new ArrayList<>();
    
   /**
    * Add a new event to the queue.
    * 
    * The inQueue property of the event will automatically
    * be set to 'true'.
    * 
    * @param e      Event that will be added
    */
    public void addEvent(LWEvent e) {
        e.queueEvent();
        this.events.add(e);
    }

   /**
    * Get all queued events
    * 
    * @return   ArrayList with all events
    */
    public ArrayList<LWEvent> getQueuedEvents() {
        return this.events;
    }
    
   /**
    * Get queued events of a specific event name
    * 
    * @param    eventName       name of the event
    * @return   queued events
    */
    public ArrayList<LWEvent> getQueuedEvents(String eventName) {
        ArrayList<LWEvent> qEvents = new ArrayList<>();
        
        for (LWEvent e : this.events) {
            if (e.getEventType().equals(eventName)) {
                qEvents.add(e);
            }
        }
        return qEvents;
    }

    /**
     * Clear the event queue
     */
    public void clearQueue() {
    	this.events.clear();
    }
}