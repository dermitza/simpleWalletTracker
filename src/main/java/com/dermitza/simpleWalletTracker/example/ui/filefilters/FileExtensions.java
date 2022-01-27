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
package com.dermitza.simpleWalletTracker.example.ui.filefilters;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @filename FileExtensions.java
 * @version 0.1
 *
 * Created: 06/02/2009 rev 0.1 dermitza
 * Edited :
 *
 *
 * File extensions
 */
public interface FileExtensions {
    
    public static final int    I_WALL = 0;
    public static final String E_WALL = "wall";
    public static final String E_WALL_TXT = "Wallet Data Extension (." + E_WALL + ")";

    public static final String[] E_EXT = {E_WALL};

    public static final String[] E_EXT_TXT = {E_WALL_TXT};
}
