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

import com.traxel.lumbermill.filter.FilterSetControl;
import com.traxel.lumbermill.log.LogControl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class TableControl implements ListSelectionListener {

    //~ Static fields/initializers ---------------------------------------------

    // ------------------------------------
    // Class Definition
    // ------------------------------------

    public static final String FIRST_SELECTED_EVENT = "First Selected Event";
    public static final String LAST_SELECTED_EVENT = "Last Selected Event";

    //~ Instance fields --------------------------------------------------------

    // -----------------------------------
    // Instance Definition
    // -----------------------------------

    private final LogControl LOG_CONTROL = new LogControl();
    private final FilterSetControl FILTER_SET_CONTROL = new FilterSetControl();
    private final ColumnSetControl COLUMN_SET_CONTROL = new ColumnSetControl();
    private final TableView TABLE_VIEW = new TableView(LOG_CONTROL.getLog(),
            FILTER_SET_CONTROL.getFilterSet(),
            COLUMN_SET_CONTROL.getColumnSet());
    private final TableStatus TABLE_STATUS = new TableStatus();
    private final Set LISTENERS = Collections.synchronizedSet(new HashSet());
    private Event _previousFirst;
    private Event _previousLast;

    //~ Instance initializers --------------------------------------------------

    // -------------------------------------
    // Instance Initialization
    // ------------------------------------

    {
        final ListSelectionModel model;

        model = getView().getSelectionModel();
        model.addListSelectionListener(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ----------------------------------- Instance Accessors -----------------------------------
     *
     * @return  DOCUMENT ME!
     */
    TableView getView() {
        return TABLE_VIEW;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public LogControl getLogControl() {
        return LOG_CONTROL;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FilterSetControl getFilterSetControl() {
        return FILTER_SET_CONTROL;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public TableStatus getTableStatus() {
        return TABLE_STATUS;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JMenu getColumnMenu() {
        return COLUMN_SET_CONTROL.getMenu();
    }

    // ---------------------------------------
    // ListSelectionListener Implementation
    // ---------------------------------------

    @Override
    public void valueChanged(final ListSelectionEvent e) {
        // if( ! e.getValueIsAdjusting() ) {
        setElapsed();
        // }
    }

    /**
     * ------------------------------------- Public Instance API -------------------------------------
     *
     * @param  l  DOCUMENT ME!
     */
    public void addPropertyListener(final PropertyChangeListener l) {
        LISTENERS.add(l);
    }

    /**
     * ------------------------------------- Package Instance API -------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    Event getFirstSelectedEvent() {
        return TABLE_VIEW.getFirstSelectedEvent();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Event getLastSelectedEvent() {
        return TABLE_VIEW.getLastSelectedEvent();
    }

    /**
     * ------------------------------------- Private Instance API -------------------------------------
     */
    private synchronized void setElapsed() {
        boolean change = false;
        final Event newFirst = getFirstSelectedEvent();
        final Event newLast = getLastSelectedEvent();
        if ((newFirst != _previousFirst) && (newFirst != null)) {
            change = true;
            firePropertyChange(FIRST_SELECTED_EVENT,
                _previousFirst,
                newFirst);
            _previousFirst = newFirst;
        }
        if ((newLast != _previousLast) && (newLast != null)) {
            change = true;
            firePropertyChange(LAST_SELECTED_EVENT,
                _previousLast,
                newLast);
            _previousLast = newLast;
        }
        if (change) {
            TABLE_STATUS.setElapsed(newFirst, newLast);
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
        synchronized (LISTENERS) {
            final Iterator it;
            PropertyChangeListener listener;
            final PropertyChangeEvent event;

            event = new PropertyChangeEvent(this, property, oldValue, newValue);
            it = LISTENERS.iterator();
            while (it.hasNext()) {
                listener = (PropertyChangeListener)it.next();
                listener.propertyChange(event);
            }
        }
    }
}
