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
import com.traxel.lumbermill.filter.Filter;
import com.traxel.lumbermill.filter.FilterListener;
import com.traxel.lumbermill.filter.FilterNotifier;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public abstract class DefaultFilter implements Filter {

    //~ Instance fields --------------------------------------------------------

    protected FilterNotifier filterNotifier;
    protected boolean active;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultFilter object.
     */
    public DefaultFilter() {
        filterNotifier = new FilterNotifier(this);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addFilterListener(final FilterListener l) {
        filterNotifier.addFilterListener(l);
    }

    @Override
    public void removeFilterListener(final FilterListener l) {
        filterNotifier.removeFilterListener(l);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
        filterNotifier.fireFilterChange();
    }

    @Override
    public abstract boolean isVisible(Event e);
    @Override
    public abstract String getName();
}
