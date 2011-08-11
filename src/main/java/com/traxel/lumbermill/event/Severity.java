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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class Severity implements Comparable {

    //~ Static fields/initializers ---------------------------------------------

    // ---------------------------------------
    // Class Definition
    // ---------------------------------------
    private static final List LEVELS;
    public static final Severity INHERIT = new Inherit();
    public static final Severity ALL = new All();
    public static final Severity LEVEL_1 = new Severity(1, "Finest");
    public static final Severity LEVEL_2 = new Severity(2, "Debug");
    public static final Severity LEVEL_3 = new Severity(3, "Fine");
    public static final Severity LEVEL_4 = new Severity(4, "Config");
    public static final Severity LEVEL_5 = new Severity(5, "Info");
    public static final Severity LEVEL_6 = new Severity(6, "Warning");
    public static final Severity LEVEL_7 = new Severity(7, "Error");
    public static final Severity LEVEL_8 = new Severity(8, "Fatal");
    public static final Severity DISABLED = new Disabled();
    public static final Severity DISABLED_THROW_AWAY = new DisabledAndThrowAway();

    // ---------------------------------------
    // Class Initialization
    // ---------------------------------------
    static {
        final List levels;

        levels = new ArrayList();
        levels.add(INHERIT);
        levels.add(ALL);
        levels.add(LEVEL_1);
        levels.add(LEVEL_2);
        levels.add(LEVEL_3);
        levels.add(LEVEL_4);
        levels.add(LEVEL_5);
        levels.add(LEVEL_6);
        levels.add(LEVEL_7);
        levels.add(LEVEL_8);
        levels.add(DISABLED);
        levels.add(DISABLED_THROW_AWAY);

        LEVELS = Collections.unmodifiableList(levels);
    }

    //~ Instance fields --------------------------------------------------------

    // --------------------------------------
    // Instance Definition
    // --------------------------------------
    private final int _level;
    private final String _text;

    //~ Constructors -----------------------------------------------------------

    /**
     * -------------------------------------- Instance Initialization --------------------------------------
     *
     * @param  level  DOCUMENT ME!
     * @param  text   DOCUMENT ME!
     */
    private Severity(final int level, final String text) {
        _level = level;
        _text = text;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Severity getSeverityByString(final String s) {
        if (s.equalsIgnoreCase("ALL")) {
            return ALL;
        }
        if (s.equalsIgnoreCase("INHERIT")) {
            return INHERIT;
        }
        if (s.equalsIgnoreCase("Finest")) {
            return LEVEL_1;
        }
        if (s.equalsIgnoreCase("Debug")) {
            return LEVEL_2;
        }
        if (s.equalsIgnoreCase("Fine")) {
            return LEVEL_3;
        }
        if (s.equalsIgnoreCase("Config")) {
            return LEVEL_4;
        }
        if (s.equalsIgnoreCase("Info")) {
            return LEVEL_5;
        }
        if (s.equalsIgnoreCase("Warning")) {
            return LEVEL_6;
        }
        if (s.equalsIgnoreCase("Error")) {
            return LEVEL_7;
        }
        if (s.equalsIgnoreCase("Fatal")) {
            return LEVEL_8;
        }
        if (s.equalsIgnoreCase("Disabled")) {
            return DISABLED;
        }
        if (s.equalsIgnoreCase("Throw away")) {
            return DISABLED_THROW_AWAY;
        } else {
            return DISABLED;
        }
    }
    /**
     * --------------------------------------- Class API ---------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public static List getLevels() {
        return LEVELS;
    }
    /**
     * -------------------------------------- Instance Accessors --------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public int getLevel() {
        return _level;
    }

    @Override
    public String toString() {
        return _text;
    }

    // --------------------------------------
    // Comparable Implementation
    // --------------------------------------
    @Override
    public int compareTo(final Object o) {
        if (o == this) {
            return 0;
        }
        if (o == null) {
            return 1;
        }
        final Severity other = (Severity)o;
        return getLevel() - other.getLevel();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class Inherit extends Severity {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Inherit object.
         */
        public Inherit() {
            super(-1, "Inherit");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class All extends Severity {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new All object.
         */
        public All() {
            super(0, "All");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class Disabled extends Severity {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Disabled object.
         */
        public Disabled() {
            super(9, "Disabled");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class DisabledAndThrowAway extends Severity {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DisabledAndThrowAway object.
         */
        public DisabledAndThrowAway() {
            super(10, "Throw away");
        }
    }
}
