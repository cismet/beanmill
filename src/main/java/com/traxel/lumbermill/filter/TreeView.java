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

import javax.swing.JTree;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class TreeView extends JTree implements FilterListener {

    //~ Instance fields --------------------------------------------------------

    private final Tree tree;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TreeView object.
     */
    public TreeView() {
        this(new Tree());
    }

    /**
     * Creates a new TreeView object.
     *
     * @param  tree  DOCUMENT ME!
     */
    public TreeView(final Tree tree) {
        super(tree);
        this.tree = tree;
        this.tree.addFilterListener(this);
        setEditable(false);
        setShowsRootHandles(true);
        setCellRenderer(new NodeView());
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void filterChange(final FilterEvent e) {
        if (getModel().equals(e.getSource())) {
            treeDidChange();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Filter getFilter() {
        return tree;
    }
}
