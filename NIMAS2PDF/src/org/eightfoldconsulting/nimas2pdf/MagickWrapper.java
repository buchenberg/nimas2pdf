// Copyright (C) 2009 Eightfold Consulting LLC
//
// This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
// without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with this program;
// if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.eightfoldconsulting.nimas2pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author Gregory Buchenberger
 */
public class MagickWrapper {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private static File convertFile;

    /**
     *
     * @param convertfile
     */
    public MagickWrapper(File convertfile) {
        MagickWrapper.convertFile = convertfile;
    }

    /**
     * Uses a Runtime.exec()to use imagemagick to perform the given conversion
     * operation. Returns true on success, false on failure. Does not check if
     * either file exists.
     *
     * @param in Image file for processing
     * @param out Resulting image file
     * @param width Width in pixels
     * @param height Height in pixels
     * @return Description of the Return Value
     */
    public boolean resize(File in, File out, int width, int height) {

        ArrayList<String> command = new ArrayList<String>(10);
        try {
            command.add(convertFile.getCanonicalPath());
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            return false;
        }
        command.add(in.getAbsolutePath());
        command.add("-resize");
        command.add(width + "x" + height + ">");
        //command.add("-density");
        //command.add("72");
        command.add("-quality");
        command.add("100");
        command.add(out.getAbsolutePath());
        return exec((String[]) command.toArray(new String[1]));
    }

    /**
     * Tries to exec the command, waits for it to finsih, logs errors if exit
     * status is nonzero, and returns true if exit status is 0 (success).
     *
     * @param command Command to execute
     * @return Description of the Return Value
     */
    private boolean exec(String[] command) {
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            return false;
        }
        int exitStatus;
        while (true) {
            try {
                exitStatus = proc.waitFor();
                break;
            } catch (java.lang.InterruptedException ex) {
                logger.severe(ex.getMessage());
            }
        }
        if (exitStatus != 0) {
            logger.severe("Error executing resize command on the file " + command[1] + "\nExit code: " + exitStatus);
        }
        return (exitStatus == 0);
    }
}
