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

import com.traxel.lumbermill.log.Log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class EventListener extends Thread {

    //~ Static fields/initializers ---------------------------------------------

    public static final String STATUS_PROPERTY = "_status";
    public static final String NA = "N/A";

    //~ Instance fields --------------------------------------------------------

    private final Log sink;
    private final Set<PropertyChangeListener> listeners;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EventListener object.
     *
     * @param  sink  DOCUMENT ME!
     */
    protected EventListener(final Log sink) {
        this.sink = sink;
        listeners = Collections.synchronizedSet(new HashSet<PropertyChangeListener>());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public abstract void close();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getHost();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getPortString();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getType();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getStatus();

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    public void addPropertyListener(final PropertyChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    protected void add(final Event event) {
        sink.add(event);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  property  DOCUMENT ME!
     * @param  oldValue  DOCUMENT ME!
     * @param  newValue  DOCUMENT ME!
     */
    protected void firePropertyChange(final String property, final Object oldValue, final Object newValue) {
        final PropertyChangeEvent event = new PropertyChangeEvent(this,
                property, oldValue, newValue);
        synchronized (listeners) {
            final Iterator<PropertyChangeListener> it = listeners.iterator();
            while (it.hasNext()) {
                it.next().propertyChange(event);
            }
        }
    }
}
