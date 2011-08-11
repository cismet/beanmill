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

import org.apache.log4j.PropertyConfigurator;

import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class Init extends ModuleInstall {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void restored() {
        try {
            final InputStream is = this.getClass().getResourceAsStream("log4j.properties"); // NOI18N
            final Properties prop = new Properties();
            prop.load(is);
            PropertyConfigurator.configure(prop);
        } catch (final IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        super.restored();
    }
}
