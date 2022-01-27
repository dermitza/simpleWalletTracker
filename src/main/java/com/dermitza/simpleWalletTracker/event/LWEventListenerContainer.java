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
 * Container for an event listener that provides
 * some additional configuration for the event listener
 * 
 * Currently it only stores the the autoRemove flag,
 * but this will probably extended in future versions
 * 
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
public class LWEventListenerContainer {

    /**
     * The actual event listener
     */
    private LWEventListener listener = null;
    
    /**
     * Whether to use autoRemove
     */
    private boolean autoRemove = false;
    
    /**
     * Create a new event listener container based
     * on an event listener
     * 
     * @param listener
     */
    public LWEventListenerContainer(LWEventListener listener) {
        this.listener = listener;
    }
    
    /**
     * Enable auto-remove for this listener
     * 
     * @param enable
     */
    public void enableAutoRemove(boolean enable) {
        this.autoRemove = enable;
    }
    
    /**
     * Check, whether auto-remove has been enabled.
     * 
     * @return
     */
    public boolean isAutoRemoveEnabled() {
        return this.autoRemove;
    }

    /**
     * Get the event listener
     * 
     * @return
     */
    public LWEventListener getListener() {
        return this.listener;
    }
}