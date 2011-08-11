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

import com.traxel.lumbermill.event.EventListener;
import com.traxel.lumbermill.event.EventListenerServer;
import com.traxel.lumbermill.log.Log;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ServerMill extends Mill {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(
            ServerMill.class);

    //~ Instance fields --------------------------------------------------------

    // ------------------------------------------
    // Instance Definition
    // ------------------------------------------
    private final EventListener eventListener;

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------------ Instance Initialization ------------------------------------------
     *
     * @param  port  DOCUMENT ME!
     */
    public ServerMill(final int port) {
        this(port, null);
    }

    /**
     * Creates a new ServerMill object.
     *
     * @param  port  DOCUMENT ME!
     * @param  log   DOCUMENT ME!
     */
    public ServerMill(final int port, final Log log) {
        super("Server: " + port, log);
        EventListener listener = null;
        try {
            listener = new EventListenerServer(getLog(), port);
        } catch (IOException e) {
            LOG.warn("could not create EventListenerServer for log and port: "
                        + getLog() + " :: " + port);
        }
        eventListener = listener;
        setEventListener(eventListener);
    }

    //~ Methods ----------------------------------------------------------------

    // ---------------------------------------
    // Public Instance API
    // ---------------------------------------
    @Override
    public void closeEventListeners() {
        if (eventListener != null) {
            eventListener.close();
        }
    }
}
