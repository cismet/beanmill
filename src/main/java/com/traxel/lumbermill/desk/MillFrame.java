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
package com.traxel.lumbermill.desk;

import com.traxel.lumbermill.MillView;
import com.traxel.lumbermill.event.EventListenerStatus;
import com.traxel.lumbermill.event.EventsPanel;
import com.traxel.lumbermill.event.TableStatus;
import com.traxel.lumbermill.filter.FilterSetView;
import com.traxel.lumbermill.log.LogActiveStateView;
import com.traxel.lumbermill.log.LogStatus;

import javax.swing.JInternalFrame;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MillFrame extends JInternalFrame {

    //~ Instance fields --------------------------------------------------------

    // ---------------------------------------------
    // Instance Definition
    // ---------------------------------------------

    private final MillView millView;

    //~ Constructors -----------------------------------------------------------

    /**
     * ---------------------------------------------- Instance Initialization
     * ----------------------------------------------
     *
     * @param  name                DOCUMENT ME!
     * @param  logActiveStateView  DOCUMENT ME!
     * @param  logStatus           DOCUMENT ME!
     * @param  tableStatus         DOCUMENT ME!
     * @param  eventsPanel         DOCUMENT ME!
     * @param  filterSetView       DOCUMENT ME!
     */
    public MillFrame(final String name,
            final LogActiveStateView logActiveStateView,
            final LogStatus logStatus,
            final TableStatus tableStatus,
            final EventsPanel eventsPanel,
            final FilterSetView filterSetView) {
        super(name, true, true, true, true);
        millView = new MillView(logActiveStateView,
                logStatus,
                tableStatus,
                eventsPanel,
                filterSetView);
        setContentPane(millView);
        pack();
        setLocation(0, 0);
        setVisible(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * --------------------------------------------- Package API ---------------------------------------------
     *
     * @param  status  DOCUMENT ME!
     */
    public void setEventListenerStatus(final EventListenerStatus status) {
        millView.setEventListenerStatus(status);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MillView getMillView() {
        return millView;
    }
}
