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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class EventsPanelControl implements PropertyChangeListener {

    //~ Instance fields --------------------------------------------------------

    private final TableControl tableControl;
    private final EventView eventView;
    private final EventsPanel eventPanel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EventsPanelControl object.
     */
    public EventsPanelControl() {
        tableControl = new TableControl();
        eventView = new EventView();
        eventPanel = new EventsPanel(tableControl.getView(), eventView);
        tableControl.addPropertyListener(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public TableControl getTableControl() {
        return tableControl;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent e) {
        final String property = e.getPropertyName();
        if (TableControl.FIRST_SELECTED_EVENT.equals(property)) {
            eventView.setEvent((Event)e.getNewValue());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public EventsPanel getView() {
        return eventPanel;
    }
}
