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

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class BasicEvent extends AbstractEvent {

    //~ Instance fields --------------------------------------------------------

    private Severity sev;
    private String src;
    private String message;
    private Throwable throwable;
    private long ts;
    private String location;
    private String ndc;
    private String stackTrace;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BasicEvent object.
     */
    public BasicEvent() {
    }

    /**
     * Creates a new BasicEvent object.
     *
     * @param  e  DOCUMENT ME!
     */
    public BasicEvent(final Event e) {
        sev = e.getSeverity();
        src = e.getSource();
        message = e.getMessage();
        throwable = e.getThrown();
        ts = e.getTimestamp();
        location = e.getLocation();
        ndc = e.getNDC();
        stackTrace = e.getStackTrace();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  stackTrace  DOCUMENT ME!
     */
    public void setStackTrace(final String stackTrace) {
        this.stackTrace = stackTrace;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  location  DOCUMENT ME!
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  message  DOCUMENT ME!
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ndc  DOCUMENT ME!
     */
    public void setNDC(final String ndc) {
        this.ndc = ndc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sev  DOCUMENT ME!
     */
    public void setSeverity(final Severity sev) {
        this.sev = sev;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  src  DOCUMENT ME!
     */
    public void setSource(final String src) {
        this.src = src;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  throwable  DOCUMENT ME!
     */
    public void setThrowable(final Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ts  DOCUMENT ME!
     */
    public void setTimestamp(final long ts) {
        this.ts = ts;
    }

    @Override
    public Severity getSeverity() {
        return sev;
    }

    @Override
    public String getSource() {
        return src;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getThrown() {
        return throwable;
    }

    @Override
    public long getTimestamp() {
        return ts;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getNDC() {
        return ndc;
    }

    @Override
    public String getStackTrace() {
        return stackTrace;
    }
}
