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

import org.jdom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import de.cismet.beanmill.BeanMillPane;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class Node extends DefaultMutableTreeNode implements Comparable, FilterListener, Filter {

    //~ Static fields/initializers ---------------------------------------------

    // ----------------------------------------
    // Class Definition
    // -----------------------------------------
    static final Comparator LIST_COMPARATOR = new Comparator() {

            @Override
            public int compare(final Object a, final Object b) {
                if (a == b) {
                    return 0;
                }
                if (a == null) {
                    return -1;
                }
                if (b == null) {
                    return 1;
                }

                final List listA;
                final List listB;
                int minSize;
                int comparison;
                String aVal;
                String bVal;

                listA = (List)a;
                listB = (List)b;
                minSize = listA.size();
                if (listB.size() < minSize) {
                    minSize = listB.size();
                }
                for (int i = 0; i < minSize; i++) {
                    aVal = (String)listA.get(i);
                    bVal = (String)listB.get(i);
                    comparison = aVal.compareTo(bVal);
                    if (comparison != 0) {
                        return comparison;
                    }
                }
                return listA.size() - listB.size();
            }
        };

    //~ Instance fields --------------------------------------------------------

    // -----------------------------------------
    // Instance Definition
    // -----------------------------------------
    private final Set LISTENERS = Collections.synchronizedSet(new HashSet());
    private final List COMPONENTS;
    private final FilterNotifier NOTIFIER;
    private Severity _severity;
    // ---------------------------------------
    // Filter Implementation
    // ---------------------------------------
    private boolean active = true;

    //~ Instance initializers --------------------------------------------------

    // -----------------------------------------
    // Instance Initialization
    // -----------------------------------------
    {
        NOTIFIER = new FilterNotifier(this);
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Node object.
     *
     * @param  components  DOCUMENT ME!
     */
    public Node(final List components) {
        COMPONENTS = Collections.unmodifiableList(new ArrayList(components));
        if (components.size() == 0) {
            // root severity is never INHERIT
            _severity = Severity.ALL;
        } else {
            _severity = Severity.INHERIT;
        }
        setUserObject(this);
    }
    /**
     * ----------------------------------------- Instance Accessors -----------------------------------------
     *
     * @param  nodeElement  DOCUMENT ME!
     * @param  map          DOCUMENT ME!
     */
    public Node(final Element nodeElement, final Map map) {
        _severity = Severity.INHERIT;
        setActive(true);
        setUserObject(this);
        final String[] path = nodeElement.getAttributeValue("path").split("\\.");
        final List components = Arrays.asList(path);
        // COMPONENTS = Collections.unmodifiableList(new ArrayList(components));
        COMPONENTS = new ArrayList(components);
        setSeverity(Severity.getSeverityByString(nodeElement.getAttributeValue("severity")));
        final List childrenList = nodeElement.getChildren("filternode");
        for (final Object c : childrenList) {
            final Element childElement = (Element)c;
            final Node childNode = new Node(childElement, map);
            add(childNode);
        }
        map.put(getSourceComponents(), this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List getSourceComponents() {
        return COMPONENTS;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Severity getSeverity() {
        return _severity;
    }

    // ---------------------------------------
    // DefaultMutableTreeNode Implementation
    // ---------------------------------------
    @Override
    public void setParent(final MutableTreeNode parent) {
        final Node oldParent;

        oldParent = (Node)getParent();
        if (oldParent != null) {
            oldParent.removeFilterListener(this);
        }
        if (parent != null) {
            ((Node)parent).addFilterListener(this);
        }
        super.setParent(parent);
    }

    // ---------------------------------------
    // FilterListener Implementation
    // ---------------------------------------
    @Override
    public void filterChange(final FilterEvent e) {
        if (Severity.INHERIT.equals(getSeverity())) {
            fireFilterChange();
        }
    }

    // ---------------------------------------
    // Comparable Implementation
    // ---------------------------------------
    @Override
    public int compareTo(final Object o) {
        if (o == null) {
            return 1;
        }
        if (this == o) {
            return 0;
        }
        final Node other = (Node)o;
        final List components;
        final List otherComponents;

        components = getSourceComponents();
        otherComponents = other.getSourceComponents();
        if (components.toString().equals(otherComponents.toString())) {
            return 0;
        } else {
            return LIST_COMPARATOR.compare(components, otherComponents);
        }
    }

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
        if (Severity.INHERIT.equals(getSeverity())) {
            return ((Node)getParent()).isVisible(event);
        }
        final Severity eventSeverity;
        eventSeverity = event.getSeverity();

        if (getSeverity() == Severity.DISABLED_THROW_AWAY) {
            BeanMillPane.getInstance().getLog().remove(event);
            return false;
        } else {
            if (eventSeverity.compareTo(getSeverity()) >= 0) {
                return true;
            } else {
                if (BeanMillPane.getInstance().isDeleteFilteredEvents()) {
                    BeanMillPane.getInstance().getLog().remove(event);
                }
                return false;
            }
        }
    }

    @Override
    public void addFilterListener(final FilterListener listener) {
        NOTIFIER.addFilterListener(listener);
    }

    @Override
    public void removeFilterListener(final FilterListener listener) {
        NOTIFIER.removeFilterListener(listener);
    }

    @Override
    public String getName() {
        return "Node";
    }

    // ---------------------------------------
    // Object Implementation
    // ---------------------------------------
    @Override
    public String toString() {
        return getSourceComponents().toString();
    }
    /**
     * --------------------------------------- Public Instance API ---------------------------------------
     *
     * @param   severity  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public synchronized void setSeverity(final Severity severity) {
        if (severity == null) {
            // severity is never null
            throw new IllegalArgumentException("attempt to set null severity");
        }
        if (Severity.INHERIT.equals(severity) && (getSourceComponents().size() == 0)) {
            // root severity is never INHERIT
            return;
        }
        if (getSeverity().equals(severity)) {
            // no need to propagate events when no change
            return;
        }
        _severity = severity;
        fireFilterChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Severity getEffectiveSeverity() {
        if (!Severity.INHERIT.equals(getSeverity())) {
            return getSeverity();
        }
        return ((Node)getParent()).getEffectiveSeverity();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getNodeString() {
        final List components;
        String text;

        components = getSourceComponents();
        if (components.size() == 0) {
            text = "root";
        } else {
            text = (String)components.get(components.size() - 1);
        }

        return text;
    }
    /**
     * --------------------------------------- Private Instance API ---------------------------------------
     */
    private void fireFilterChange() {
        NOTIFIER.fireFilterChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Element getXMLElement() {
        final Element e = new Element("filternode");
        e.setAttribute("severity", this.getSeverity().toString());
        String path = "";
        for (final Object o : COMPONENTS) {
            if (!path.equals("")) {
                path += ".";
            }
            path += o.toString();
        }
        e.setAttribute("path", path);
        for (int i = 0; i < getChildCount(); ++i) {
            final Node child = (Node)getChildAt(i);
            e.addContent(child.getXMLElement());
        }
        return e;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Node) {
            final Node tester = (Node)o;
            return COMPONENTS.toString().equals(tester.COMPONENTS.toString());
        } else {
            return false;
        }
    }
}
