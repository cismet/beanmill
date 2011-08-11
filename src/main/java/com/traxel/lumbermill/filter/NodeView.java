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

import java.awt.Component;

import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class NodeView extends DefaultTreeCellRenderer {

    //~ Methods ----------------------------------------------------------------

    // --------------------------------------
    // TreeCellRenderer API
    // --------------------------------------

    @Override
    public Component getTreeCellRendererComponent(final JTree tree,
            final Object value,
            final boolean selected,
            final boolean expanded,
            final boolean leaf,
            final int row,
            final boolean hasFocus) {
        final String text;
        final Node node;
        final Severity effective;
        final Severity actual;

        node = (Node)value;
        text = node.getNodeString();

        this.hasFocus = hasFocus;
        this.selected = selected;
        setText(text);

        if (selected) {
            setForeground(getTextSelectionColor());
        } else {
            setForeground(getTextNonSelectionColor());
        }

        setEnabled(true);
        actual = node.getSeverity();
        effective = node.getEffectiveSeverity();

        setIcon(new SeverityView(actual, effective, node.isRoot(), node.isLeaf(), false));

        setComponentOrientation(tree.getComponentOrientation());
        return this;
    }
}
