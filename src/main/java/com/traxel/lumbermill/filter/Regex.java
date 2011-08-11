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

import java.util.regex.Pattern;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class Regex implements Filter {

    //~ Static fields/initializers ---------------------------------------------

    // -----------------------------------------
    // Class Definition
    // -----------------------------------------

    public static final int MESSAGE = 0;

    //~ Instance fields --------------------------------------------------------

    // -----------------------------------------
    // Instance Definition
    // -----------------------------------------

    private final FilterNotifier FILTER_NOTIFIER;
    private Pattern _pattern;

    // -----------------------------------------
    // Filter Implementation
    // -----------------------------------------
    private boolean active = true;

    //~ Instance initializers --------------------------------------------------

    // -----------------------------------------
    // Instance Initialization
    // -----------------------------------------

    {
        FILTER_NOTIFIER = new FilterNotifier(this);
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Regex object.
     */
    public Regex() {
    }

    /**
     * Creates a new Regex object.
     *
     * @param  field  DOCUMENT ME!
     */
    public Regex(final int field) {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------------ Instance Accessors ------------------------------------------
     *
     * @param   regex  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void setRegex(String regex) throws Exception {
        // System.out.println("REGEX set to:"+regex);
        if ((regex == null) || "".equals(regex)) {
            _pattern = null;
        } else {
            regex = ".*" + regex + ".*";
            regex = regex.replaceAll("(\\.\\*)+", ".*");
            _pattern = Pattern.compile(regex, Pattern.DOTALL);
        }
        FILTER_NOTIFIER.fireFilterChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Pattern getPattern() {
        return _pattern;
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
    public void addFilterListener(final FilterListener listener) {
        FILTER_NOTIFIER.addFilterListener(listener);
    }

    @Override
    public void removeFilterListener(final FilterListener listener) {
        FILTER_NOTIFIER.addFilterListener(listener);
    }

    @Override
    public boolean isVisible(final Event event) {
        if (getPattern() == null) {
            return true;
        }
        if (event.getMessage() == null) {
            return false;
        }
        return getPattern().matcher(event.getMessage()).matches();
    }

    @Override
    public String getName() {
        return "Regex";
    }
}
