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
// this doesn't belong in the model hierarchy, but it has
// to be here because javax.swing.table.TableColumnModel
// violates M/V separation.
package com.traxel.lumbermill.event;

import com.traxel.color.model.ScreenWheel;
import com.traxel.color.model.Wheel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SeverityView implements Icon {

    //~ Static fields/initializers ---------------------------------------------

    // -------------------------------------------
    // Class Initialization
    // ------------------------------------------
    private static final int SIZE = 16;
    private static final Wheel WHEEL = new ScreenWheel();

    //~ Instance fields --------------------------------------------------------

    ImageIcon classIcon = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/class.png"));
    ImageIcon inheritedIcon = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/inherited.png"));
    ImageIcon throwAwayIconMenu = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/delete.png"));
    ImageIcon throwAwayIcon = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/deleteSmall.png"));
    // ImageIcon throwAwayIcon = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/bullet_delete.png"));
    ImageIcon inheritMenuIcon = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/inheritMenu.png"));
    ImageIcon rootIcon = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/package_folder.png"));
    ImageIcon packageIcon = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/package.png"));
    // -------------------------------------------
    // Instance Initialization
    // -------------------------------------------
    private final Severity _actual;
    private final Severity _effective;
    private boolean root = false;
    private boolean leaf = false;
    private boolean menu = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SeverityView object.
     *
     * @param  actual     DOCUMENT ME!
     * @param  effective  DOCUMENT ME!
     * @param  root       DOCUMENT ME!
     * @param  leaf       DOCUMENT ME!
     * @param  menu       DOCUMENT ME!
     */
    public SeverityView(final Severity actual,
            final Severity effective,
            final boolean root,
            final boolean leaf,
            final boolean menu) {
        _actual = actual;
        _effective = effective;
        this.menu = menu;
        this.root = root;
        this.leaf = leaf;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * ------------------------------------------ Class API ------------------------------------------
     *
     * @param   severity  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Color getColor(final Severity severity) {
        if (Severity.INHERIT.equals(severity)) {
            return Color.GRAY;
        }
        if (Severity.ALL.equals(severity)) {
            return Color.WHITE;
        }
        if (Severity.DISABLED.equals(severity)) {
            return Color.BLACK;
        }
        final int angle;
        final Color color;
        final float ratio;
        final float adjustedLevel;

        adjustedLevel = (float)(severity.getLevel() - 1);
        ratio = adjustedLevel / 8f;
        angle = (int)(ratio * 270f) + 30;
        color = WHEEL.getBaseColor(angle);

        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Severity getActual() {
        return _actual;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Severity getEffective() {
        return _effective;
    }

    // -------------------------------------------
    // ImageIcon Implementation
    // -------------------------------------------
    @Override
    public int getIconWidth() {
        return SIZE + 10;
    }

    @Override
    public int getIconHeight() {
        return SIZE + 2;
    }

    @Override
    public void paintIcon(final Component parent, final Graphics g, final int x, final int y) {
        if (!menu) {
            if (root) {
                g.drawImage(rootIcon.getImage(), 0, 0, null);
            } else {
                if (leaf) {
                    g.drawImage(classIcon.getImage(), 0, 0, null);
                } else {
                    g.drawImage(packageIcon.getImage(), 0, 0, null);
                }
            }

            if (Severity.DISABLED_THROW_AWAY.equals(getEffective())) {
                g.drawImage(throwAwayIcon.getImage(), 12, 7, null);
            } else {
                g.drawImage(createNiceOval(9, getColor(getEffective())), 12, 7, null);
            }
            if (Severity.INHERIT.equals(getActual())) {
                g.drawImage(inheritedIcon.getImage(), 14, 2, null);
            }
        } else {
            ////////////////////////////////////
            //
            // MENU
            //
            ////////////////////////////////////
            final Color bg = parent.getBackground();
            final Color color = getColor(getEffective());

            if (Severity.INHERIT.equals(getActual())) {
                g.drawImage(inheritMenuIcon.getImage(), 1, 1, null);
            }
            if (Severity.DISABLED_THROW_AWAY.equals(getActual())) {
                g.drawImage(throwAwayIconMenu.getImage(), 1, 1, null);
            } else {
                g.drawImage(createNiceOval(SIZE, color), 1, 1, null);
//                g.setColor( Color.BLACK );
//                g.fillOval( x, y, getIconWidth(), getIconHeight() );
//                g.setColor( color );
//                g.fillOval( x + 1, y + 1,
//                        getIconWidth() - 2, getIconHeight() - 2 );
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   dimension  DOCUMENT ME!
     * @param   color      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BufferedImage createNiceOval(final int dimension, final Color color) {
        final int ICON_DIMENSION = dimension;
        final Color backgroundColor = color;
        final BufferedImage image = new BufferedImage(ICON_DIMENSION, ICON_DIMENSION,
                BufferedImage.TYPE_INT_ARGB);
        // set completely transparent
        for (int col = 0; col < ICON_DIMENSION; col++) {
            for (int row = 0; row < ICON_DIMENSION; row++) {
                image.setRGB(col, row, 0x0);
            }
        }
        final Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(backgroundColor);
        graphics.fillOval(0, 0, ICON_DIMENSION - 1, ICON_DIMENSION - 1);

        // create a whitish spot in the left-top corner of the icon
        final double id4 = ICON_DIMENSION / 4.0;
        final double spotX = id4;
        final double spotY = id4;
        for (int col = 0; col < ICON_DIMENSION; col++) {
            for (int row = 0; row < ICON_DIMENSION; row++) {
                // distance to spot
                final double dx = col - spotX;
                final double dy = row - spotY;
                double dist = Math.sqrt((dx * dx) + (dy * dy));

                // distance of 0.0 - comes 90% to Color.white
                // distance of ICON_DIMENSION - stays the same

                if (dist > ICON_DIMENSION) {
                    dist = ICON_DIMENSION;
                }

                final int currColor = image.getRGB(col, row);
                final int transp = (currColor >>> 24) & 0xFF;
                final int oldR = (currColor >>> 16) & 0xFF;
                final int oldG = (currColor >>> 8) & 0xFF;
                final int oldB = (currColor >>> 0) & 0xFF;

                final double coef = 0.9 - (0.9 * dist / ICON_DIMENSION);
                final int dr = 255 - oldR;
                final int dg = 255 - oldG;
                final int db = 255 - oldB;

                final int newR = (int)(oldR + (coef * dr));
                final int newG = (int)(oldG + (coef * dg));
                final int newB = (int)(oldB + (coef * db));

                final int newColor = (transp << 24) | (newR << 16) | (newG << 8) | newB;
                image.setRGB(col, row, newColor);
            }
        }
        // draw outline of the icon
        graphics.setColor(Color.black);
        graphics.drawOval(0, 0, ICON_DIMENSION - 1, ICON_DIMENSION - 1);

        // Buchstabe
// letter = Character.toUpperCase(letter);
// graphics.setFont(new Font("Arial", Font.BOLD, ICON_DIMENSION-5));
// FontRenderContext frc = graphics.getFontRenderContext();
// TextLayout mLayout = new TextLayout("" + letter, graphics.getFont(),
// frc);
//
// float x = (float) (-.5 + (ICON_DIMENSION - mLayout.getBounds()
// .getWidth()) / 2);
// float y = ICON_DIMENSION
// - (float) ((ICON_DIMENSION - mLayout.getBounds().getHeight()) / 2);

        // draw the letter
// graphics.drawString("" + letter, x, y);
        return image;
    }
}
