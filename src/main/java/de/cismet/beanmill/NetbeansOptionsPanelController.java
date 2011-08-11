/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.beanmill;

import org.netbeans.spi.options.OptionsPanelController;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
final class NetbeansOptionsPanelController extends OptionsPanelController {

    //~ Instance fields --------------------------------------------------------

    private NetbeansPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    //~ Methods ----------------------------------------------------------------

    @Override
    public void update() {
        getPanel().load();
        changed = false;
    }

    @Override
    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    @Override
    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    @Override
    public boolean isValid() {
        return getPanel().valid();
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    @Override
    public JComponent getComponent(final Lookup masterLookup) {
        return getPanel();
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private NetbeansPanel getPanel() {
        if (panel == null) {
            panel = new NetbeansPanel(this);
        }
        return panel;
    }

    /**
     * DOCUMENT ME!
     */
    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
