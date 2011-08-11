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

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class EventView extends JEditorPane {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(
            EventView.class);

    private static final String RES_PREFIX = "/de/cismet/beanmill/res/big/";

    //~ Instance fields --------------------------------------------------------

    private final ImageIcon allIcon;
    private final ImageIcon finestIcon;
    private final ImageIcon debugIcon;
    private final ImageIcon fineIcon;
    private final ImageIcon configIcon;
    private final ImageIcon infoIcon;
    private final ImageIcon warnIcon;
    private final ImageIcon errorIcon;
    private final ImageIcon fatalIcon;
    private final HTMLDocument htmlDoc;
    private final HTMLEditorKit htmlKit;

    private Image current = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EventView object.
     */
    public EventView() {
        allIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "all.png"));
        finestIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "0-finest.png"));
        debugIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "1-debug.png"));
        fineIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "2-fine.png"));
        configIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "3-config.png"));
        infoIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "4-info.png"));
        warnIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "5-warn.png"));
        errorIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "6-error.png"));
        fatalIcon = new ImageIcon(getClass().getResource(RES_PREFIX
                            + "7-fatal.png"));
        htmlDoc = new HTMLDocument();
        htmlKit = new HTMLEditorKit();
        setDocument(htmlDoc);
        setEditorKit(htmlKit);
        setContentType("text/html");
        setEditable(false);
        setOpaque(false);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    public void setEvent(final Event event) {
        if (event != null) {
            final String html = event.getHtml();
            if (LOG.isDebugEnabled()) {
                LOG.debug("event html: " + html);
            }
            System.out.println("event html: " + html);
            setText(html);
        } else {
            setText("");
        }
        final Severity eventSeverity = event.getSeverity();
        if (eventSeverity.equals(Severity.ALL)) {
            current = allIcon.getImage();
        } else if (eventSeverity.equals(Severity.LEVEL_1)) {
            current = finestIcon.getImage();
        } else if (eventSeverity.equals(Severity.LEVEL_2)) {
            current = debugIcon.getImage();
        } else if (eventSeverity.equals(Severity.LEVEL_3)) {
            current = fineIcon.getImage();
        } else if (eventSeverity.equals(Severity.LEVEL_4)) {
            current = configIcon.getImage();
        } else if (eventSeverity.equals(Severity.LEVEL_5)) {
            current = infoIcon.getImage();
        } else if (eventSeverity.equals(Severity.LEVEL_6)) {
            current = warnIcon.getImage();
        } else if (eventSeverity.equals(Severity.LEVEL_7)) {
            current = errorIcon.getImage();
        } else if (eventSeverity.equals(Severity.LEVEL_8)) {
            current = fatalIcon.getImage();
        }
        if (event instanceof XMLEvent) {
            EventView.this.setBackground(new Color(236, 233, 216));
        } else {
            EventView.this.setBackground(Color.white);
        }
        setCaretPosition(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Image getLogo() {
        return current;
    }
}
