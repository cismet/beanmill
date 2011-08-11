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

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class TreePanel extends JScrollPane implements FilterView {

    //~ Instance fields --------------------------------------------------------

    // ----------------------------------------
    // Instance Definition
    // ---------------------------------------

    public final TreeView TREE_VIEW;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------------------------------- Instance Initialization -----------------------------------------
     */
    public TreePanel() {
        super(new TreeView());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        TREE_VIEW = (TreeView)getViewport().getView();
        setPreferredSize(new Dimension(150, 400));
    }
    /**
     * Creates a new TreePanel object.
     *
     * @param  tree  DOCUMENT ME!
     */
    public TreePanel(final Tree tree) {
        super(new TreeView(tree));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        TREE_VIEW = (TreeView)getViewport().getView();
        setPreferredSize(new Dimension(150, 400));
    }

    //~ Methods ----------------------------------------------------------------

    // -----------------------------------------
    // FilterView Implementation
    // -----------------------------------------

    @Override
    public Component getFilterComponent() {
        return this;
    }

    @Override
    public Filter getFilter() {
        return TREE_VIEW.getFilter();
    }
}
