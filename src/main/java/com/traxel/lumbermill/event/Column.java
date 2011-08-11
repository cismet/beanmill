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
import java.util.List;

import javax.swing.table.TableColumn;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class Column extends TableColumn {

    //~ Instance fields --------------------------------------------------------

    // -----------------------------------
    // Instance Definition
    // -----------------------------------

    private final Accessor ACCESSOR;
    private final boolean USE_FORMATTER;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------------------------- Instance Initialization -----------------------------------
     *
     * @param  accessor      DOCUMENT ME!
     * @param  modelIndex    DOCUMENT ME!
     * @param  useFormatter  DOCUMENT ME!
     * @param  minWidth      DOCUMENT ME!
     * @param  defaultWidth  DOCUMENT ME!
     * @param  maxWidth      DOCUMENT ME!
     */
    Column(final Accessor accessor,
            final int modelIndex,
            final boolean useFormatter,
            final int minWidth,
            final int defaultWidth,
            final int maxWidth) {
        super(modelIndex,
            defaultWidth,
            CellView.getView(accessor),
            null);
        ACCESSOR = accessor;
        USE_FORMATTER = useFormatter;
        setMinWidth(minWidth);
        setMaxWidth(maxWidth);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ----------------------------------- Instance Accessors -----------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public final Accessor getAccessor() {
        return ACCESSOR;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   event  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final Object getValue(final Event event) {
        if (USE_FORMATTER) {
            return getAccessor().getString(event);
        } else {
            return getAccessor().getValue(event);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final Class getType() {
        return getAccessor().getType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final String getName() {
        return getAccessor().getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final String getShortName() {
        return getAccessor().getShortName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final String getDescription() {
        return getAccessor().getDescription();
    }

    @Override
    public final Object getHeaderValue() {
        return getShortName();
    }
}
