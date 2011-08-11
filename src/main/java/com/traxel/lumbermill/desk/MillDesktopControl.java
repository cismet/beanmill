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

import javax.swing.Action;
import javax.swing.JMenu;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MillDesktopControl {

    //~ Instance fields --------------------------------------------------------

    // ----------------------------------------
    // Instance Definition
    // ----------------------------------------

    private final MillDesktop DESKTOP = new MillDesktop();
    private final WindowMenu WINDOW_MENU = new WindowMenu(DESKTOP);
    private final MillMenu MILL_MENU = new MillMenu(this);

    //~ Methods ----------------------------------------------------------------

    /**
     * ---------------------------------------- Public Instance API ----------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public MillDesktop getDesktop() {
        return DESKTOP;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JMenu getWindowMenu() {
        return WINDOW_MENU;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  frame  DOCUMENT ME!
     */
    public void add(final MillFrame frame) {
        WINDOW_MENU.addMillFrame(DESKTOP, frame);
        DESKTOP.add(frame);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MillMenu getMillMenu() {
        return MILL_MENU;
    }
}
