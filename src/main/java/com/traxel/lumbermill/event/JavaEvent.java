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

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class JavaEvent extends AbstractEvent {

    //~ Static fields/initializers ---------------------------------------------

    // -----------------------------------------
    // Class Definition
    // -----------------------------------------

    private static final Map SEVERITY_MAP;

    // -------------------------------------------
    // Class Initialization
    // -------------------------------------------

    static {
        final Map severityMap = new HashMap();
        severityMap.put(Level.FINEST, Severity.LEVEL_1);
        severityMap.put(Level.FINER, Severity.LEVEL_2);
        severityMap.put(Level.FINE, Severity.LEVEL_3);
        severityMap.put(Level.CONFIG, Severity.LEVEL_4);
        severityMap.put(Level.INFO, Severity.LEVEL_5);
        severityMap.put(Level.WARNING, Severity.LEVEL_6);
        severityMap.put(Level.SEVERE, Severity.LEVEL_8);
        SEVERITY_MAP = Collections.unmodifiableMap(severityMap);
    }

    //~ Instance fields --------------------------------------------------------

    // -------------------------------------
    // Instance Definition
    // -------------------------------------

    private final LogRecord RECORD;
    private final long RECEIVE_TIME = new Date().getTime();

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------- Instance Initialization -------------------------------------
     *
     * @param  record  DOCUMENT ME!
     */
    public JavaEvent(final LogRecord record) {
        RECORD = record;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------------ Class API ------------------------------------------
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

        estimated = Level.SEVERE;
        it = SEVERITY_MAP.keySet().iterator();
        while (it.hasNext()) {
            known = (Level)it.next();
            if (estimated == null) {
                estimated = known;
            } else {
                moreThanObserved = known.intValue() > observed.intValue();
                lessThanEstimated = known.intValue() < estimated.intValue();
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

    // ------------------------------------
    // Event Implementation
    // ------------------------------------

    @Override
    public Severity getSeverity() {
        return getSeverity(RECORD.getLevel());
    }

    @Override
    public String getSource() {
        return RECORD.getLoggerName();
    }

    @Override
    public String getMessage() {
        return RECORD.getMessage();
    }

    @Override
    public Throwable getThrown() {
        return RECORD.getThrown();
    }

    @Override
    public long getTimestamp() {
        return RECEIVE_TIME;
    }

    @Override
    public String getLocation() {
        final String clazz = RECORD.getSourceClassName();
        final String method = RECORD.getSourceMethodName();

        if ((clazz != null) || (method != null)) {
            return clazz + "." + method;
        }
        return null;
    }

    @Override
    public String getNDC() {
        return null;
    }
}
