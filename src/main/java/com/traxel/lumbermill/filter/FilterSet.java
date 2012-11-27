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
package com.traxel.lumbermill.filter;

import com.traxel.lumbermill.event.Event;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class FilterSet implements Filter, FilterListener {

    //~ Instance fields --------------------------------------------------------

    // ---------------------------------------------
    // Instance Definition
    // ---------------------------------------------
    private final Set filters = new LinkedHashSet();
    private final Set listeners = new HashSet();
    private boolean active = true;

    //~ Methods ----------------------------------------------------------------

    /**
     * --------------------------------------------- Filter Implementation ---------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public List getFilters() {
        return new ArrayList(filters);
    }

    @Override
    public boolean isVisible(final Event event) {
        synchronized (filters) {
            final Iterator it;
            Filter filter;

            it = filters.iterator();
            while (it.hasNext()) {
                filter = (Filter)it.next();

                if (filter.isActive() && !filter.isVisible(event)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void addFilterListener(final FilterListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeFilterListener(final FilterListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    @Override
    public String getName() {
        return "Filter Set";
    }

    // -------------------------------------------
    // FilterListener Implementation
    // -------------------------------------------
    @Override
    public void filterChange(final FilterEvent event) {
        fireFilterEvent(event);
    }

    /**
     * --------------------------------------------- Public Instance API --------------------------------------------
     *
     * @param  filter  DOCUMENT ME!
     */
    public void add(final Filter filter) {
        synchronized (filters) {
            filter.addFilterListener(this);
            filters.add(filter);
            fireFilterAdded(filter);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  filter  DOCUMENT ME!
     */
    public void remove(final Filter filter) {
        synchronized (filters) {
            filter.removeFilterListener(this);
            filters.remove(filter);
            fireFilterRemoved(filter);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void removeAll() {
        final Iterator<Filter> it;
        synchronized (filters) {
            it = new HashSet<Filter>(filters).iterator();
        }

        while (it.hasNext()) {
            remove(it.next());
        }
    }

    /**
     * -------------------------------------------- Private Instance API --------------------------------------------
     *
     * @param  source  DOCUMENT ME!
     */
    public void fireFilterAdded(final Filter source) {
        final FilterEvent event;

        event = new FilterEvent(source);
        fireFilterEvent(event);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  source  DOCUMENT ME!
     */
    private void fireFilterRemoved(final Filter source) {
        final FilterEvent event;

        event = new FilterEvent(source);
        fireFilterEvent(event);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    private void fireFilterEvent(final FilterEvent event) {
        final Iterator it;
        synchronized (listeners) {
            it = new HashSet(listeners).iterator();
        }

        FilterListener listener;

        while (it.hasNext()) {
            listener = (FilterListener)it.next();
            listener.filterChange(event);
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }
}
