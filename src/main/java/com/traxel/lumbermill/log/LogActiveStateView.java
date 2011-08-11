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
package com.traxel.lumbermill.log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class LogActiveStateView extends JToolBar implements PropertyChangeListener {

    //~ Instance fields --------------------------------------------------------

    final JToggleButton PLAY = new JToggleButton(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/beanmill/res/start.png")));
    final JToggleButton PAUSE = new JToggleButton(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/beanmill/res/pause.png")));
    final JToggleButton STOP = new JToggleButton(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/beanmill/res/stop.png")));
    final JButton CLEAR = new JButton(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/beanmill/res/delete.png")));

    ButtonGroup bg = new ButtonGroup();

    // ---------------------------------------
    // Instance Definition
    // ---------------------------------------

    private final Log LOG;
    private final ActiveStateIcon PLAY_ICON = new ActiveStateIcon.PlayIcon();
    private final ActiveStateIcon PAUSE_ICON = new ActiveStateIcon.PauseIcon();
    private final ActiveStateIcon STOP_ICON = new ActiveStateIcon.StopIcon();
    private final ActiveStateIcon CLEAR_ICON = new ActiveStateIcon.ClearIcon();

    //~ Constructors -----------------------------------------------------------

    /**
     * --------------------------------------- Instance Initialization ---------------------------------------
     *
     * @param  log  DOCUMENT ME!
     */
    public LogActiveStateView(final Log log) {
        PLAY.setToolTipText("Start Logging");
        PAUSE.setToolTipText("Pause Logging");
        STOP.setToolTipText("Stop Logging");
        CLEAR.setToolTipText("Clear Output");
        this.setFloatable(false);
        // this.setOrientation(this.VERTICAL);
        LOG = log;
        LOG.addPropertyListener(this);
        if (Log.PLAY == LOG.getActiveState()) {
            PLAY_ICON.setActive(true);
        } else if (Log.PAUSE == LOG.getActiveState()) {
            PAUSE_ICON.setActive(true);
        } else if (Log.STOP == LOG.getActiveState()) {
            STOP_ICON.setActive(true);
        }
        add(PLAY);
        add(PAUSE);
        add(STOP);
        add(CLEAR);
        bg.add(PLAY);
        bg.add(PAUSE);
        bg.add(STOP);

        PLAY.setSelected(true);
    }

    //~ Methods ----------------------------------------------------------------

    // ----------------------------------------
    // PropertyChangeListener Implementation
    // ----------------------------------------

    @Override
    public void propertyChange(final PropertyChangeEvent e) {
        if (LOG.equals(e.getSource())
                    && "_activeState".equals(e.getPropertyName())) {
            final int newValue;

            PLAY_ICON.setActive(false);
            PAUSE_ICON.setActive(false);
            STOP_ICON.setActive(false);
            CLEAR_ICON.setActive(false);
            newValue = ((Integer)e.getNewValue()).intValue();
            if (Log.PLAY == newValue) {
                PLAY_ICON.setActive(true);
            } else if (Log.PAUSE == newValue) {
                PAUSE_ICON.setActive(true);
            } else if (Log.STOP == newValue) {
                STOP_ICON.setActive(true);
            }
            repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JButton getClearButton() {
        return CLEAR;
    }
}
