/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.beanmill.filter;

import com.traxel.lumbermill.event.Event;

import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class ActiveSelectionFilter extends DefaultFilter {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(
            ActiveSelectionFilter.class);

    //~ Instance fields --------------------------------------------------------

    private String fullPath = "";
    private int from = -1;
    private int to = -1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ActiveSelectionFilter object.
     */
    public ActiveSelectionFilter() {
        super();
        active = false;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getFrom() {
        return from;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getTo() {
        return to;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fullPath  DOCUMENT ME!
     * @param  from      DOCUMENT ME!
     * @param  to        DOCUMENT ME!
     */
    public void setSelection(final String fullPath, final int from, final int to) {
        this.from = from;
        this.to = to;
        this.fullPath = fullPath;
        filterNotifier.fireFilterChange();
    }

    @Override
    public String getName() {
        return "ActiveSelectionFilter";
    }

    @Override
    public boolean isVisible(final Event e) {
        try {
            final int linenumber = new Integer(e.getLocation().split("java:")[1].split("\\)")[0]);
            if ((from == to) && (from == -1)) {
                return e.getSource().equals(fullPath);
            } else {
                return e.getSource().equals(fullPath)
                            && (linenumber >= from)
                            && (linenumber <= to);
            }
        } catch (final Exception ex) {
            LOG.warn("could not check for visibility using event: " + e, ex);
            return false;
        }
    }
}
