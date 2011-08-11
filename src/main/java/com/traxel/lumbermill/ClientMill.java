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
import com.traxel.lumbermill.event.EventListenerClient;

import java.io.IOException;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ClientMill extends Mill {

    //~ Instance fields --------------------------------------------------------

    // -------------------------------------------
    // Instance Definition
    // -------------------------------------------

    private final EventListener eventListener;

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------------- Instance Initialization -------------------------------------------
     *
     * @param  host  DOCUMENT ME!
     * @param  port  DOCUMENT ME!
     */
    public ClientMill(final String host, final int port) {
        super("Client: " + host + ":" + port);
        EventListener listener = null;

        try {
            listener = new EventListenerClient(getLog(),
                    host,
                    port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        eventListener = listener;
        setEventListener(eventListener);
    }

    //~ Methods ----------------------------------------------------------------

    // ------------------------------------------
    // Mill Implementation
    // ------------------------------------------

    @Override
    public void closeEventListeners() {
        if (eventListener != null) {
            eventListener.close();
        }
    }
}
