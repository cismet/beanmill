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

import com.traxel.lumbermill.desk.FrameControl.Cascade;
import com.traxel.lumbermill.desk.FrameControl.MaximizeFrame;
import com.traxel.lumbermill.desk.FrameControl.MinimizeAll;
import com.traxel.lumbermill.desk.FrameControl.RestoreAll;
import com.traxel.lumbermill.desk.FrameControl.TileHorizontal;
import com.traxel.lumbermill.desk.FrameControl.TileVertical;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class WindowMenu extends JMenu {

    //~ Constructors -----------------------------------------------------------

    /**
     * --------------------------------------- Instance Initialization ---------------------------------------
     *
     * @param  desktop  DOCUMENT ME!
     */
    public WindowMenu(final MillDesktop desktop) {
        super("Windows");
        final Action action;

        add(new Cascade(desktop));
        add(new TileHorizontal(desktop));
        add(new TileVertical(desktop));
        add(new MinimizeAll(desktop));
        add(new RestoreAll(desktop));
        addSeparator();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * --------------------------------------- Public API ---------------------------------------
     *
     * @param  desktop  DOCUMENT ME!
     * @param  frame    DOCUMENT ME!
     */
    public void addMillFrame(final MillDesktop desktop, final MillFrame frame) {
        final MaximizeFrame maximize;
        final JMenuItem menuItem;
        final FrameControlRemover remover;

        maximize = new MaximizeFrame(desktop, frame);
        menuItem = add(maximize);
        new TitleChangeListener(frame, menuItem);
        new FrameControlRemover(frame, menuItem);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * -------------------------------------- Class Definition --------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    private static class TitleChangeListener implements PropertyChangeListener {

        //~ Static fields/initializers -----------------------------------------

        // Class Definition
        private static final String PROPERTY = JInternalFrame.TITLE_PROPERTY;

        //~ Instance fields ----------------------------------------------------

        // Instance Definition
        private final JInternalFrame FRAME;
        private final JMenuItem MENU_ITEM;

        //~ Constructors -------------------------------------------------------

        /**
         * Instance Initialization.
         *
         * @param  frame     DOCUMENT ME!
         * @param  menuItem  DOCUMENT ME!
         */
        public TitleChangeListener(final JInternalFrame frame, final JMenuItem menuItem) {
            FRAME = frame;
            MENU_ITEM = menuItem;
            FRAME.addPropertyChangeListener(this);
        }

        //~ Methods ------------------------------------------------------------

        // PropertyChange Implementation
        @Override
        public void propertyChange(final PropertyChangeEvent e) {
            if (FRAME.equals(e.getSource())
                        && PROPERTY.equals(e.getPropertyName())) {
                MENU_ITEM.setText(e.getNewValue().toString());
            }
        }
    }

    /**
     * --------------------------------------- Instance Definition ---------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    private class FrameControlRemover extends InternalFrameAdapter {

        //~ Instance fields ----------------------------------------------------

        private final JMenuItem MENU_ITEM;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new FrameControlRemover object.
         *
         * @param  frame     DOCUMENT ME!
         * @param  menuItem  DOCUMENT ME!
         */
        public FrameControlRemover(final JInternalFrame frame, final JMenuItem menuItem) {
            MENU_ITEM = menuItem;
            frame.addInternalFrameListener(this);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void internalFrameClosed(final InternalFrameEvent e) {
            remove(MENU_ITEM);
        }
    }
}
