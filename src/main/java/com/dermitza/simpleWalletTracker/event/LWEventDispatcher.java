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
import java.util.HashMap;

/**
 * Dispatcher for the events
 *
 * Stores the listener objects and notifies them.
 *
 * @author Stephan Schmidt <stephan.schmidt@schlund.de>
 */
public class LWEventDispatcher {
    
    /**
     * Stores all listeners
     */
    private final HashMap<String, LWEventListenerCollection> listeners = new HashMap<>();
    
    /**
     * Stores global listeners, that handle all events
     */
    private final LWEventListenerCollection globalListeners = new LWEventListenerCollection();
    
    /**
     * Stores all instances that previously have been created
     */
    private static final HashMap<String, LWEventDispatcher> instances = new HashMap<String,LWEventDispatcher>();
    
    /**
     * Queue that stores the triggered events so they
     * still can be propagated to event listeners that are registered
     * at a later time
     */
    private final LWEventQueue queue = new LWEventQueue();
    
    /**
     * Constructor is private.
     *
     * Use getInstance() or getDetachedInstance() instead.
     */
    private LWEventDispatcher() {
    }
    
    /**
     * Get the default dispatcher
     *
     * @return   EventDispatcher object
     */
    synchronized public static LWEventDispatcher getInstance() {
        return LWEventDispatcher.getInstance("__default");
    }
    
    /**
     * Create a new dispatcher or return an existing one,
     * if it has been created
     *
     * @param name   unique name of the dispatcher instance
     * @return       EventDispatcher object
     */
    synchronized public static LWEventDispatcher getInstance(String name) {
        if (!LWEventDispatcher.instances.containsKey(name)) {
            LWEventDispatcher.instances.put(name, new LWEventDispatcher());
        }
        return LWEventDispatcher.instances.get(name);
    }
    
    /**
     * Get an instance and do not remember it.
     *
     * @return
     */
    synchronized public static LWEventDispatcher getDetachedInstance() {
        return new LWEventDispatcher();
    }
    
    /**
     * Detach a dispatcher
     *
     * When detaching a dispatcher getInstance() will return a fresh
     * dispatcher instead of the old one.
     *
     * @param name
     * @return
     */
    synchronized public static boolean detachDispatcher(String name) {
        if (!LWEventDispatcher.instances.containsKey(name)) {
            return false;
        }
        LWEventDispatcher.instances.remove(name);
        return true;
    }
    
    /**
     * Check, whether a specified EventDispatcher instance already has been created
     *
     * @param name
     * @return
     */
    synchronized public static boolean dispatcherExists(String name) {
        return LWEventDispatcher.instances.containsKey(name);
    }
    
    /**
     * Add an event listener object
     *
     * @param eventName      name of the event to listen on
     * @param listener       instance of the event listener
     */
    public void addListener(String eventName, LWEventListener listener){
        this.addListener(eventName, listener, false);
    }
    
    /**
     * Add an event listener object
     *
     * @param eventName      name of the event to listen on
     * @param listener       instance of the event listener
     * @param autoRemove     whether to remove the listener after the first event it has handled
     */
    public void addListener(String eventName, LWEventListener listener, boolean autoRemove){
        if (!this.listeners.containsKey(eventName)) {
            this.listeners.put(eventName, new LWEventListenerCollection());
        }
        LWEventListenerCollection col = this.listeners.get(eventName);
        col.addListener(listener, autoRemove);
        
        // check the event queue
        ArrayList<LWEvent> events = this.queue.getQueuedEvents(eventName);
        
        for (LWEvent e : events) {
            this.propagate(e, false);
        }
    }
    
    /**
     * Remove an event listener
     *
     * @param eventName      name of the event
     * @param className      the class name of the listener
     * @return
     */
    public LWEventListener removeEventListener(String eventName, String className) {
        if (!this.listeners.containsKey(eventName)) {
            return null;
        }
        LWEventListenerCollection collection = (LWEventListenerCollection)this.listeners.get(eventName);
        LWEventListenerContainer container = (LWEventListenerContainer)collection.removeListener(className);
        if (container != null) {
            return container.getListener();
        }
        return null;
    }
    
    /**
     * Remove an event listener
     *
     * @param eventName      name of the event
     * @param listener       the event listener object
     * @return
     */
    public LWEventListener removeEventListener(String eventName, LWEventListener listener) {
        if (!this.listeners.containsKey(eventName)) {
            return null;
        }
        LWEventListenerCollection collection = (LWEventListenerCollection)this.listeners.get(eventName);
        LWEventListenerContainer container = (LWEventListenerContainer)collection.removeListener(listener);
        if (container != null) {
            return container.getListener();
        }
        return null;
    }
    
    /**
     * Add an event listener object
     *
     * @param listener       instance of the event listener
     */
    public void addGlobalListener(LWEventListener listener){
        this.addGlobalListener(listener, false);
    }
    
    /**
     * Add an event listener object
     *
     * @param listener       instance of the event listener
     * @param autoRemove     whether to remove the listener after the first event it has handled
     */
    public void addGlobalListener(LWEventListener listener, boolean autoRemove){
        this.globalListeners.addListener(listener, autoRemove);
        
        // check the event queue
        ArrayList<LWEvent> events = this.queue.getQueuedEvents();
        
        for (LWEvent e : events) {
            this.propagate(e, false);
        }
    }
    
    
    /**
     * Remove an event listener, that has been globally added
     *
     * @param listener       the event listener object
     * @return
     */
    public LWEventListener removeGlobalEventListener(LWEventListener listener) {
        LWEventListenerContainer container = (LWEventListenerContainer)this.globalListeners.removeListener(listener);
        if (container != null) {
            return container.getListener();
        }
        return null;
    }
    
    /**
     * Remove an event listener, that has been globally added
     *
     * @param className       the classname of the event listener
     * @return
     */
    public LWEventListener removeGlobalEventListener(String className) {
        LWEventListenerContainer container = (LWEventListenerContainer)this.globalListeners.removeListener(className);
        if (container != null) {
            return container.getListener();
        }
        return null;
    }
    
    /**
     * Trigger an event, if you already created an event object
     *
     * The Event object will not be queued.
     *
     * @param e  Event that will be triggered
     * @return   The event object
     */
    public LWEvent triggerEvent(LWEvent e){
        return this.propagate(e, false);
    }
    
    
    /**
     * Trigger an event, if you already created an event object
     *
     * @param e      Event that will be triggered
     * @param queue  Whether to queue the event
     * @return       The event object
     */
    public LWEvent triggerEvent(LWEvent e, boolean queue){
        return this.propagate(e, queue);
    }
    
    /**
     * Trigger an event that has no context information
     *
     * The Event will not be queued.
     *
     * @param name   name of the event
     * @return       The Event object
     */
    public LWEvent triggerEvent(String name){
        LWEvent e = new LWEvent(name);
        return this.propagate(e, false);
    }
    
    /**
     * Trigger an event that has no context information
     *
     * @param name   name of the event
     * @param queue  Whether to queue the event
     * @return       The Event object
     */
    public LWEvent triggerEvent(String name, boolean queue){
        LWEvent e = new LWEvent(name);
        return this.propagate(e, queue);
    }
    
    /**
     * Trigger an event with context information
     *
     * @param name      Name of the event
     * @param queue     Whether to queue the event
     * @param context   Context of the event
     * @return          The Event object
     */
    public LWEvent triggerEvent(String name, boolean queue, int context){
        LWEvent e = new LWEvent(name, context);
        return this.propagate(e, queue);
    }
    
    /**
     * Trigger an event with context and user information
     *
     * @param name      Name of the event
     * @param queue     Whether to queue the event
     * @param context   Context of the event
     * @param userInfo  Any additional information for the event
     * @return          The Event object
     */
    public LWEvent triggerEvent(String name, boolean queue, int context, String userInfo){
        LWEvent e = new LWEvent(name, context, userInfo);
        return this.propagate(e, queue);
    }
    
    /**
     * Propagate an event to all listeners that have been registered
     *
     * @param e      The event
     * @param queue  Whether you want the event to be queued or not
     * @return       The modified event
     */
    private LWEvent propagate(LWEvent e, boolean queue){
        if (this.listeners.containsKey(e.getEventType())) {
            LWEventListenerCollection col = this.listeners.get(e.getEventType());
            col.propagate(e);
        }
        
        if (e.isCancelled()) {
            return e;
        }
        
        this.globalListeners.propagate(e);
        
        if (e.isCancelled() || queue == false) {
            return e;
        }
        // add this event to the queue
        this.queue.addEvent(e);
        return e;
    }
    
    /**
     * Get the names of all events for which any listeners
     * have been added.
     *
     * @return	Set containing all event names
     */
    public String[] getRegisteredEventNames() {
        String[] a = {};
        String[] names = this.listeners.keySet().toArray(a);
        return names;
    }
    
    /**
     * Get all event listeners of the speficied event
     *
     * @param eventName
     * @return
     */
    public LWEventListenerCollection getEventListeners(String eventName) {
        if (this.listeners.containsKey(eventName)) {
            return this.listeners.get(eventName);
        }
        return new LWEventListenerCollection();
    }
    
    /**
     * Get the global event listeners
     *
     * @return
     */
    public LWEventListenerCollection getGlobalEventListeners() {
        return this.globalListeners;
    }
    
    /**
     * Remove all event listeners from this dispatcher and clear the queue
     */
    public void reset() {
        for (LWEventListenerCollection listeners : this.listeners.values()) {
            listeners.removeAllListeners();
        }
        this.globalListeners.removeAllListeners();
        this.queue.clearQueue();
    }
}