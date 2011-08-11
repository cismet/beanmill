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

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class RegexControl implements FilterControl, ActionListener {

    //~ Instance fields --------------------------------------------------------

    // -----------------------------------------------
    // Instance Definition
    // ----------------------------------------------

    private final RegexView VIEW = new RegexView();

    //~ Instance initializers --------------------------------------------------

    // -----------------------------------------------
    // Instance Initialization
    // -----------------------------------------------

    {
        VIEW.FIELD.addActionListener(this);
    }

    //~ Methods ----------------------------------------------------------------

    // -----------------------------------------------
    // FilterControl Implementation
    // ----------------------------------------------

    @Override
    public FilterView getFilterView() {
        return VIEW;
    }

    @Override
    public Filter getFilter() {
        return VIEW.getFilter();
    }

    // --------------------------------------------
    // ActionListener Implementation
    // --------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
            VIEW.REGEX.setRegex(VIEW.FIELD.getText());
        } catch (Exception skip) {
        }
    }
}
