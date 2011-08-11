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
/*
 * ChooseHotkeyDialog.java
 *
 * Created on 10.03.2009, 15:45:39
 */
package de.cismet.beanmill;

import org.apache.log4j.Logger;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class ChooseHotkeyDialog extends JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(
            ChooseHotkeyDialog.class);

    //~ Instance fields --------------------------------------------------------

    private int hotkey;
    private String hkSubstitute;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblHotKey;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ChooseHotkeyDialog.
     *
     * @param  parent  DOCUMENT ME!
     */
    public ChooseHotkeyDialog(final Dialog parent) {
        super(parent, null, true);
        init();
    }

    /**
     * Creates new form ChooseHotkeyDialog.
     *
     * @param  parent  DOCUMENT ME!
     */
    public ChooseHotkeyDialog(final Frame parent) {
        super(parent, true);
        init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("init");
        }
        initComponents();
        this.addKeyListener(new KeyListener() {

                private boolean pressed;
                private int pressedKey;

                @Override
                public void keyTyped(final KeyEvent e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("typed key: " + e.getKeyChar());
                    }
                    // keys that generate this event are inappropriate
                    // setHotkey(e, true);
                    pressed = false;
                    pressedKey = -1;
                }

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("pressed key: " + e.getKeyCode());
                    }
                    if (!pressed) {
                        pressedKey = e.getKeyCode();
                        pressed = true;
                    }
                }

                @Override
                public void keyReleased(final KeyEvent e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("released key: " + e.getKeyCode());
                    }
                    if (pressedKey == e.getKeyCode()) {
                        pressed = false;
                        setHotkey(e, false);
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e      DOCUMENT ME!
     * @param  typed  DOCUMENT ME!
     */
    private void setHotkey(final KeyEvent e, final boolean typed) {
        if (typed) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("key was typed: " + e);
            }
            hotkey = e.getKeyChar();
            hkSubstitute = String.valueOf(e.getKeyChar());
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("escape was used, will not be set as hotkey: " + e);
            }
            hotkey = -1;
            hkSubstitute = null;
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("special key was used: " + e);
            }
            hotkey = e.getKeyCode();
            hkSubstitute = KeyEvent.getKeyText(hotkey);
        }
        setVisible(false);
    }

    /**
     * Displays a modal dialog waiting for a new hotkey.
     *
     * @param   parent  DOCUMENT ME!
     *
     * @return  the system dependant representation of the hotkey
     */
    public static ChooseHotkeyDialog showHotkeyDialog(final Dialog parent) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("showHKDialog with parent " + parent);
        }
        final ChooseHotkeyDialog dialog = new ChooseHotkeyDialog(parent);
        dialog.displayDialog();
        return dialog;
    }

    /**
     * Displays a modal dialog waiting for a new hotkey.
     *
     * @param   parent  DOCUMENT ME!
     *
     * @return  the system dependant representation of the hotkey
     */
    public static ChooseHotkeyDialog showHotkeyDialog(final Frame parent) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("showHKDialog with parent " + parent);
        }
        final ChooseHotkeyDialog dialog = new ChooseHotkeyDialog(parent);
        dialog.displayDialog();
        return dialog;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getHotkey() {
        return hotkey;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getHKSubstitute() {
        return hkSubstitute;
    }

    /**
     * DOCUMENT ME!
     */
    private void displayDialog() {
        pack();
        // TODO: why do we have to set the correct location by ourselves
        final Point parentLoc = getParent().getLocation();
        final Dimension parentDim = getParent().getSize();
        setLocation(parentLoc.x + (parentDim.width / 2) - (getWidth() / 2),
            parentLoc.y
                    + (parentDim.height / 2)
                    - (getHeight() / 2));
        setVisible(true);
        final Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            while (hotkey == 0) {
                                Thread.sleep(100);
                            }
                        } catch (final InterruptedException ex) {
                            LOG.error("thread was interrupted while sleeping", ex);
                        }
                    }
                });
        t.start();
        try {
            t.join();
        } catch (final InterruptedException ex) {
            LOG.error("thread was interrupted while joining", ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblHotKey = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(200, 100));
        setModal(true);
        setResizable(false);
        setUndecorated(true);

        lblHotKey.setFont(new java.awt.Font("Lucida Grande", 0, 14));
        lblHotKey.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHotKey.setText(org.openide.util.NbBundle.getMessage(
                ChooseHotkeyDialog.class,
                "ChooseHotkeyDialog.lblHotKey.text")); // NOI18N
        lblHotKey.setMinimumSize(new java.awt.Dimension(200, 100));
        lblHotKey.setPreferredSize(new java.awt.Dimension(200, 100));
        getContentPane().add(lblHotKey, java.awt.BorderLayout.CENTER);

        pack();
    } // </editor-fold>//GEN-END:initComponents
}
