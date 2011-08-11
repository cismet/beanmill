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
package com.traxel.lumbermill.log;

import com.traxel.lumbermill.event.Event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class Log {

    //~ Static fields/initializers ---------------------------------------------

    // ---------------------------------------
    // Class Definition
    // ---------------------------------------

    public static final int PLAY = 0;
    public static final int PAUSE = 1;
    public static final int STOP = 2;

    public static final String INFO_PROPERTY_NAME = "_info";

    //~ Instance fields --------------------------------------------------------

    // ---------------------------------------
    // Instance Definition
    // ---------------------------------------

    private final List CACHE = Collections.synchronizedList(new ArrayList());
    private final List EVENTS = Collections.synchronizedList(new ArrayList());
    private final Set LISTENERS = Collections.synchronizedSet(new HashSet());
    private final Set PROPERTY_LISTENERS = Collections.synchronizedSet(new HashSet());
    private int _minSize = 4500;
    private int _maxSize = 5000;
    private int _activeState = PLAY;

    /**
     * DOCUMENT ME!
     *
     * @throws  CacheOverflowException  when PAUSE'd and cache exceeds the maximum size of LOG.
     */
    private Integer addLocker = 0;

    //~ Methods ----------------------------------------------------------------

    /**
     * --------------------------------------- Instance Accessors ---------------------------------------
     *
     * @param  minSize  DOCUMENT ME!
     * @param  maxSize  DOCUMENT ME!
     */
    public void setMinMax(final int minSize, final int maxSize) {
        _minSize = minSize;
        _maxSize = maxSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getMinSize() {
        return _minSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getMaxSize() {
        return _maxSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getActiveState() {
        return _activeState;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  state  DOCUMENT ME!
     */
    private void setActiveState(final int state) {
        if (_activeState == state) {
            return;
        }
        firePropertyChange("_activeState",
            new Integer(_activeState),
            new Integer(state));
        _activeState = state;
    }

    /**
     * -------------------------------------- Public Instance API --------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public int size() {
        return EVENTS.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    public void addPropertyListener(final PropertyChangeListener l) {
        synchronized (PROPERTY_LISTENERS) {
            PROPERTY_LISTENERS.add(l);
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void add(final Event e) {
        if (e != null) {
            synchronized (addLocker) {
                if (PLAY == getActiveState()) {
                    EVENTS.add(e);
                    fireAdded(e);
                    if (EVENTS.size() > getMaxSize()) {
                        final int firstKept;
                        final Event[] removed;

                        firstKept = EVENTS.size() - _minSize;
                        removed = new Event[firstKept];
                        for (int i = firstKept - 1; i >= 0; i--) {
                            removed[i] = (Event)EVENTS.get(i);
                            EVENTS.remove(i);
                        }
                        fireRemoved(removed);
                    }
                } else if (PAUSE == getActiveState()) {
                    if (CACHE.size() >= getMaxSize()) {
                        stop();
                        CACHE.clear();
                        firePropertyChange(INFO_PROPERTY_NAME,
                            null,
                            "Pause Buffer Overflow");
                    } else {
                        CACHE.add(e);
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void remove(final Event e) {
        EVENTS.remove(e);
        fireRemoved(new Event[] { e });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Iterator iterator() {
        final List events;

        synchronized (EVENTS) {
            events = new ArrayList(EVENTS);
        }

        return events.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    public void addLogListener(final LogListener l) {
        if (l != null) {
            synchronized (LISTENERS) {
                LISTENERS.add(l);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Event[] toArray() {
        synchronized (EVENTS) {
            return (Event[])EVENTS.toArray(new Event[0]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void play() {
        synchronized (EVENTS) {
            final Iterator it;

            setActiveState(PLAY);
            it = CACHE.iterator();
            while (it.hasNext()) {
                add((Event)it.next());
            }
            CACHE.clear();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void pause() {
        synchronized (EVENTS) {
            setActiveState(PAUSE);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        synchronized (EVENTS) {
            setActiveState(STOP);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        synchronized (EVENTS) {
            EVENTS.clear();
            CACHE.clear();
            fireCleared();
        }
    }

    /**
     * ------------------------------------- Private Instance API -------------------------------------
     *
     * @param  event  DOCUMENT ME!
     */
    private void fireAdded(final Event event) {
        synchronized (LISTENERS) {
            final Iterator it = LISTENERS.iterator();
            while (it.hasNext()) {
                ((LogListener)it.next()).added(this, event);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  events  DOCUMENT ME!
     */
    private void fireRemoved(final Event[] events) {
        synchronized (LISTENERS) {
            final Iterator it = LISTENERS.iterator();
            while (it.hasNext()) {
                ((LogListener)it.next()).removed(this, events);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void fireCleared() {
        synchronized (LISTENERS) {
            final Iterator it = LISTENERS.iterator();
            while (it.hasNext()) {
                ((LogListener)it.next()).cleared(this);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  property  DOCUMENT ME!
     * @param  oldValue  DOCUMENT ME!
     * @param  newValue  DOCUMENT ME!
     */
    private void firePropertyChange(final String property, final Object oldValue, final Object newValue) {
        final PropertyChangeEvent event = new PropertyChangeEvent(this, property,
                oldValue, newValue);

        synchronized (PROPERTY_LISTENERS) {
            final Iterator it;
            PropertyChangeListener listener;

            it = PROPERTY_LISTENERS.iterator();
            while (it.hasNext()) {
                listener = (PropertyChangeListener)it.next();
                listener.propertyChange(event);
            }
        }
    }
}
