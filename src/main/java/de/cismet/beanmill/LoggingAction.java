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
package de.cismet.beanmill;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class LoggingAction extends CallableSystemAction {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void performAction() {
        final TopComponent win = LoggingTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(LoggingAction.class, "CTL_LoggingAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected String iconResource() {
        return LoggingTopComponent.ICON_PATH;
    }
}
