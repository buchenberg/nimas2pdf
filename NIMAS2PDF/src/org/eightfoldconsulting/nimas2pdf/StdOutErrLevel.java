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

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.util.logging.Level;

/**
 * Class defining 2 new Logging levels, one for STDOUT, one for STDERR,
 * used when multiplexing STDOUT and STDERR into the same rolling log file
 * via the Java Logging APIs.
 */
public class StdOutErrLevel extends Level {
    
    /**
     * private constructor
     * @param name name used in toString
     * @param value integer value, should correspond to something reasonable in default Level class
     */
    private StdOutErrLevel(String name, int value) {
        super(name, value);
    }
    /**
     * Level for STDOUT activity.
     */
    public static Level STDOUT = new StdOutErrLevel("STDOUT", Level.INFO.intValue()+53);
    /**
     * Level for STDERR activity
     */
    public static Level STDERR = new StdOutErrLevel("STDERR", Level.INFO.intValue()+54);

    /**
     * Method to avoid creating duplicate instances when deserializing the
     * object.
     * @return the singleton instance of this <code>Level</code> value in this
     * classloader
     * @throws ObjectStreamException If unable to deserialize
     */
    protected Object readResolve()
	throws ObjectStreamException {
        if (this.intValue() == STDOUT.intValue())
            return STDOUT;
        if (this.intValue() == STDERR.intValue())
            return STDERR;
        throw new InvalidObjectException("Unknown instance :" + this);
    }        
    
}
