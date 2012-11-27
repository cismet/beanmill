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
package com.traxel.lumbermill;

import com.traxel.lumbermill.desk.MillDesktop;
import com.traxel.lumbermill.desk.MillDesktopControl;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

/**
 * The main GUI Framne (not used in Beanmill).
 *
 * @version  $Revision$, $Date$
 */
public class Lumbermill extends JFrame {

    //~ Instance fields --------------------------------------------------------

    // ----------------------------------
    // Instance Definition
    // ----------------------------------

    final MillDesktopControl DESKTOP_CONTROL = new MillDesktopControl();

    //~ Instance initializers --------------------------------------------------

    // ----------------------------------
    // Instance Initialization
    // ----------------------------------

    {
        final JMenuBar menuBar;
        final MillDesktop desktop;

        menuBar = new JMenuBar();
        menuBar.add(DESKTOP_CONTROL.getMillMenu());
        menuBar.add(DESKTOP_CONTROL.getWindowMenu());
        desktop = DESKTOP_CONTROL.getDesktop();
        setContentPane(desktop);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ---------------------------------- Class API ----------------------------------
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        new Lumbermill();
    }
}
