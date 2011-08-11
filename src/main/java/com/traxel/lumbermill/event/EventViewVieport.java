/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EventViewVieport.java
 *
 * Created on 22. Mai 2007, 09:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.traxel.lumbermill.event;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JViewport;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class EventViewVieport extends JViewport {

    //~ Instance fields --------------------------------------------------------

    private EventView ev;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EventViewVieport object.
     *
     * @param  ev  DOCUMENT ME!
     */
    public EventViewVieport(final EventView ev) {
        this.ev = ev;
        setView(ev);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void paintChildren(final Graphics g) {
        // Invoke the superclass behavior first
        // to paint the children.
        final Graphics2D g2d = (Graphics2D)g;
        final Composite alphaComp = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,
                0.15f);
        final Composite noAlphaComp = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,
                1f);
        g2d.setComposite(alphaComp);
        if ((ev != null) && (ev.getLogo() != null)) {
            // Paint the logo image on top of the
            // bottom right corner.
            g2d.drawImage(ev.getLogo(),
                getWidth()
                        - ev.getLogo().getWidth(null),
                getHeight()
                        - ev.getLogo().getHeight(null),
                this);
        }
        g2d.setComposite(noAlphaComp);
        super.paintChildren(g);
    }

    // Return the view component's background color
    // so that our UI will use that color to fill
    // the background.
    @Override
    public Color getBackground() {
        return (getView() == null) ? super.getBackground() : getView().getBackground();
    }
}
