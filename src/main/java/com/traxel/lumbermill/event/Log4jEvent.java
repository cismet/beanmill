/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
  This file is part of Lumbermill.

  Lumbermill is free software; you can redistribute it
  and/or modify it under the terms of the GNU General Public
  License as published by the Free Software Foundation;
  either version 2 of the License, or (at your option) any
  later version.

  Lumbermill is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied
  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
  PURPOSE.  See the GNU General Public License for more
  details.

  You should have received a copy of the GNU General Public
  License along with Lumbermill; if not, write to the Free
  Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA 02111-1307 USA
*/
package com.traxel.lumbermill.event;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class Log4jEvent extends AbstractEvent implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    // -------------------------------------
    // Class Definition
    // -------------------------------------

    private static final Map SEVERITY_MAP;

    // -------------------------------------
    // Class Initialization
    // -------------------------------------

    static {
        final Map severityMap = new HashMap();
        severityMap.put(Level.DEBUG, Severity.LEVEL_2);
        severityMap.put(Level.INFO, Severity.LEVEL_5);
        severityMap.put(Level.WARN, Severity.LEVEL_6);
        severityMap.put(Level.ERROR, Severity.LEVEL_7);
        severityMap.put(Level.FATAL, Severity.LEVEL_8);
        SEVERITY_MAP = Collections.unmodifiableMap(severityMap);
    }

    //~ Instance fields --------------------------------------------------------

    // ------------------------------------
    // Instance Definition
    // ------------------------------------

    private final LoggingEvent LOG4J;

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------- Instance Initialization -------------------------------------
     *
     * @param  log4j  DOCUMENT ME!
     */
    public Log4jEvent(final LoggingEvent log4j) {
        LOG4J = log4j;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * --------------------------------------------- Class API --------------------------------------------
     *
     * @param   observed  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static Severity estimateSeverity(final Level observed) {
        final Iterator it;
        Level known;
        Level estimated;
        Severity severity;
        boolean moreThanObserved;
        boolean lessThanEstimated;

        estimated = Level.FATAL;
        it = SEVERITY_MAP.keySet().iterator();
        while (it.hasNext()) {
            known = (Level)it.next();
            if (estimated == null) {
                estimated = known;
            } else {
                moreThanObserved = known.toInt() > observed.toInt();
                lessThanEstimated = known.toInt() < estimated.toInt();
                if (moreThanObserved && lessThanEstimated) {
                    estimated = known;
                }
            }
        }

        severity = (Severity)SEVERITY_MAP.get(estimated);
        if (severity == null) {
            severity = Severity.LEVEL_8;
        }

        return severity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   level  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Severity getSeverity(final Level level) {
        Severity severity;

        severity = (Severity)SEVERITY_MAP.get(level);
        if (severity == null) {
            severity = estimateSeverity(level);
        }

        return severity;
    }

    // -------------------------------------
    // Event Implementation
    // -------------------------------------

    @Override
    public long getTimestamp() {
        return LOG4J.timeStamp;
    }

    @Override
    public long getElapsedTime() {
        return LOG4J.timeStamp - LOG4J.getStartTime();
    }

    @Override
    public Throwable getThrown() {
        final ThrowableInformation info;
        Throwable thrown = null;

        info = LOG4J.getThrowableInformation();
        if (info != null) {
            thrown = info.getThrowable();
        }

        return thrown;
    }

    @Override
    public String getMessage() {
        return String.valueOf(LOG4J.getMessage());
    }

    @Override
    public String getSource() {
        return LOG4J.getLoggerName();
    }

    @Override
    public Severity getSeverity() {
        return getSeverity(LOG4J.getLevel());
    }

    @Override
    public boolean hasThrown() {
        return getStackTrace() != null;
    }

    @Override
    public String getStackTrace() {
        final StringBuffer buff;
        final String[] strRep;

        strRep = LOG4J.getThrowableStrRep();
        if ((strRep == null) || (strRep.length == 0)) {
            return null;
        }
        buff = new StringBuffer();
        for (int i = 0; i < strRep.length; i++) {
            buff.append(strRep[i] + "\n");
        }
        return buff.toString();
    }

    @Override
    public String getLocation() {
        final LocationInfo info;

        info = LOG4J.getLocationInformation();
        if (info == null) {
            return null;
        }
        return info.fullInfo;
    }

    @Override
    public String getNDC() {
        return LOG4J.getNDC();
    }
}
