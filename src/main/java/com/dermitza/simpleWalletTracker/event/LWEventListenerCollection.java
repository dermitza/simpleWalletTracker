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
import java.util.Iterator;

/**
 * Stores all event listeners for one event
 * 
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
public class LWEventListenerCollection {

   /**
    * All event listeners
    */
    private final ArrayList<LWEventListenerContainer> listeners = new ArrayList<>();
    
   /**
    * Add a new event listener to the collection
    * 
    * @param listener       Listener to add
     * @param autoRemove
    * @return               amount of event listeners in this collection
    */
    public int addListener(LWEventListener listener, boolean autoRemove) {

        LWEventListenerContainer container = new LWEventListenerContainer(listener);
        container.enableAutoRemove(autoRemove);
        
        this.listeners.add(container);
        return this.listeners.size();
    }

   /**
    * Propagate an event to all listeners in this collection
    * 
    * @param e      event to propagate
    * @return       event
    */
    public LWEvent propagate(LWEvent e) {
        ArrayList<LWEventListener> remove = new ArrayList<>();
        for (int i = 0; i < this.listeners.size(); i++) {
            LWEventListenerContainer container = (
                    LWEventListenerContainer)this.listeners.get(i);
            container.getListener().handleEvent(e);
            
            // remove the listener
            if (container.isAutoRemoveEnabled()) {
                remove.add(container.getListener());
            }
            if (e.isCancelled()) {
                break;
            }
        }
        // remove the listeners that have been set to autoRemove
        for (LWEventListener listener : remove) {
            this.removeListener(listener);
        }
        return e;
    }

   /**
    * Remove a listener from the list
    * 
    * @param index  index of the event listener   
    * @return
    */
    public LWEventListenerContainer removeListener(int index) {
        return (LWEventListenerContainer)this.listeners.remove(index);
    }

    /**
     * Remove a listener of a specified class from the list
     * 
     * @param className
     * @return
     */
    public LWEventListenerContainer removeListener(String className) {
        for (LWEventListenerContainer container : this.listeners) {
            if (container.getListener().getClass().getName().equals(className)) {
                this.listeners.remove(container);
                return container;
            }
        }
        return null;
    }
    
    /**
     * Remove a listener from the list
     * 
     * If a listener has been added more the once, only the
     * first listener is removed
     * 
     * @param listener      listener to remove   
     * @return
     */
     public LWEventListenerContainer removeListener(LWEventListener listener) {
        for (LWEventListenerContainer container : this.listeners) {
            if (container.getListener().equals(listener)) {
                this.listeners.remove(container);
                return container;
            }
        }
         return null;
     }

     /**
      * Remove all listeners from this collection
      */
     public void removeAllListeners() {
    	 this.listeners.clear();
     }
     
     /**
      * Get an iterator to iterate over the event listeners in this
      * Collection
      * 
      * @return
      */
     public Iterator iterator() {
         return this.listeners.iterator();
     }
}