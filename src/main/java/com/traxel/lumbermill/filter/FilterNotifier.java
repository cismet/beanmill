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

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Sends notifications immediately - see TimerNotifier one that has a 250 ms delay, aggregates multiple notifications.
 * This is public so people outside the package can use it.
 *
 * @version  $Revision$, $Date$
 */
public class FilterNotifier {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(
            FilterNotifier.class);

    //~ Instance fields --------------------------------------------------------

    private final Set<FilterListener> filterListeners;
    private final Filter owner;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FilterNotifier object.
     *
     * @param  owner  DOCUMENT ME!
     */
    public FilterNotifier(final Filter owner) {
        this.owner = owner;
        filterListeners = Collections.synchronizedSet(new HashSet<FilterListener>());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public final void addFilterListener(final FilterListener listener) {
        synchronized (filterListeners) {
            filterListeners.add(listener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public final void removeFilterListener(final FilterListener listener) {
        synchronized (filterListeners) {
            filterListeners.remove(listener);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void fireFilterChange() {
        final FilterEvent event = new FilterEvent(owner);
        final Vector<FilterListener> fls = new Vector<FilterListener>(filterListeners);

        final Iterator<FilterListener> it = fls.iterator();
        while (it.hasNext()) {
            final FilterListener fl = it.next();
            if (LOG.isDebugEnabled()) {
                LOG.debug("firing to: " + fl);
            }
            fl.filterChange(event);
        }
    }
}
