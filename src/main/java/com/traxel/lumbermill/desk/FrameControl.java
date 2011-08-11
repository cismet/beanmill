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
package com.traxel.lumbermill.desk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import java.beans.PropertyVetoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class FrameControl extends AbstractAction {

    //~ Instance fields --------------------------------------------------------

    // -------------------------------------------
    // Instance Definition
    // -------------------------------------------

    protected final MillDesktop desktop;

    //~ Constructors -----------------------------------------------------------

    /**
     * --------------------------------------------- Instance Initialization
     * ---------------------------------------------
     *
     * @param  desktop  DOCUMENT ME!
     * @param  name     DOCUMENT ME!
     */
    public FrameControl(final MillDesktop desktop, final String name) {
        super(name);
        this.desktop = desktop;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * --------------------------------------------- Implementation Interface
     * ---------------------------------------------
     */
    public abstract void layout();

    // ----------------------------------------------
    // AbstractAction Implementation
    // ---------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        synchronized (FrameControl.class) {
            layout();
        }
    }

    /**
     * -------------------------------------------- Inherited API --------------------------------------------
     *
     * @return  DOCUMENT ME!
     */
    protected List getActiveFrames() {
        final JInternalFrame[] allFrames;
        JInternalFrame frame;
        final List frames;

        allFrames = desktop.getAllFrames();
        if ((allFrames == null) || (allFrames.length == 0)) {
            return Collections.EMPTY_LIST;
        }
        frames = new ArrayList();
        for (int i = 0; i < allFrames.length; i++) {
            frame = allFrames[i];
            if (!frame.isClosed() && !frame.isIcon()) {
                frames.add(frame);
            }
        }
        return frames;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected List getAllFrames() {
        return Arrays.asList(desktop.getAllFrames());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   frame  DOCUMENT ME!
     * @param   size   DOCUMENT ME!
     * @param   x      DOCUMENT ME!
     * @param   y      DOCUMENT ME!
     *
     * @throws  PropertyVetoException  DOCUMENT ME!
     */
    protected void adjustFrame(final JInternalFrame frame, final Dimension size, final int x, final int y)
            throws PropertyVetoException {
        frame.setMaximum(false);
        frame.setSize(size);
        frame.toFront();
        frame.setSelected(true);
        frame.setLocation(x, y);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * --------------------------------------------- Class Definition ---------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    public abstract static class Tile extends FrameControl {

        //~ Static fields/initializers -----------------------------------------

        // Class Definition
        public static final int HORIZONTAL = 0;
        public static final int VERTICAL = 1;

        //~ Constructors -------------------------------------------------------

        /**
         * Instance Initialization.
         *
         * @param  desktop  DOCUMENT ME!
         * @param  name     DOCUMENT ME!
         */
        public Tile(final MillDesktop desktop, final String name) {
            super(desktop, name);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Interface.
         *
         * @return  DOCUMENT ME!
         */
        public abstract int getOrientation();
        // FrameControl Implementation
        @Override
        public final void layout() {
            final int ORIENTATION = getOrientation();
            JInternalFrame frame;
            final List frames;
            final Iterator it;
            final Dimension deskSize;
            final Dimension frameSize;
            int x;
            int y;
            int width;
            int height;
            final int deskWidth;
            final int deskHeight;

            frames = getActiveFrames();
            if (frames.size() == 0) {
                return;
            }
            deskSize = desktop.getSize();
            deskWidth = (int)deskSize.getWidth();
            deskHeight = (int)deskSize.getHeight();
            if (ORIENTATION == VERTICAL) {
                width = deskWidth / frames.size();
                height = deskHeight;
            } else {
                width = deskWidth;
                height = deskHeight / frames.size();
            }
            frameSize = new Dimension(width, height);
            x = 0;
            y = 0;
            it = frames.iterator();
            while (it.hasNext()) {
                frame = (JInternalFrame)it.next();
                try {
                    adjustFrame(frame, frameSize, x, y);
                    if (ORIENTATION == VERTICAL) {
                        x += width;
                    } else {
                        y += height;
                    }
                } catch (PropertyVetoException e) {
                    // Shouldn't ever happen.
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class TileHorizontal extends Tile {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new TileHorizontal object.
         *
         * @param  desktop  DOCUMENT ME!
         */
        public TileHorizontal(final MillDesktop desktop) {
            super(desktop, "Tile Horizontal");
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public int getOrientation() {
            return HORIZONTAL;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class TileVertical extends Tile {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new TileVertical object.
         *
         * @param  desktop  DOCUMENT ME!
         */
        public TileVertical(final MillDesktop desktop) {
            super(desktop, "Tile Vertical");
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public int getOrientation() {
            return VERTICAL;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class Cascade extends FrameControl {

        //~ Static fields/initializers -----------------------------------------

        // Class Definition
        private static final int INCREMENT = 30;

        //~ Constructors -------------------------------------------------------

        /**
         * Class Initialization.
         *
         * @param  desktop  DOCUMENT ME!
         */
        public Cascade(final MillDesktop desktop) {
            super(desktop, "Cascade");
        }

        //~ Methods ------------------------------------------------------------

        // FrameControl Implementation
        @Override
        public void layout() {
            JInternalFrame frame;
            final List frames;
            final Iterator it;
            final Dimension deskSize;
            final Dimension frameSize;
            int x;
            int y;
            final int width;
            final int height;
            final int totalShrink;

            frames = getActiveFrames();
            if (frames.size() == 0) {
                return;
            }
            deskSize = desktop.getSize();
            totalShrink = INCREMENT * frames.size();
            width = (int)(deskSize.getWidth() - totalShrink);
            height = (int)(deskSize.getHeight() - totalShrink);
            frameSize = new Dimension(width, height);
            x = 0;
            y = 0;
            it = frames.iterator();
            while (it.hasNext()) {
                frame = (JInternalFrame)it.next();
                try {
                    adjustFrame(frame, frameSize, x, y);
                    x += INCREMENT;
                    y += INCREMENT;
                } catch (PropertyVetoException e) {
                    // Shouldn't ever happen
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class MinimizeAll extends FrameControl {

        //~ Constructors -------------------------------------------------------

        /**
         * Instance Initialization.
         *
         * @param  desktop  DOCUMENT ME!
         */
        public MinimizeAll(final MillDesktop desktop) {
            super(desktop, "Minimize All");
        }

        //~ Methods ------------------------------------------------------------

        // FrameControl Implementation
        @Override
        public void layout() {
            JInternalFrame frame;
            final List frames;
            final Iterator it;

            frames = getActiveFrames();
            if (frames.size() == 0) {
                return;
            }
            it = frames.iterator();
            while (it.hasNext()) {
                frame = (JInternalFrame)it.next();
                try {
                    frame.setIcon(true);
                } catch (PropertyVetoException e) {
                    // Shouldn't ever happen
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class RestoreAll extends FrameControl {

        //~ Constructors -------------------------------------------------------

        /**
         * Instance Initialization.
         *
         * @param  desktop  DOCUMENT ME!
         */
        public RestoreAll(final MillDesktop desktop) {
            super(desktop, "Restore All");
        }

        //~ Methods ------------------------------------------------------------

        // FrameControl Implementation
        @Override
        public void layout() {
            JInternalFrame frame;
            final List frames;
            final Iterator it;

            frames = getAllFrames();
            if (frames.size() == 0) {
                return;
            }
            it = frames.iterator();
            while (it.hasNext()) {
                frame = (JInternalFrame)it.next();
                try {
                    frame.setIcon(false);
                    frame.setMaximum(false);
                } catch (PropertyVetoException e) {
                    // Shouldn't ever happen
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class MaximizeFrame extends FrameControl {

        //~ Instance fields ----------------------------------------------------

        // Instance Definition
        private final JInternalFrame frame;

        //~ Constructors -------------------------------------------------------

        /**
         * Instance Initialization.
         *
         * @param  desktop  DOCUMENT ME!
         * @param  frame    DOCUMENT ME!
         */
        public MaximizeFrame(final MillDesktop desktop, final JInternalFrame frame) {
            super(desktop, frame.getTitle());
            this.frame = frame;
        }

        //~ Methods ------------------------------------------------------------

        // FrameControl Implementation
        @Override
        public void layout() {
            try {
                frame.setMaximum(true);
                frame.toFront();
                frame.setSelected(true);
            } catch (PropertyVetoException e) {
                // shouldn't ever happen
                e.printStackTrace();
            }
        }
    }
}
