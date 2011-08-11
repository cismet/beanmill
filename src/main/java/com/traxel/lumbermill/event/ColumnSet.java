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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableColumnModel;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class ColumnSet extends DefaultTableColumnModel {

    //~ Instance fields --------------------------------------------------------

    // ------------------------------------
    // Instance Definition
    // ------------------------------------

    final Column SEVERITY = new Column(Accessor.SEVERITY, 0, false, 70, 70, 70);
    final Column TIMESTAMP = new Column(Accessor.TIMESTAMP, 1, true, 100, 100, 100);
    final Column ELAPSED_TIME = new Column(Accessor.ELAPSED_TIME, 2, true, 70, 70, 70);
    final Column HAS_THROWN = new Column(Accessor.HAS_THROWN, 3, false, 20, 20, 20);
    final Column SHORT_SOURCE = new Column(Accessor.SHORT_SOURCE, 4, false, 20, 50, 9999);
    final Column SOURCE = new Column(Accessor.SOURCE, 5, false, 20, 100, 9999);
    final Column MESSAGE = new Column(Accessor.MESSAGE, 6, false, 0, 400, 9999);
    final Column NDC = new Column(Accessor.NDC, 7, false, 0, 200, 9999);

    final List ALL_COLUMNS;
    private final List DEFAULT_COLUMNS;

    //~ Instance initializers --------------------------------------------------

    // -----------------------------------------
    // Instance Initialization
    // -----------------------------------------

    {
        final ArrayList allColumns = new ArrayList();

        allColumns.add(SEVERITY);
        allColumns.add(TIMESTAMP);
        allColumns.add(ELAPSED_TIME);
        allColumns.add(HAS_THROWN);
        allColumns.add(SHORT_SOURCE);
        allColumns.add(SOURCE);
        allColumns.add(MESSAGE);
        allColumns.add(NDC);

        ALL_COLUMNS = Collections.unmodifiableList(allColumns);

        final List defaultColumns = new ArrayList();

        defaultColumns.add(SEVERITY);
        defaultColumns.add(ELAPSED_TIME);
        defaultColumns.add(HAS_THROWN);
        defaultColumns.add(SOURCE);
        defaultColumns.add(MESSAGE);

        DEFAULT_COLUMNS = Collections.unmodifiableList(defaultColumns);

        final Iterator it;

        it = DEFAULT_COLUMNS.iterator();
        while (it.hasNext()) {
            addColumn((Column)it.next());
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ---------------------------------------- Package Instance API ----------------------------------------
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Column columnForIndex(final int index) {
        // JTables are weird.
        return (Column)ALL_COLUMNS.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean contains(final Column column) {
        if (column == null) {
            return false;
        }
        final Enumeration enumeration;

        enumeration = getColumns();
        while (enumeration.hasMoreElements()) {
            if (column.equals(enumeration.nextElement())) {
                return true;
            }
        }
        return false;
    }
}
