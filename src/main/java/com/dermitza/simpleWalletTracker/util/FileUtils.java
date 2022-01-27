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
package com.dermitza.simpleWalletTracker.util;

import com.dermitza.simpleWalletTracker.wallet.Wallet;
import com.dermitza.simpleWalletTracker.example.ui.filefilters.FileExtensions;
import com.dermitza.simpleWalletTracker.example.ui.filefilters.GenericFileFilter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 * @filename FileUtils.java
 * @version 0.1
 *
 * Created: 16/03/2008 rev 0.1 dermitza
 * Edited : 
 *
 *
 * Collection of static file utilities.
 */
public class FileUtils {

    private static XStream x = new XStream();
    private static JFileChooser c = new JFileChooser();
    private static GenericFileFilter ff = new GenericFileFilter();

    // to disallow instantiation
    private FileUtils() {
    }

    public static String getFileSeparator(){
        return System.getProperty("file.separator");
    }

    public static String getUserHome(){
        return System.getProperty("user.home");
    }

    public static boolean fileInJAR(String path) {
        if (path.contains("!")) {
            return true;
        }
        return false;
    }

    public static String replaceURL(String path) {
        String newPath = path.replace("%20", " ");
        return newPath;
    }

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String getClassPath(String qualifiedClassName) {
        String loc = null;
        try {
            Class qc = Class.forName(qualifiedClassName);
            CodeSource source = qc.getProtectionDomain().getCodeSource();
            if (source != null) {
                URL location = source.getLocation();
                loc = location.getPath();
                System.out.println(loc);
            //System.out.println(qualifiedClassName + " : " + location);
            } else {
                //System.out.println(qualifiedClassName + " : " + "unknown source, likely rt.jar");
            }
        } catch (Exception e) {
            //System.err.println("Unable to locate class on command line.");
            //e.printStackTrace();
        }

        return loc;
    }

    //========================================================================//
    //---------------------------- LOAD METHODS ------------------------------//
    //========================================================================//
    public static <T> T loadData(String path, boolean interactive) {
        File f = new File(path);
        return FileUtils.<T>loadData(f, interactive);
    }

    public static <T> T loadData(String path) {
        File f = new File(path);
        return FileUtils.<T>loadData(f, false);
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadData(InputStream stream, String filename,
            boolean interactive) {
        try {
            T dc = (T) x.fromXML(stream);
            if (interactive) {
                JOptionPane.showMessageDialog(null, "Template successfully" +
                        " loaded from '" + filename + "'.");
            }
            return dc;
        } catch (XStreamException xe) {
            JOptionPane.showMessageDialog(null, "Error loading template" +
                    " from '" + filename + "'.");
            return null;
        }
    }

    public static <T> T interactiveLoadData(String description, int extension) {
        ff.setFilter(FileExtensions.E_EXT[extension],
                FileExtensions.E_EXT_TXT[extension]);
        c.setFileFilter(ff);

        try {
            Thread.sleep(10);
        } catch (InterruptedException ie) {
        }

        int code = c.showDialog(null, description);

        if (code == JFileChooser.APPROVE_OPTION) {
            return FileUtils.<T>loadData(c.getSelectedFile(), true);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadData(File file, boolean interactive) {
        try {
            x.allowTypes(new Class[]{Wallet.class});
            FileReader r = new FileReader(file);
            T dc = (T) x.fromXML(r);
            if (interactive) {
                JOptionPane.showMessageDialog(null, "Data successfully loaded" +
                        " from '" + file.getName() + "'.");
            }
            r.close();
            return dc;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File '" + file.getName() +
                    "' not found.");
            return null;
        } catch(IOException ioe){
            JOptionPane.showMessageDialog(null, "Could not close FileReader" +
                    " on file '" + file.getName() + "'.");
            return null;
        } catch (XStreamException xe) {
            JOptionPane.showMessageDialog(null, "Error loading data" +
                    " from '" + file.getName() + "'.");
            return null;
        }
    }

    //########################################################################//
    //---------------------------- SAVE METHODS ------------------------------//
    //########################################################################//
    public static <T> boolean saveData(T data, String description,
            int extension) {

        ff.setFilter(FileExtensions.E_EXT[extension],
                FileExtensions.E_EXT_TXT[extension]);
        c.setFileFilter(ff);

        int code = c.showDialog(null, description);

        if (code == JFileChooser.APPROVE_OPTION) {
            return FileUtils.saveData(
                    data, c.getSelectedFile(), description, extension);
        }
        return false;
    }

    public static <T> boolean saveData(T data, File file,
            String description, int extension) {
        if (file.exists()) {
            int i = JOptionPane.showConfirmDialog(null, "File '" +
                    file.getName() +
                    "' already exists. Do you want to replace it?", "",
                    JOptionPane.YES_NO_OPTION);
            if (i != JOptionPane.YES_OPTION) {
                return FileUtils.saveData(
                        data, description, extension);
            }
        }
        try {
            BufferedWriter b = new BufferedWriter(
                    new FileWriter(file, false));
            String s = x.toXML(data);
            System.out.println(s);
            b.write(s);
            b.close();
            JOptionPane.showMessageDialog(null, "Data successfully saved" +
                    " to '" + file.getName() + "'.");
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing data to '" +
                    file.getName() + "'.");
            return false;
        } catch (XStreamException xe) {
            JOptionPane.showMessageDialog(null, "Error serializing data" +
                    " to '" + file.getName() + "'.");
            return false;
        }
    }

    public static <T> boolean saveData(T data, String path) {
        try{
            File f = new File(path);
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            String s = x.toXML(data);
            bw.write(s);
            bw.close();
            return true;
        }catch(IOException ioe){
            System.err.println("Could not write to file '" + path + "'.");
            return false;
        }
    }
}
