/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package com.traxel.lumbermill.event;

import com.traxel.color.model.Colors;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class SeverityCellView extends DefaultTableCellRenderer {

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getTableCellRendererComponent(final JTable table,
            final Object value,
            final boolean isSelected,
            final boolean hasFocus,
            final int row,
            final int column) {
        final Severity severity;
        Color color;
        final int angle;
        severity = (Severity)value;
        color = SeverityView.getColor(severity);
        color = Colors.blend(2, color, 1, Color.white);
        setBackground(color);
        setFont(table.getFont());
        setForeground(Color.BLACK);
        setValue(" " + severity.toString());

        return this;
    }
}
