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

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class Accessor implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    // --------------------------------------------------
    // Class Definition
    // --------------------------------------------------

    private static final Format DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final Format DECIMAL_SECONDS = new Format() {

            @Override
            public StringBuffer format(final Object o, final StringBuffer buff, final FieldPosition pos) {
                final long value;
                final long left;
                final long right;

                value = ((Long)o).longValue();
                left = value / 1000;
                buff.append("" + left + ".");
                right = value % 1000;
                if (right == 0) {
                    buff.append("000");
                } else if (right < 10) {
                    buff.append("00" + right);
                } else if (right < 100) {
                    buff.append("0" + right);
                } else {
                    buff.append("" + right);
                }

                return buff;
            }
            @Override
            public Object parseObject(final String source, final ParsePosition pos) {
                return Long.valueOf(source.replaceAll("\\.", ""));
            }
        };

    public static final Accessor SEVERITY = new Accessor("Severity") {

            @Override
            public Object getValue(final Event event) {
                return event.getSeverity();
            }
            @Override
            public Class getType() {
                return Severity.class;
            }
        };

    public static final Accessor TIMESTAMP = new Accessor("Timestamp") {

            {
                setFormat(DEFAULT_TIME_FORMAT);
            }

            @Override
            public Object getValue(final Event event) {
                return new Date(event.getTimestamp());
            }
            @Override
            public Class getType() {
                return Date.class;
            }
        };

    public static final Accessor ELAPSED_TIME = new Accessor("Elapsed Time", "Elapsed") {

            {
                setFormat(DECIMAL_SECONDS);
            }

            @Override
            public Object getValue(final Event event) {
                return new Long(event.getElapsedTime());
            }
            @Override
            public Class getType() {
                return Long.TYPE;
            }
        };

    public static final Accessor HAS_THROWN = new Accessor("Has Thrown", "T") {

            @Override
            public Object getValue(final Event event) {
                return Boolean.valueOf((event.getStackTrace() != null) && (event.getStackTrace().length() > 0));
            }
            @Override
            public Class getType() {
                return Boolean.class;
            }
        };

    public static final Accessor SHORT_SOURCE = new Accessor("Short Source", "Source") {

            @Override
            public Object getValue(final Event event) {
                return event.getLastSourceComponent();
            }
            @Override
            public Class getType() {
                return String.class;
            }
        };

    public static final Accessor SOURCE = new Accessor("Source") {

            @Override
            public Object getValue(final Event event) {
                return event.getSource();
            }
            @Override
            public Class getType() {
                return String.class;
            }
        };

    public static final Accessor MESSAGE = new Accessor("Message") {

            @Override
            public Object getValue(final Event event) {
                return event.getMessage();
            }
            @Override
            public Class getType() {
                return String.class;
            }
        };

    public static final Accessor NDC = new Accessor("NDC (Log4J)", "NDC") {

            @Override
            public Object getValue(final Event event) {
                return event.getNDC();
            }
            @Override
            public Class getType() {
                return String.class;
            }
        };

    //~ Instance fields --------------------------------------------------------

    // --------------------------------------------------
    // Instance Definition
    // --------------------------------------------------

    private Format _format;
    private final String _name;
    private final String _shortName;
    private final String _description;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Accessor object.
     *
     * @param  name  DOCUMENT ME!
     */
    public Accessor(final String name) {
        this(name, name);
    }

    /**
     * Creates a new Accessor object.
     *
     * @param  name       DOCUMENT ME!
     * @param  shortName  DOCUMENT ME!
     */
    public Accessor(final String name, final String shortName) {
        this(name, shortName, name);
    }

    /**
     * -------------------------------------------------- Instance Initialization
     * --------------------------------------------------
     *
     * @param  name         DOCUMENT ME!
     * @param  shortName    DOCUMENT ME!
     * @param  description  DOCUMENT ME!
     */
    public Accessor(final String name, final String shortName, final String description) {
        _name = name;
        _shortName = shortName;
        _description = description;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * -------------------------------------------------- Instance Accessors
     * --------------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return _name;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getShortName() {
        return _shortName;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDescription() {
        return _description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  format  DOCUMENT ME!
     */
    public void setFormat(final Format format) {
        _format = format;
    }

    /**
     * -------------------------------------------------- Implemenatation API
     * --------------------------------------------------
     *
     * @param   event  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract Object getValue(Event event);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract Class getType();

    /**
     * -------------------------------------------------- Public API --------------------------------------------------
     *
     * @param   event  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getString(final Event event) {
        if (_format == null) {
            return String.valueOf(getValue(event));
        } else {
            return _format.format(getValue(event));
        }
    }
}
