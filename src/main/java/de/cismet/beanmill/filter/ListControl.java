/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ListControl.java
 *
 * Created on 24. August 2007, 16:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.beanmill.filter;

import com.traxel.lumbermill.filter.Filter;
import com.traxel.lumbermill.filter.FilterControl;
import com.traxel.lumbermill.filter.FilterSet;
import com.traxel.lumbermill.filter.FilterView;

import org.jdom.Element;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class ListControl implements FilterControl {

    //~ Instance fields --------------------------------------------------------

    private final ListView listView;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ListControl.
     *
     * @param  fs   DOCUMENT ME!
     * @param  xml  DOCUMENT ME!
     */
    public ListControl(final FilterSet fs, final Element xml) {
        listView = new ListView(fs, xml);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public FilterView getFilterView() {
        return listView;
    }

    @Override
    public Filter getFilter() {
        return listView.getFilter();
    }
}
