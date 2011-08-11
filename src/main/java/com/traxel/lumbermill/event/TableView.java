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

import com.traxel.lumbermill.filter.FilterSet;
import com.traxel.lumbermill.log.Log;

import org.apache.log4j.Logger;

import org.openide.util.NbPreferences;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

import de.cismet.beanmill.NetbeansPanel;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class TableView extends JTable {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(TableView.class);

    //~ Instance fields --------------------------------------------------------

    private transient volatile boolean hotkeyPressed;

    //~ Constructors -----------------------------------------------------------

    /**
     * ------------------------------------- Instance Initialization -------------------------------------
     *
     * @param  log        DOCUMENT ME!
     * @param  filterSet  DOCUMENT ME!
     * @param  columnSet  DOCUMENT ME!
     */
    public TableView(final Log log, final FilterSet filterSet, final ColumnSet columnSet) {
        super(new Table(log, filterSet, columnSet));
        hotkeyPressed = false;
        // When a JTable allows multiple selection, and
        // the first row is selected, and a new row is inserted
        // at index 0, the new row gets added to the selection
        // set.  As a result, when you have a few hundred
        // rows per second being inserted at index 0, you
        // get a a few hundred listselectionevents per second.
        // This is a Bad Thing.
        // Fix Borken JTable
        getModel().addTableModelListener(new Unselector());

        setColumnModel(getTable().getColumnSet());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // listener for hotkey down
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

                @Override
                public void eventDispatched(final AWTEvent event) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("received event: " + event);
                    }
                    final KeyEvent keyEvent = (KeyEvent)event;
                    switch (keyEvent.getID()) {
                        case KeyEvent.KEY_TYPED: {
                            return;
                        }
                        case KeyEvent.KEY_PRESSED: {
                            if (keyEvent.getKeyCode()
                                        == NbPreferences.forModule(TableView.class).getInt(
                                            NetbeansPanel.PROP_TOOLTIP_HK,
                                            -1)) {
                                hotkeyPressed = true;
                            }
                            return;
                        }
                        case KeyEvent.KEY_RELEASED: {
                            if (keyEvent.getKeyCode()
                                        == NbPreferences.forModule(TableView.class).getInt(
                                            NetbeansPanel.PROP_TOOLTIP_HK,
                                            -1)) {
                                hotkeyPressed = false;
                            }
                            return;
                        }
                        default: {
                            return;
                        }
                    }
                }
            }, AWTEvent.KEY_EVENT_MASK);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component prepareRenderer(final TableCellRenderer renderer, final int rowIndex, final int vColIndex) {
        final Component c = super.prepareRenderer(renderer, rowIndex,
                vColIndex);
        if (c instanceof JComponent) {
            final JComponent jc = (JComponent)c;
            final Table model = getTable();
            final Event event = model.getEvent(rowIndex);
            try {
                final int htmlWitdh = 1000;
                final String tablePattern = "<html><table {0} border=\"0"
                            + "\"><tr><td>" + model.getEvent(rowIndex).getHtml() + "</td></tr></table></html>";
                final String widthTable = MessageFormat.format(tablePattern,
                        "width=\""
                                + htmlWitdh
                                + "\"");
                final String normalTable = MessageFormat.format(tablePattern,
                        "");

                if (hotkeyPressed) {
                    if (new JLabel(normalTable).getPreferredSize().width
                                <= htmlWitdh) {
                        jc.setToolTipText(normalTable);
                    } else {
                        jc.setToolTipText(widthTable);
                    }
                } else {
                    jc.setToolTipText(null);
                }
            } catch (final Exception e) {
                // LOG.error("error while preparing renderer", e);
            }
            if (!((renderer instanceof SeverityCellView)
                            || this.getSelectionModel().isSelectedIndex(rowIndex))) {
                if (event instanceof XMLEvent) {
                    jc.setBackground(new Color(236, 233, 216));
                } else {
                    jc.setBackground(Color.white);
                }
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("renderer component: " + c);
            }
        }
        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Table getTable() {
        return (Table)getModel();
    }
    /**
     * ------------------------------------ Public API ------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    public Event getFirstSelectedEvent() {
        final int firstSelectedRow = getSelectedRow();
        if (firstSelectedRow > -1) {
            return getTable().getEvent(firstSelectedRow);
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Event getLastSelectedEvent() {
        final int[] selectedRows = getSelectedRows();
        if ((selectedRows != null) && (selectedRows.length != 0)) {
            return getTable().getEvent(selectedRows[selectedRows.length - 1]);
        }
        return null;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * ------------------------------------- Instance Definition -------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    private class Unselector implements TableModelListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void tableChanged(final TableModelEvent e) {
            if (TableModelEvent.INSERT == e.getType()) {
                if ((0 == e.getFirstRow()) && (0 == getSelectedRow())) {
                    removeRowSelectionInterval(0, 0);
                }
            }
        }
    }
}
