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

import org.apache.log4j.Logger;

import org.openide.util.Exceptions;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class EventListenerServer extends EventListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(
            EventListenerServer.class);

    //~ Instance fields --------------------------------------------------------

    private final ServerSocket serverSocket;
    private final int port;
    private int clientCount;
    private boolean stopRequest;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EventListenerServer object.
     *
     * @param   sink  DOCUMENT ME!
     * @param   port  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public EventListenerServer(final Log sink, final int port) throws IOException {
        super(sink);
        this.port = port;
        stopRequest = false;
        serverSocket = openServerSocket(port);
        start();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   port  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    private ServerSocket openServerSocket(final int port) throws IOException {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
            socket.setSoTimeout(1000);
        } catch (final IOException e) {
            LOG.error("could not create server socket", e); // NOI18N
            Exceptions.printStackTrace(e);

            if (socket != null) {
                try {
                    socket.close();
                } catch (final IOException e2) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("could not close socket", e2);
                    }
                    // this socket is dead and about
                    // to go out of scope - ignore
                    throw e;
                }
            }
            throw e;
        }

        return socket;
    }

    @Override
    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    @Override
    public void run() {
        while (!stopRequest) {
            try {
                listen(serverSocket.accept());
            } catch (final SocketTimeoutException e) {
                // this is normal - gives the thread
                // a chance to stop
            } catch (final SocketException e) {
                LOG.error("error during listening", e);
                pleaseStop();
            } catch (final IOException e) {
                LOG.error("error during listening", e);
                pleaseStop();
            }
        }
    }

    @Override
    public void close() {
        pleaseStop();
        if (serverSocket != null) {
            try {
                // wait for the socket to stop
                synchronized (this) {
                    this.wait(1000);
                }
            } catch (final Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("could not wait for one second", e);
                }
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOG.warn("could not close server socket", e);
            }
        }
        firePropertyChange(STATUS_PROPERTY, null, getStatus());
    }

    @Override
    public String getPortString() {
        return String.valueOf(port);
    }

    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public String getType() {
        return "Server";
    }

    @Override
    public String getStatus() {
        if (serverSocket.isBound()) {
            return (clientCount + " Clients");
        } else {
            return ("Closed");
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void pleaseStop() {
        stopRequest = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  source  DOCUMENT ME!
     */
    private void listen(final Socket source) {
        new SocketListener(source).start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  amount  DOCUMENT ME!
     */
    private synchronized void adjustClientCount(final int amount) {
        clientCount += amount;
        firePropertyChange(STATUS_PROPERTY, null, getStatus());
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class SocketListener extends Thread {

        //~ Instance fields ----------------------------------------------------

        private final Socket socket;
        private final ObjectInputStream stream;
        private boolean stopRequest = false;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SocketListener object.
         *
         * @param  socket  DOCUMENT ME!
         */
        public SocketListener(final Socket socket) {
            ObjectInputStream ois = null;
            this.socket = socket;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (final IOException e) {
                LOG.warn("could not open ObjectInputStream from socket: "
                            + socket, e);
            }
            stream = ois;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            adjustClientCount(1);
            if (stream != null) {
                while (!stopRequest) {
                    final Event event = Event.create(readObject());
                    if (event != null) {
                        add(event);
                    }
                }
            }
            adjustClientCount(-1);
        }

        @Override
        public void finalize() throws Throwable {
            try {
                socket.close();
            } catch (final Exception e) {
                LOG.warn("could not close socket: " + socket, e);
            }
            try {
                stream.close();
            } catch (final Exception e) {
                LOG.warn("could not close stream: " + stream, e);
            }
            super.finalize();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private Object readObject() {
            try {
                return stream.readObject();
            } catch (final EOFException e) {
                // this is "normal" for JSR47.
// if(LOG.isDebugEnabled())
// LOG.debug("received EOFException", e);
                stopRequest = true;
            } catch (final Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("received Exception", e);
                }
                stopRequest = true;
            }
            return null;
        }
    }
}
