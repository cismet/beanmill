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

import org.apache.log4j.Logger;

import org.jdom.Element;

import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.cismet.beanmill.filter.ListControl;
import de.cismet.beanmill.filter.ListView;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class FilterSetControl {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(
            FilterSetControl.class);

    //~ Instance fields --------------------------------------------------------

    private final HashMap<FilterControl, Filter> filterPerTree;
    private final List<FilterControl> allFilterControls;
    private final FilterSetView filterSetView;
    private final FilterSet filterSet;
    private final FilterSetMenu filterSetMenu;
    private final TreeControl treeControl;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FilterSetControl object.
     */
    public FilterSetControl() {
        filterPerTree = new HashMap<FilterControl, Filter>();
        filterSetView = new FilterSetView();
        filterSet = new FilterSet();
        treeControl = new TreeControl();
        final List<FilterControl> filterControls = new ArrayList();
        filterControls.add(treeControl);
        allFilterControls = Collections.unmodifiableList(filterControls);
        final JCheckBoxMenuItem[] filterItems = new JCheckBoxMenuItem[allFilterControls.size()];
        for (int i = 0; i < allFilterControls.size(); i++) {
            final FilterControl fc = allFilterControls.get(i);
            final Action action = new FilterCheckBoxAction(fc);
            filterItems[i] = new JCheckBoxMenuItem(action);
            filterItems[i].setState(true);
            apply(fc);
        }
        filterSetMenu = new FilterSetMenu("Filters", filterItems);
        filterSetView.addChangeListener(new ChangeListener() {

                // This method is called whenever the selected tab changes
                @Override
                public void stateChanged(final ChangeEvent evt) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("stateChanged: " + evt);
                    }
                    for (final FilterControl fc : filterPerTree.keySet()) {
                        if (!(fc.getFilter() instanceof Regex)) {
                            fc.getFilter().setActive(false);
                        }
                    }
                    try {
                        getActiveComponent().getFilter().setActive(true);
                        filterSet.fireFilterAdded(null);
                    } catch (Exception e) {
                        LOG.warn("could not set active filter", e);
                    }
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FilterView getActiveComponent() {
        return ((FilterView)filterSetView.getSelectedComponent());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FilterSetView getFilterSetView() {
        return filterSetView;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FilterSet getFilterSet() {
        return filterSet;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FilterSetMenu getFilterSetMenu() {
        return filterSetMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ListView addListView() {
        return addListView((Element)null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   xml  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ListView addListView(final Element xml) {
        final ListControl lc = new ListControl(filterSet, xml);
        apply(lc);
        filterSetView.setSelectedComponent((ListView)lc.getFilterView());
        return (ListView)lc.getFilterView();
    }

    /**
     * DOCUMENT ME!
     */
    public void addTree() {
        apply(new TreeControl());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tree  DOCUMENT ME!
     */
    public void addTree(final Tree tree) {
        apply(new TreeControl(tree));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  control  DOCUMENT ME!
     */
    private synchronized void apply(final FilterControl control) {
        final Filter filter = control.getFilter();
        final FilterView view = control.getFilterView();
        filterSetView.addTab(filter.getName(), view.getFilterComponent());
        filterSet.add(filter);
        filterPerTree.put(control, filter);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  control  DOCUMENT ME!
     */
    private synchronized void unapply(final FilterControl control) {
        final Filter filter = control.getFilter();
        final FilterView view = control.getFilterView();
        filterSetView.remove(view.getFilterComponent());
        filterSet.remove(filter);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class FilterCheckBoxAction extends AbstractAction {

        //~ Instance fields ----------------------------------------------------

        private final FilterControl filterControl;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new FilterCheckBoxAction object.
         *
         * @param  control  DOCUMENT ME!
         */
        public FilterCheckBoxAction(final FilterControl control) {
            super(control.getFilter().getName());
            filterControl = control;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JCheckBoxMenuItem item = (JCheckBoxMenuItem)e.getSource();
            if (item.getState()) {
                apply(filterControl);
            } else {
                unapply(filterControl);
            }
        }
    }
}
