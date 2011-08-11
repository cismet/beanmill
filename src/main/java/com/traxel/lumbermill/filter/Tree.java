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

import com.traxel.lumbermill.event.Event;
import com.traxel.lumbermill.event.Severity;

import org.apache.log4j.Logger;

import org.jdom.Element;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class Tree extends DefaultTreeModel implements Filter, FilterListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(Tree.class);

    //~ Instance fields --------------------------------------------------------

    private final Map map;
    private final TimerNotifier timerNotifier;
    private boolean active;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Tree object.
     */
    public Tree() {
        super(new Node(Collections.EMPTY_LIST));
        timerNotifier = new TimerNotifier(this);
        map = new HashMap();
        active = true;
    }

    /**
     * Creates a new Tree object.
     *
     * @param  treeFilter  DOCUMENT ME!
     */
    public Tree(final Element treeFilter) {
        this();
        // In rootElement steht der Node mit path="" (root)
        final Element rootElement = treeFilter.getChild("filternode");
        final Node n = (Node)getRoot();
        n.setSeverity(Severity.getSeverityByString(rootElement.getAttributeValue("severity")));
        final List children = rootElement.getChildren("filternode");
        for (final Object c : children) {
            final Element childElement = (Element)c;
            final Node childNode = new Node(childElement, map);
            childNode.addFilterListener(this);
            map.put(childNode.getSourceComponents(), childNode);
            ((Node)getRoot()).add(childNode);
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }

    @Override
    public boolean isVisible(final Event event) {
        final Node node = getNode(event);
        return node.isVisible(event);
    }

    @Override
    public void addFilterListener(final FilterListener listener) {
        timerNotifier.addFilterListener(listener);
    }

    @Override
    public void removeFilterListener(final FilterListener listener) {
        timerNotifier.addFilterListener(listener);
    }

    @Override
    public String getName() {
        return "Node Tree";
    }

    @Override
    public void filterChange(final FilterEvent e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("filterChange event: " + e + " :: source: " + e.getSource());
        }
        if (e.getSource() instanceof Node) {
            timerNotifier.fireFilterChange();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   event  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public synchronized Node getNode(final Event event) {
        return getNode(event.getSourceComponents());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   components  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private synchronized Node getNode(final List components) {
        if (components.size() == 0) {
            return (Node)getRoot();
        }
        final Node node;
        final Node parent;
        if (map.get(components) != null) {
            node = (Node)map.get(components);
            return node;
        } else {
            node = new Node(components);
            node.addFilterListener(this);
            map.put(components, node);
        }
        if (components.size() > 1) {
            final List parentComponents = components.subList(0, components.size() - 1);
            parent = getNode(parentComponents);
        } else {
            parent = (Node)getRoot();
        }
        insertNodeInto(node, parent);
        return node;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  child   DOCUMENT ME!
     * @param  parent  DOCUMENT ME!
     */
    private synchronized void insertNodeInto(final Node child, final Node parent) {
        boolean added = false;
        final List children = Collections.list(parent.children());
        for (int i = 0; i < children.size(); i++) {
            final Node other = (Node)children.get(i);
            if (child.compareTo(other) < 0) {
                insertNodeInto(child, parent, i);
                added = true;
                break;
            }
        }
        if (!added) {
            parent.add(child);
            final int[] newIndexs = new int[1];
            newIndexs[0] = parent.getIndex(child);
            nodesWereInserted(parent, newIndexs);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Element getXML() {
        final Element tree = new Element("treefilter");
        final Node rootNode = (Node)getRoot();
        tree.addContent(rootNode.getXMLElement());
        return tree;
    }
}
