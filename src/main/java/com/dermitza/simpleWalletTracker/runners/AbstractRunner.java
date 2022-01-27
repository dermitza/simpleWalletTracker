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
package com.dermitza.simpleWalletTracker.runners;

import com.dermitza.simpleWalletTracker.logging.LoggerHandler;
import java.util.logging.Logger;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 */
public abstract class AbstractRunner implements Runnable {

    private static final Logger LOGGER = 
            LoggerHandler.getLogger(AbstractRunner.class.getName());
    protected static final long WAIT_PERIOD = 200L; // 200ms wait period

    private final Object lock = new Object();
    private boolean running = false;

    public AbstractRunner() {

    }

    abstract protected void init();
    
    abstract protected void process();

    abstract protected void shutdown();

    @Override
    public void run() {
        // init
        init();

        setRunning(true);

        runLoop:
        while (running) {

            synchronized (lock) {
                
                process();
                trySleep(WAIT_PERIOD);
            }
        }
        
        shutdown();

    }

    protected void trySleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            LOGGER.config("Interrupted");
        }

    }

    public void setRunning(boolean running) {
        this.running = running;
        if (!isRunning()) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    public boolean isRunning() {
        return this.running;
    }
}
