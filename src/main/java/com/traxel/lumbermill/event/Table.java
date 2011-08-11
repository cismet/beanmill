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

import com.traxel.lumbermill.filter.FilterEvent;
import com.traxel.lumbermill.filter.FilterListener;
import com.traxel.lumbermill.filter.FilterSet;
import com.traxel.lumbermill.log.Log;
import com.traxel.lumbermill.log.LogListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class Table extends AbstractTableModel implements LogListener, FilterListener {

    //~ Instance fields --------------------------------------------------------

    // ---------------------------------------
    // Instance Definition
    // ---------------------------------------

    private List EVENTS = Collections.synchronizedList(new ArrayList());
    private final ColumnSet COLUMN_SET;
    private final Log LOG;
    private final FilterSet FILTER_SET;

    // ---------------------------------------
    // Private Instance API
    // ---------------------------------------
    private Integer insertLocker = 1;

    //~ Constructors -----------------------------------------------------------

    /**
     * --------------------------------------- Instance Initialization ---------------------------------------
     *
     * @param  log        DOCUMENT ME!
     * @param  filterSet  DOCUMENT ME!
     * @param  columnSet  DOCUMENT ME!
     */
    public Table(final Log log, final FilterSet filterSet, final ColumnSet columnSet) {
        COLUMN_SET = columnSet;
        FILTER_SET = filterSet;
        FILTER_SET.addFilterListener(this);
        LOG = log;
        LOG.addLogListener(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Log getLog() {
        return LOG;
    }

    /**
     * DOCUMENT ME!
     */
    public void fireChange() {
        fireTableDataChanged();
    }

    // ---------------------------------------
    // TableModel Implementation
    // ---------------------------------------

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final Column column;
        final Event event;

        column = getColumn(columnIndex);
        event = getEvent(rowIndex);

        return column.getValue(event);
    }

    @Override
    public int getColumnCount() {
        return COLUMN_SET.getColumnCount();
    }

    @Override
    public int getRowCount() {
        return EVENTS.size();
    }

    // ---------------------------------------
    // LogListener Implementation
    // ---------------------------------------

    @Override
    public void added(final Log source, final Event event) {
        if (LOG.equals(source)) {
            insert(event);
        }
    }

    @Override
    public void removed(final Log source, final Event[] events) {
        if (LOG.equals(source)) {
            remove(events);
        }
    }

    @Override
    public void cleared(final Log source) {
        if (LOG.equals(source)) {
            removeAll();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List getAllEvents() {
        return EVENTS;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  events  DOCUMENT ME!
     */
    public void resetAllEvents(final List events) {
        EVENTS = events;
        fireChange();
    }
    // ---------------------------------------
    // FilterListener Implemenation
    // ---------------------------------------

    @Override
    public void filterChange(final FilterEvent e) {
        synchronized (EVENTS) {
            removeAll();
            insertAll(LOG.toArray());
        }
    }

    // ---------------------------------------
    // Public Instance API
    // ---------------------------------------

    @Override
    public String getColumnName(final int columnIndex) {
        return getColumn(columnIndex).getName();
    }

    @Override
    public Class getColumnClass(final int columnIndex) {
        return getColumn(columnIndex).getType();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   rowIndex  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Event getEvent(final int rowIndex) {
        try {
            return (Event)EVENTS.get(rowIndex);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * events should be given in chronological order, from first occured to last occured.
     *
     * @param  events  DOCUMENT ME!
     */
    public void insertAll(final Event[] events) {
        int count = 0;
        synchronized (EVENTS) {
            for (int i = 0; i < events.length; i++) {
                if ((events[i] != null)
                            && FILTER_SET.isVisible(events[i])) {
                    count++;
                    EVENTS.add(0, events[i]);
                }
            }
            if (count > 0) {
                fireTableRowsInserted(0, count - 1);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void removeAll() {
        synchronized (EVENTS) {
            final int minRow = 0;
            final int maxRow = EVENTS.size() - 1;

            EVENTS.clear();
            if (maxRow >= minRow) {
                fireTableRowsDeleted(minRow, maxRow);
            }
        }
    }

    /**
     * -------------------------------------- Package Instance API --------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    ColumnSet getColumnSet() {
        return COLUMN_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   columnIndex  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Column getColumn(final int columnIndex) {
        return getColumnSet().columnForIndex(columnIndex);
    }
    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    private void insert(final Event event) {
        if ((event != null) && FILTER_SET.isVisible(event)) {
            synchronized (insertLocker) {
                EVENTS.add(0, event);
                fireTableRowsInserted(0, 0);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  events  DOCUMENT ME!
     */
    private void remove(final Event[] events) {
        int minRow = -1;
        int maxRow = -1;
        int row;

        synchronized (EVENTS) {
            for (int i = 0; i < events.length; i++) {
                row = EVENTS.indexOf(events[i]);
                if ((minRow == -1) || (row < minRow)) {
                    minRow = row;
                }
                if ((maxRow == -1) || (row > maxRow)) {
                    maxRow = row;
                }
            }
            for (int i = 0; i < events.length; i++) {
                EVENTS.remove(events[i]);
            }
            // Deadlock with this in synch loop?
            if ((minRow > -1) && (maxRow > -1)) {
                fireTableRowsDeleted(minRow, maxRow);
            }
        }
    }
}
