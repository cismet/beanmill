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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JPopupMenu;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class SeverityListView extends JPopupMenu implements ActionListener {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        setVisible(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  severityActions  DOCUMENT ME!
     */
    public synchronized void setActions(final List severityActions) {
        removeActions();
        final Iterator it = severityActions.iterator();
        while (it.hasNext()) {
            final Action severityAction = (Action)it.next();
            add(severityAction).addActionListener(this);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private synchronized void removeActions() {
        setVisible(false);
        final Object[] elements = getSubElements();
        if (elements != null) {
            for (int i = elements.length - 1; i >= 0; i--) {
                remove(i);
            }
        }
    }
}
