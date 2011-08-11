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
package com.traxel.lumbermill.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class EventListenerStatus extends JPanel implements PropertyChangeListener {

    //~ Instance fields --------------------------------------------------------

    private final EventListener listener;
    private final JTextField txtStatus;
    private final JTextField txtType;
    private final JTextField txtHost;
    private final JTextField txtPort;
    private final JLabel lblStatus;
    private final JLabel lblType;
    private final JLabel lblHost;
    private final JLabel lblPort;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EventListenerStatus object.
     *
     * @param  listener  DOCUMENT ME!
     */
    public EventListenerStatus(final EventListener listener) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.listener = listener;
        txtStatus = new JTextField(10);
        txtType = new JTextField(5);
        txtHost = new JTextField(10);
        txtPort = new JTextField(5);
        lblStatus = new JLabel("Listener Status: ", JLabel.TRAILING);
        lblType = new JLabel("  Type: ", JLabel.TRAILING);
        lblHost = new JLabel("  Host: ", JLabel.TRAILING);
        lblPort = new JLabel("  Port: ", JLabel.TRAILING);
        lblStatus.setLabelFor(txtStatus);
        lblType.setLabelFor(txtType);
        lblHost.setLabelFor(txtHost);
        lblPort.setLabelFor(txtPort);
        add(lblStatus);
        add(txtStatus);
        add(lblType);
        add(txtType);
        add(lblHost);
        add(txtHost);
        add(lblPort);
        add(txtPort);
        txtStatus.setEditable(false);
        txtType.setEditable(false);
        txtHost.setEditable(false);
        txtPort.setEditable(false);
        if (listener != null) {
            txtStatus.setText(listener.getStatus());
            txtType.setText(listener.getType());
            txtHost.setText(listener.getHost());
            txtPort.setText(listener.getPortString());
            listener.addPropertyListener(this);
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        if (listener.equals(event.getSource())) {
            final String property = event.getPropertyName();
            if (EventListener.STATUS_PROPERTY.equals(property)) {
                final Object newValue = event.getNewValue();
                if (newValue == null) {
                    txtStatus.setText("");
                } else {
                    txtStatus.setText(newValue.toString());
                }
            }
        }
    }
}
