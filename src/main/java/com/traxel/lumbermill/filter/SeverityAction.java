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
package com.traxel.lumbermill.filter;

import com.traxel.lumbermill.event.Severity;
import com.traxel.lumbermill.event.SeverityView;

import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class SeverityAction extends AbstractAction {

    //~ Instance fields --------------------------------------------------------

    // -------------------------------------------
    // Instance Definition
    // -------------------------------------------

    private final Severity _severity;
    private final Collection _nodes;

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------------- Instance Initialization -------------------------------------------
     * TODO Hier muss noch was gemacht werden.
     *
     * @param  severity  DOCUMENT ME!
     * @param  nodes     DOCUMENT ME!
     */
    public SeverityAction(final Severity severity, final Collection nodes) {
        super(severity.toString(),
            new SeverityView(severity, severity, false, false, true));
        _severity = severity;
        _nodes = nodes;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------------- Class API -------------------------------------------
     *
     * @param   nodes  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static List getActions(final Collection nodes) {
        final List levels;
        final Iterator it;
        Severity severity;
        SeverityAction action;
        final List actions;

        actions = new ArrayList();
        levels = Severity.getLevels();
        it = levels.iterator();
        while (it.hasNext()) {
            severity = (Severity)it.next();
            action = new SeverityAction(severity, nodes);
            actions.add(action);
        }

        return actions;
    }

    /**
     * ------------------------------------------- Instance Accessors -------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    private Collection getNodes() {
        return _nodes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Severity getSeverity() {
        return _severity;
    }

    // -------------------------------------------
    // Action Implementation
    // -------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Iterator it;
        Node node;

        if (getNodes() != null) {
            it = getNodes().iterator();
            while (it.hasNext()) {
                node = (Node)it.next();
                node.setSeverity(getSeverity());
            }
        }
    }
}
