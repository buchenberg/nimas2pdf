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
import javax.swing.filechooser.*;

/**
 *
 * @author Gregory Buchenberger
 */
public class FilterOPF extends FileFilter {

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(file);
        if (extension != null) {
            if (extension.equals(Utils.opf)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "NIMAS package file (*.opf)";
    }
}

