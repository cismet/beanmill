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
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreePath;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class TreeControl extends MouseAdapter implements FilterControl {

    //~ Instance fields --------------------------------------------------------

    // ------------------------------------------------
    // Instance Definition
    // ------------------------------------------------
    private final TreePanel TREE_PANEL;
    private final TreeView TREE_VIEW;
    private final SeverityListView POPUP = new SeverityListView();

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------------------ Instance Initialization
     * ------------------------------------------------
     */
    public TreeControl() {
        TREE_PANEL = new TreePanel();
        TREE_VIEW = TREE_PANEL.TREE_VIEW;
        TREE_VIEW.addMouseListener(this);
    }

    /**
     * Creates a new TreeControl object.
     *
     * @param  tree  DOCUMENT ME!
     */
    public TreeControl(final Tree tree) {
        TREE_PANEL = new TreePanel(tree);
        TREE_VIEW = TREE_PANEL.TREE_VIEW;
        TREE_VIEW.addMouseListener(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public TreeView getTree() {
        return TREE_VIEW;
    }

    // ------------------------------------------------
    // MouseAdapter Implementation
    // ------------------------------------------------
    @Override
    public void mouseClicked(final MouseEvent e) {
        final TreePath[] paths;
        final Collection nodes;
        Node node;
        final List actions;
        final Point location;

        POPUP.setVisible(false);
        if (MouseEvent.BUTTON3 == e.getButton()) {
            final TreePath path = TREE_VIEW.getPathForLocation(e.getX(), e.getY());
            TREE_VIEW.setSelectionPath(path);
            paths = TREE_VIEW.getSelectionPaths();
            if ((paths == null) || (paths.length == 0)) {
                return;
            }
            nodes = new ArrayList();
            for (int i = 0; i < paths.length; i++) {
                node = (Node)paths[i].getLastPathComponent();
                nodes.add(node);
            }
            actions = SeverityAction.getActions(nodes);
            location = getScreenLocation();

//            showPopup(actions,
//                    (int) (e.getX() + location.getX()),
//                    (int) (e.getY() + location.getY() + 5));
            final Point locP = TREE_PANEL.getLocationOnScreen();
            showPopup(actions, (int)(e.getX() + locP.getX()), (int)(e.getY() + locP.getY() + 5));
        }
    }

    // -----------------------------------------------
    // FilterControl Implementation
    // ---------------------------------------------
    @Override
    public Filter getFilter() {
        return getFilterView().getFilter();
    }

    @Override
    public FilterView getFilterView() {
        return TREE_PANEL;
    }
    /**
     * ------------------------------------------- Private Instance API -------------------------------------------
     *
     * @param  actions  DOCUMENT ME!
     * @param  x        DOCUMENT ME!
     * @param  y        DOCUMENT ME!
     */
    private synchronized void showPopup(final List actions, final int x, final int y) {
        POPUP.setVisible(false);
        POPUP.setActions(actions);
        POPUP.pack();
        POPUP.setLocation(x, y);
        POPUP.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private List getLocations() {
        final List locations;
        Component parent;

        locations = new ArrayList();
        parent = TREE_VIEW;
        while (parent != null) {
            locations.add(parent.getLocation());
            parent = parent.getParent();
        }

        return locations;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Point getScreenLocation() {
        final List locations;
        final Iterator it;
        Point location;
        int screenX = 0;
        int screenY = 0;

        locations = getLocations();
        it = locations.iterator();
        while (it.hasNext()) {
            location = (Point)it.next();
            if (location != null) {
                screenX += location.getX();
                screenY += location.getY();
            }
        }

        return new Point(screenX, screenY);
    }
}
