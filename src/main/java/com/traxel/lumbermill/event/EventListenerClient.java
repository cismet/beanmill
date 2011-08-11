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

import com.traxel.lumbermill.log.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.net.Socket;
import java.net.SocketException;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class EventListenerClient extends EventListener {

    //~ Instance fields --------------------------------------------------------

    // ------------------------------------------------
    // Instance Definition
    // ------------------------------------------------

    private final Socket SOCKET;
    private final ObjectInputStream STREAM;
    private final String HOST;
    private final int PORT;
    private boolean _stopRequested = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------------------ Instance Initialization
     * ------------------------------------------------
     *
     * @param   sink  DOCUMENT ME!
     * @param   host  DOCUMENT ME!
     * @param   port  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public EventListenerClient(final Log sink, final String host, final int port) throws IOException {
        super(sink);
        final InputStream rawStream;

        PORT = port;
        HOST = host;
        SOCKET = new Socket(HOST, PORT);
        rawStream = SOCKET.getInputStream();
        STREAM = new ObjectInputStream(rawStream);
        start();
    }

    //~ Methods ----------------------------------------------------------------

    // -------------------------------------------------
    // Thread Implementation
    // -------------------------------------------------

    @Override
    public void run() {
        if (STREAM != null) {
            Event event;
            while (!_stopRequested) {
                event = null;
                try {
                    event = Event.create(readObject());
                } catch (SocketException e) {
                    if (!_stopRequested) {
                        // when close is called, the socket
                        // gets killed before stream.flush
                        // is called from the sending side.
                        e.printStackTrace();
                        close();
                    }
                }
                if (event != null) {
                    add(event);
                }
            }
        }
    }

    // -----------------------------------------------
    // Object Implementation
    // -----------------------------------------------

    @Override
    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    // -----------------------------------------------
    // EventListener Implementation
    // -----------------------------------------------

    @Override
    public void close() {
        pleaseStop();
        if (SOCKET != null) {
            // it won't close naturally.  Just kill it.
            try {
                SOCKET.close();
            } catch (Exception e) {
                // dying anyway, no worries.
            }
        }
        firePropertyChange(STATUS_PROPERTY,
            null,
            getStatus());
    }

    @Override
    public String getType() {
        return "Client";
    }

    @Override
    public String getPortString() {
        return String.valueOf(PORT);
    }

    @Override
    public String getHost() {
        return HOST;
    }

    @Override
    public String getStatus() {
        if (SOCKET.isBound() && !SOCKET.isClosed()) {
            return ("Connected");
        } else {
            return ("Disconnected");
        }
    }

    /**
     * ----------------------------------------------- Instance API -----------------------------------------------
     *
     * @return  DOCUMENT ME!
     *
     * @throws  SocketException  DOCUMENT ME!
     */
    private Object readObject() throws SocketException {
        Object o = null;

        try {
            o = STREAM.readObject();
        } catch (EOFException e) {
            close();
        } catch (SocketException e) {
            // this happens when close is called, and
            // the last call to STREAM.readObject is
            // still blocking.
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return o;
    }

    /**
     * DOCUMENT ME!
     */
    private void pleaseStop() {
        _stopRequested = true;
    }
}
