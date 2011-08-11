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

import java.awt.event.ActionEvent;

import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class ColumnSetControl {

    //~ Instance fields --------------------------------------------------------

    private final ColumnSet COLUMN_SET = new ColumnSet();
    private final JMenu MENU = new JMenu("Columns");

    //~ Instance initializers --------------------------------------------------

    // ----------------------------------------------
    // Instance Initialization
    // ----------------------------------------------

    {
        final Iterator it;
        Column column;
        ColumnAction action;
        JMenuItem item;

        it = COLUMN_SET.ALL_COLUMNS.iterator();
        while (it.hasNext()) {
            column = (Column)it.next();
            action = new ColumnAction(column);
            item = new JCheckBoxMenuItem(action);
            item.setArmed(false);
            if (COLUMN_SET.contains(column)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
            MENU.add(item);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ---------------------------------------------- Instance Accessors ----------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public ColumnSet getColumnSet() {
        return COLUMN_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JMenu getMenu() {
        return MENU;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * ---------------------------------------------- Instance Definition
     * ----------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    private class ColumnAction extends AbstractAction {

        //~ Instance fields ----------------------------------------------------

        // Instance Definition
        private final Column COLUMN;

        //~ Constructors -------------------------------------------------------

        /**
         * Instance Initialization.
         *
         * @param  column  DOCUMENT ME!
         */
        public ColumnAction(final Column column) {
            super(column.getName());
            COLUMN = column;
        }

        //~ Methods ------------------------------------------------------------

        // Action Implementation
        @Override
        public void actionPerformed(final ActionEvent e) {
            final JCheckBoxMenuItem item;

            item = (JCheckBoxMenuItem)e.getSource();
            if (item.getState()) {
                COLUMN_SET.addColumn(COLUMN);
            } else {
                COLUMN_SET.removeColumn(COLUMN);
            }
        }
    }
}
