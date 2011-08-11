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
package com.traxel.lumbermill.log;

import java.awt.event.ActionEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class LogStatus extends JPanel implements PropertyChangeListener {

    //~ Instance fields --------------------------------------------------------

    // ---------------------------------------------
    // Instance Definition
    // ---------------------------------------------

    private final Log LOG;
    private final JTextField SIZE_FIELD = new JTextField(5);
    private final JTextField INFO_FIELD = new JTextField(16);
    private final JLabel SIZE_LABEL = new JLabel("  Log Size: ", JLabel.TRAILING);
    private final JLabel INFO_LABEL = new JLabel("  ", JLabel.TRAILING);

    private final Action SIZE_ACTION = new SizeAction();
    private final Timer TIMER = new Timer(1000, SIZE_ACTION);

    //~ Constructors -----------------------------------------------------------

    /**
     * --------------------------------------------- Instance Initialization
     * ---------------------------------------------
     *
     * @param  log  DOCUMENT ME!
     */
    public LogStatus(final Log log) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        LOG = log;
        SIZE_LABEL.setLabelFor(SIZE_FIELD);
        INFO_LABEL.setLabelFor(INFO_FIELD);
        add(SIZE_LABEL);
        add(SIZE_FIELD);
        add(INFO_LABEL);
        add(INFO_FIELD);
        SIZE_FIELD.setEditable(false);
        INFO_FIELD.setEditable(false);
        TIMER.setRepeats(true);
        LOG.addPropertyListener(this);
        TIMER.start();
    }

    //~ Methods ----------------------------------------------------------------

    // ---------------------------------------------
    // PropertyChangeListener Implementation
    // ---------------------------------------------

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        if (LOG.equals(event.getSource())) {
            final String propertyName;
            Object newValue;

            INFO_FIELD.setText("");
            propertyName = event.getPropertyName();
            newValue = event.getNewValue();
            if (Log.INFO_PROPERTY_NAME.equals(propertyName)) {
                if (newValue == null) {
                    newValue = "";
                }
                INFO_FIELD.setText(newValue.toString());
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class SizeAction extends AbstractAction {

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            SIZE_FIELD.setText("" + LOG.size());
        }
    }
}
