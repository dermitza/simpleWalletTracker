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
 * @filename LWEventTypes.java
 * @version 0.1
 *
 * Created: 20/03/2008 rev 0.1 dermitza
 * Edited :
 *
 *
 */
public interface LWEventTypes {
    
    /* Default event, see LWEventHandler.fireEvent() */
    public static final String DEF_EVT       = "[Default]";
    /* Network error event */
    public static final String NET_ERR_EVT   = "[Net Error]";
    /* Network status event */
    public static final String NET_STA_EVT   = "[Net Status]";
    /* User interface status event */
    public static final String UI_STA_EVT    = "[UI Status]";
    /* User interface error event */
    public static final String UI_ERR_EVT    = "[UI Error]";
    /* OK/Cancel panel event */
    public static final String UI_OKCANCEL   = "[UI OKCancel]";
    /* Core event */
    public static final String CORE_EVT      = "[Core]";
    /* Critical event */
    public static final String CRIT_EVT      = "[Critical]";
    /* Options event */
    public static final String OPT_EVT       = "[Options]";
    /* Wallet event */
    public static final String WALL_EVT = "[Wallet]";
    /* Exit event */
    public static final String EXIT_EVT      = "[Exit]";
}
