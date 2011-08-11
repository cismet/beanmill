/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.traxel.lumbermill.event;

import org.jdom.CDATA;
import org.jdom.Element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class XMLEvent extends BasicEvent {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new XMLEvent object.
     *
     * @param  e  DOCUMENT ME!
     */
    public XMLEvent(final Event e) {
        super(e);
    }
    /**
     * private Severity sev; private String src; private String message; private Throwable throwable; private long ts;
     * private String location; private String ndc;
     *
     * @param   xmlElement  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public XMLEvent(final Element xmlElement) throws Exception {
        super();
        try {
            setSeverity(Severity.getSeverityByString(xmlElement.getAttributeValue("severity")));
        } catch (Exception exception) {
        }
        try {
            setSource(xmlElement.getAttributeValue("source"));
        } catch (Exception exception) {
        }
        try {
            setMessage(xmlElement.getAttributeValue("message"));
        } catch (Exception exception) {
        }
        try {
            setTimestamp(new Long(xmlElement.getAttributeValue("timestamp")).longValue());
        } catch (NumberFormatException numberFormatException) {
        }
        try {
            setLocation(xmlElement.getAttributeValue("location"));
        } catch (Exception exception) {
        }
        try {
            setNDC(xmlElement.getAttributeValue("ndc"));
        } catch (Exception exception) {
        }

        try {
            final List l = xmlElement.getChild("stacktrace").getContent();

            setStackTrace(((CDATA)(l.get(0))).getText());
        } catch (Exception exception) {
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public Element getXMLElement() throws Exception {
        final Element e = new Element("LogEvent");

        if (getSeverity() != null) {
            e.setAttribute("severity", getSeverity().toString());
        }
        if (getSource() != null) {
            e.setAttribute("source", getSource());
        }
        if (getMessage() != null) {
            e.setAttribute("message", getMessage());
        }
        e.setAttribute("timestamp", new Long(getTimestamp()).toString());
        if (getLocation() != null) {
            e.setAttribute("location", getLocation());
        }
        if (getNDC() != null) {
            e.setAttribute("ndc", getNDC());
        }
        if (getStackTrace() != null) {
            final CDATA cd = new CDATA(getStackTrace());
            final Element stack = new Element("stacktrace");

            stack.addContent(cd);
            e.addContent(stack);
        }
        return e;
    }
}
