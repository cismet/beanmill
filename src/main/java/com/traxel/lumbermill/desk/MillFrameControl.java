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

import com.traxel.lumbermill.Mill;
import com.traxel.lumbermill.event.EventsPanel;
import com.traxel.lumbermill.event.EventsPanelControl;
import com.traxel.lumbermill.event.TableControl;
import com.traxel.lumbermill.event.TableStatus;
import com.traxel.lumbermill.filter.FilterSetControl;
import com.traxel.lumbermill.filter.FilterSetView;
import com.traxel.lumbermill.log.Log;
import com.traxel.lumbermill.log.LogActiveStateView;
import com.traxel.lumbermill.log.LogControl;
import com.traxel.lumbermill.log.LogStatus;

import org.apache.log4j.Logger;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MillFrameControl extends InternalFrameAdapter {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(
            MillFrameControl.class);

    //~ Instance fields --------------------------------------------------------

    private final EventsPanelControl eventspanelControl;
    private final MillFrame frame;
    private final Mill mill;

    //~ Constructors -----------------------------------------------------------

    /**
     * ---------------------------------------------- Instance Initialization
     * ----------------------------------------------
     *
     * @param  mill  DOCUMENT ME!
     * @param  name  DOCUMENT ME!
     */
    public MillFrameControl(final Mill mill, final String name) {
        eventspanelControl = new EventsPanelControl();
        this.mill = mill;
        frame = new MillFrame(
                name,
                getLogActiveStateView(),
                getLogStatus(),
                getTableStatus(),
                getEventsPanel(),
                getFilterSetView());
        frame.addInternalFrameListener(this);
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(getFilterSetMenu());
        menuBar.add(getColumnMenu());
        frame.setJMenuBar(menuBar);
        final BasicInternalFrameUI ui = (BasicInternalFrameUI)frame.getUI();
        final Component titlePanel = ui.getNorthPane();

        if (titlePanel == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                    "cannot add mouselistener to title panel, probably the " // NOI18N
                            + "millframe isself is not used for visualisation"); // NOI18N
            }
        } else {
            titlePanel.addMouseListener(new TitleListener());
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------------- Instance Convenience Accessors
     * ------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public Log getLog() {
        return getLogControl().getLog();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private LogControl getLogControl() {
        return getTableControl().getLogControl();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private LogActiveStateView getLogActiveStateView() {
        return getLogControl().getActiveStateView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private LogStatus getLogStatus() {
        return getLogControl().getLogStatus();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private TableStatus getTableStatus() {
        return getTableControl().getTableStatus();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private EventsPanel getEventsPanel() {
        return eventspanelControl.getView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private FilterSetView getFilterSetView() {
        return getFilterSetControl().getFilterSetView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private JMenu getFilterSetMenu() {
        return getFilterSetControl().getFilterSetMenu();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private FilterSetControl getFilterSetControl() {
        return getTableControl().getFilterSetControl();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private TableControl getTableControl() {
        return eventspanelControl.getTableControl();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private JMenu getColumnMenu() {
        return getTableControl().getColumnMenu();
    }
    /**
     * ---------------------------------------------- Instance Accessors ----------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public MillFrame getFrame() {
        return frame;
    }

    // ----------------------------------------
    // InternalFrameAdapter Implementation
    // ----------------------------------------
    @Override
    public void internalFrameClosing(final InternalFrameEvent e) {
        if (frame.equals(e.getSource())) {
            mill.closeEventListeners();
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * ---------------------------------------------- Instance Definition
     * ----------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    private class TitleListener extends MouseAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void mouseClicked(final MouseEvent e) {
            if (MouseEvent.BUTTON3 == e.getButton()) {
                final String newTitle = getTitle();
                if ((newTitle != null) && !"".equals(newTitle)) {
                    frame.setTitle(newTitle);
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private String getTitle() {
            return JOptionPane.showInputDialog(frame,
                    "Enter the new title for this frame:",
                    frame.getTitle());
        }
    }
}
