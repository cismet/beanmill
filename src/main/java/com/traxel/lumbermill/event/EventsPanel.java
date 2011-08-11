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

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class EventsPanel extends JSplitPane {

    //~ Instance fields --------------------------------------------------------

    private final TableView tableView;
    private final EventView eventView;
    private final JScrollPane tableScroll;
    private final JScrollPane eventScroll;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EventsPanel object.
     *
     * @param  tableView  DOCUMENT ME!
     * @param  eventView  DOCUMENT ME!
     */
    public EventsPanel(final TableView tableView, final EventView eventView) {
        super(VERTICAL_SPLIT);
        this.tableView = tableView;
        this.eventView = eventView;
        tableScroll = new JScrollPane(tableView);
        tableScroll.setBorder(new EmptyBorder(1, 1, 1, 1));
        eventScroll = new JScrollPane();
        eventScroll.setBorder(new EmptyBorder(1, 1, 1, 1));
        final EventViewVieport vp = new EventViewVieport(eventView);
        eventScroll.setViewport(vp);
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tableScroll.setPreferredSize(new Dimension(600, 100));
        eventScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        eventScroll.setPreferredSize(new Dimension(600, 300));
        setLeftComponent(tableScroll);
        setRightComponent(eventScroll);
        setResizeWeight(0.25d);
        setOneTouchExpandable(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public TableView getTableView() {
        return tableView;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public EventView getEventView() {
        return eventView;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JTable getEventTable() {
        return tableView;
    }
}
