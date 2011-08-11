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

import com.traxel.lumbermill.event.Event;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public interface LogListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------- Implementation API -------------------------------------
     *
     * @param  source  DOCUMENT ME!
     * @param  event   DOCUMENT ME!
     */
    void added(Log source, Event event);

    /**
     * No, this is not safe - you can change the contents of events. Don't. This is for performance.
     *
     * @param  source  DOCUMENT ME!
     * @param  events  DOCUMENT ME!
     */
    void removed(Log source, Event[] events);

    /**
     * DOCUMENT ME!
     *
     * @param  source  DOCUMENT ME!
     */
    void cleared(Log source);
}
