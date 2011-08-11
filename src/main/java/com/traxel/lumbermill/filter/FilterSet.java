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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

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

    private final Set FILTERS = Collections.synchronizedSet(new LinkedHashSet());
    private final Set LISTENERS = Collections.synchronizedSet(new HashSet());

    private boolean active = true;

    //~ Methods ----------------------------------------------------------------

    /**
     * --------------------------------------------- Filter Implementation ---------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public List getFilters() {
        return new Vector(FILTERS);
    }

    @Override
    public boolean isVisible(final Event event) {
        synchronized (FILTERS) {
            final Iterator it;
            Filter filter;

            it = FILTERS.iterator();
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
        synchronized (LISTENERS) {
            LISTENERS.add(listener);
        }
    }

    @Override
    public void removeFilterListener(final FilterListener listener) {
        synchronized (LISTENERS) {
            LISTENERS.remove(listener);
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
        synchronized (FILTERS) {
            filter.addFilterListener(this);
            FILTERS.add(filter);
            fireFilterAdded(filter);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  filter  DOCUMENT ME!
     */
    public void remove(final Filter filter) {
        synchronized (FILTERS) {
            filter.removeFilterListener(this);
            FILTERS.remove(filter);
            fireFilterRemoved(filter);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void removeAll() {
        final Set clone = new HashSet(FILTERS);
        for (final Object f : clone) {
            remove((Filter)f);
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
        synchronized (LISTENERS) {
            final Iterator it;
            FilterListener listener;

            it = LISTENERS.iterator();
            while (it.hasNext()) {
                listener = (FilterListener)it.next();
                listener.filterChange(event);
            }
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
