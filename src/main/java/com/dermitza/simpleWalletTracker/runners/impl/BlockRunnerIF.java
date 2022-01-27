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
package com.dermitza.simpleWalletTracker.runners.impl;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @filename BlockRunnerIF.java
 * @version 0.1
 *
 */
public interface BlockRunnerIF {
    
    /* Status messages */
    public static final int STATUS_DISC = 0; // Disconnected
    public static final int STATUS_INPR = 1; // Connection in progress
    public static final int STATUS_CONN = 2; // Connected
    public static final int STATUS_IDLE = 3; // Idle, ready to connect
    public static final int STATUS_ERR  = -1; // Error
    public static final int STATUS_BLOCK_MSG  = 5; // Error
    
    /* Info messages */
    public static final String INFO_DISC = "Disconnected.";
    public static final String INFO_INPR = "Connecting..";
    public static final String INFO_CONN = "Connected.";
    public static final String INFO_IDLE = "Ready.";
    
    /* Error messages */
    public static final String ERR_IO         = "I/O Error occurred.";
    public static final String ERR_LIST       = "Too many listeners.";
}
