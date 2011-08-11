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

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import org.openide.util.NbPreferences;

import java.io.Serializable;

import java.util.List;
import java.util.logging.LogRecord;

import de.cismet.beanmill.NetbeansPanel;

import de.cismet.tools.StaticHtmlTools;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class Event implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(Event.class);

    //~ Instance fields --------------------------------------------------------

    private StringBuilder html;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   object  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected static Event create(final Object object) {
        final Event event;
        if (object == null) {
            event = null;
        } else if (object instanceof LogRecord) {
            event = new JavaEvent((LogRecord)object);
        } else if (object instanceof LoggingEvent) {
            event = new Log4jEvent((LoggingEvent)object);
        } else {
            event = null;
        }
        return event;
    }

    /**
     * A number on the range 0 - 1.
     *
     * @return  DOCUMENT ME!
     */
    public abstract Severity getSeverity();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getSource();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract List getSourceComponents();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getLastSourceComponent();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getMessage();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract Throwable getThrown();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract long getTimestamp();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract long getElapsedTime();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract boolean hasThrown();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getStackTrace();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getLocation();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getNDC();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getHtml() {
        html = new StringBuilder();

        if (getLocation() != null) {
            insert("Location", getLocation(), false);
        }
        if (NbPreferences.forModule(Event.class).getBoolean(NetbeansPanel.PROP_SHOW_HTML_MSG, true)) {
            insert(this, Accessor.MESSAGE, true, false);
        }
        insert(this, Accessor.NDC, false, false);
        if (getStackTrace() != null) {
            insertStackTrace("Stack Trace", getStackTrace());
        }
        if (NbPreferences.forModule(Event.class).getBoolean(NetbeansPanel.PROP_SHOW_RAW_MSG, true)) {
            insert(this, Accessor.MESSAGE, true, true);
        }

        return html.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  event       DOCUMENT ME!
     * @param  accessor    DOCUMENT ME!
     * @param  showIfNull  DOCUMENT ME!
     * @param  raw         DOCUMENT ME!
     */
    private void insert(final Event event, final Accessor accessor, final boolean showIfNull, final boolean raw) {
        if (showIfNull || (accessor.getValue(event) != null)) {
            insert(accessor.getName(), accessor.getString(event), raw);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  header      DOCUMENT ME!
     * @param  stackTrace  DOCUMENT ME!
     */
    private void insertStackTrace(final String header, final String stackTrace) {
        final StringBuilder stackTraceS = new StringBuilder();
        boolean first = true;
        for (final String line : stackTrace.split("\n")) {   // NOI18N
            String pre = "        &nbsp;";                   // NOI18N
            if (!first) {
                pre = "	&nbsp;	&nbsp;	&nbsp;";               // NOI18N
            } else {
                first = false;
            }
            final String filtered = filterLineCommand(line);
            if (filtered != null) {
                stackTraceS.append(pre).append("<a href=\"") // NOI18N
                .append(filtered).append("\">")              // NOI18N
                .append(htmlReady(line)).append("</a><br>"); // NOI18N
            } else {
                stackTraceS.append(pre).append(line).append("<br>");
            }
        }

        html.append("<b><span style=\"font-family:sans-serif; font-size:115%\">")                       // NOI18N
        .append(header).append("</span></b><br><span style=\"font-family:sans-serif; font-size:80%\">") // NOI18N
        .append(stackTraceS).append("<br></span><br>");                                                 // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param  header   DOCUMENT ME!
     * @param  message  DOCUMENT ME!
     * @param  raw      DOCUMENT ME!
     */
    private void insert(final String header, final String message, final boolean raw) {
        final String filtered = filterLineCommand(message);
        final String modMsg;
        if (filtered != null) {
            modMsg = "<a href=\"" + filtered + "\">" + htmlReady(message) + "</a>"; // NOI18N
        } else {
            modMsg = message.replace("<html>", "").replace("</html>", "");          // NOI18N
        }

        html.append("<b><span style=\"font-family:sans-serif; font-size:115%\">")                               // NOI18N
        .append(raw ? ("Raw " + header) : header).append("</span></b><br><span style=\"font-family:sans-serif") // NOI18N
        .append("; font-size:90%\">")                                                                           // NOI18N
        .append(raw ? StaticHtmlTools.stringToHTMLString(modMsg) : modMsg).append("</span><br><br>");           // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param   line  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String htmlReady(final String line) {
        return line.replaceAll("<", "&lt;").replaceAll(">", "&gt;"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param   line  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String filterLineCommand(final String line) {
        if (line.matches(".*.java:.*")) {                              // NOI18N
            try {
                String classname = "";                                 // NOI18N
                classname = line.replaceAll(".*\\(", "");              // NOI18N
                final String end = classname.replaceAll("\\).*", "");  // NOI18N
                String[] ar = end.split("\\.java:");                   // NOI18N
                final String linenumber = ar[1];
                classname = ar[0];
                final String start = new String(line.substring(0, line.lastIndexOf(end) - 1));
                ar = start.split(" ");                                 // NOI18N
                String path = ar[ar.length - 1];
                path = path.replaceAll("\\$", ".");                    // NOI18N
                ar = path.split("\\.");                                // NOI18N
                path = "";                                             // NOI18N
                boolean join = false;
                for (int i = ar.length - 1; i >= 0; --i) {
                    if (ar[i].equals(classname)) {
                        join = true;
                    }
                    if (join) {
                        path = ar[i] + "." + path;
                    }
                }
                try {
                    if (path.length() > 0) {
                        path = new String(path.substring(0, path.length() - 1));
                    }
                    return path + ":" + linenumber;                    // NOI18N
                } catch (final Exception e) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("path processing issue: " + path, e); // NOI18N
                    }

                    return path;
                }
            } catch (final Exception e) {
                LOG.error("could not process line: " + line, e); // NOI18N

                return null;
            }
        } else {
            return null;
        }
    }
}
