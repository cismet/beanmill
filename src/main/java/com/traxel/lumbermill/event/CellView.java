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
// this doesn't belong in the model hierarchy, but it has
// to be here because javax.swing.table.TableColumnModel
// violates M/V separation.
package com.traxel.lumbermill.event;

import com.traxel.color.model.Colors;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class CellView {

    //~ Static fields/initializers ---------------------------------------------

    // -------------------------------------------
    // Class Initialization
    // -------------------------------------------

    private static final TableCellRenderer SEVERITY = new SeverityCellView();

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------------- Class API -------------------------------------------
     *
     * @param   accessor  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    static TableCellRenderer getView(final Accessor accessor) {
        if (Boolean.class.equals(accessor.getType())) {
            return null;
        } else if (Accessor.SEVERITY.equals(accessor)) {
            return SEVERITY;
        } else {
            final DefaultTableCellRenderer renderer;

            renderer = new DefaultTableCellRenderer();
            if (Accessor.TIMESTAMP.equals(accessor)
                        || Accessor.ELAPSED_TIME.equals(accessor)) {
                renderer.setHorizontalAlignment(renderer.RIGHT);
            }

            return renderer;
        }
    }
}
