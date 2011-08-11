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

import com.traxel.lumbermill.ClientMill;
import com.traxel.lumbermill.Mill;
import com.traxel.lumbermill.ServerMill;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class MillAction extends AbstractAction {

    //~ Instance fields --------------------------------------------------------

    // ------------------------------------------
    // Instance Definition
    // ------------------------------------------

    private final MillDesktopControl desktopControl;

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------------ Instance Initialization ------------------------------------------
     *
     * @param  name            DOCUMENT ME!
     * @param  desktopControl  DOCUMENT ME!
     */
    public MillAction(final String name, final MillDesktopControl desktopControl) {
        super(name);
        this.desktopControl = desktopControl;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------------ Implementation Interface ------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public abstract Mill getMill();

    // ------------------------------------------
    // AbstractAction Implementation
    // ------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Mill mill;

        mill = getMill();
        if (mill != null) {
            desktopControl.add(mill.getMillFrame());
        } else {
            System.err.println("Null Mill? Probably unparseable settings.");
        }
    }

    /**
     * ------------------------------------------ Instance API ------------------------------------------
     *
     * @param   message       DOCUMENT ME!
     * @param   defaultValue  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected String getSettings(final String message, final String defaultValue) {
        return JOptionPane.showInputDialog(desktopControl.getDesktop(),
                message,
                defaultValue);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * ------------------------------------------ Class Definition ------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    public static class ServerMillAction extends MillAction {

        //~ Static fields/initializers -----------------------------------------

        private static final String NAME = "New Server Mill";
        private static final String MESSAGE = "Please enter the port to listen on.\n"
                    + "Range is 1025 - 65535.";
        private static final String DEFAULT = "4445";

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ServerMillAction object.
         *
         * @param  desktopControl  DOCUMENT ME!
         */
        public ServerMillAction(final MillDesktopControl desktopControl) {
            super(NAME, desktopControl);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Mill getMill() {
            final String portString;

            portString = getSettings(MESSAGE, DEFAULT);
            try {
                final int port = Integer.parseInt(portString);
                return new ServerMill(port);
            } catch (NumberFormatException e) {
                System.err.println("NumberFormatException");
            }
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class ClientMillAction extends MillAction {

        //~ Static fields/initializers -----------------------------------------

        private static final String NAME = "New Client Mill";
        private static final String MESSAGE = "Please enter the host and port to listen to.\n"
                    + "<host>:<port>, 'apps.server.com:4545',"
                    + " '127.0.0.1:6500'";
        private static final String DEFAULT = "localhost:4545";

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ClientMillAction object.
         *
         * @param  desktopControl  DOCUMENT ME!
         */
        public ClientMillAction(final MillDesktopControl desktopControl) {
            super(NAME, desktopControl);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Mill getMill() {
            final String hostPort;
            final String host;
            final String portString;
            final String[] parts;

            hostPort = getSettings(MESSAGE, DEFAULT);
            parts = hostPort.split(":");
            if ((parts != null) && (parts.length == 2)) {
                host = parts[0];
                portString = parts[1];
                try {
                    final int port = Integer.parseInt(portString);
                    return new ClientMill(host, port);
                } catch (NumberFormatException e) {
                    System.err.println("NumberFormatException: "
                                + portString);
                }
            } else {
                System.err.println("Didn't split on ':' correctly: "
                            + hostPort);
            }
            return null;
        }
    }
}
