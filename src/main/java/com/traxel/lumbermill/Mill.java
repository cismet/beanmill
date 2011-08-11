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

import com.traxel.lumbermill.desk.MillFrame;
import com.traxel.lumbermill.desk.MillFrameControl;
import com.traxel.lumbermill.event.EventListener;
import com.traxel.lumbermill.event.EventListenerStatus;
import com.traxel.lumbermill.log.Log;

import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class Mill {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(Mill.class);

    //~ Instance fields --------------------------------------------------------

    // ------------------------------------------
    // Instance Definition
    // ------------------------------------------
    private final Log log;
    private final MillFrameControl millFrameControl;

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------------ Instance Initialization ------------------------------------------
     *
     * @param  name  DOCUMENT ME!
     */
    public Mill(final String name) {
        this(name, null);
    }

    /**
     * Creates a new Mill object.
     *
     * @param  name  DOCUMENT ME!
     * @param  log   DOCUMENT ME!
     */
    public Mill(final String name, final Log log) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("creating new Mill with name '" + name + "' and log: "
                        + log);
        }
        this.log = log;
        millFrameControl = new MillFrameControl(this, name);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------------- Instance Accessors ------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public MillFrame getMillFrame() {
        return millFrameControl.getFrame();
    }
    /**
     * ---------------------------------------- Instance Convenience Accessors ----------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    protected Log getLog() {
        if (log == null) {
            return millFrameControl.getLog();
        } else {
            return log;
        }
    }
    /**
     * ---------------------------------------- Implementation API ----------------------------------------
     */
    public abstract void closeEventListeners();
    /**
     * ---------------------------------------- Inherited API ----------------------------------------
     *
     * @param  eventL  DOCUMENT ME!
     */
    protected void setEventListener(final EventListener eventL) {
        getMillFrame().setEventListenerStatus(new EventListenerStatus(eventL));
    }
}
