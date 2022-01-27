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


import com.dermitza.simpleWalletTracker.util.FileUtils;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @filename GenericFileFilter.java
 * @version 0.1
 *
 * Created: 06/02/2009 rev 0.1 dermitza
 * Edited :
 *
 * Simple filter to use with a <code>JFileChooser</code>. Used to
 * save/load any extension. Common extensions are stored in
 * <code>FileExtensions</code>.
 */
public class GenericFileFilter extends FileFilter{

    private String ext;
    private String desc;

    public GenericFileFilter(){
        this("", "");
    }

    public GenericFileFilter(String ext, String desc){
        super();
        this.ext = ext;
        this.desc = desc;
    }

    public void setFilter(String ext, String desc){
        this.ext = ext;
        this.desc = desc;
    }

    /**
     * Accepts directories and files with the correct extension
     * @param f The file to check if is acceptable or not
     * @return True if the file is accepted, false otherwise
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = FileUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(ext)) {
                    return true;
            }
        }

        return false;
    }

    /**
     * Returns the description of this filter
     * @return The description of this filter
     */
    @Override
    public String getDescription() {
        return this.desc;
    }

}
