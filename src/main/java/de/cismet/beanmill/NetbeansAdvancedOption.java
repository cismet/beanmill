/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.beanmill;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;

import org.openide.util.NbBundle;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class NetbeansAdvancedOption extends AdvancedOption {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(NetbeansAdvancedOption.class,
                "AdvancedOption_DisplayName");
    }

    @Override
    public String getTooltip() {
        return NbBundle.getMessage(NetbeansAdvancedOption.class,
                "AdvancedOption_Tooltip");
    }

    @Override
    public OptionsPanelController create() {
        return new NetbeansOptionsPanelController();
    }
}
