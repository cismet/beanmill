/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.beanmill;

import com.traxel.lumbermill.event.Event;
import com.traxel.lumbermill.event.Table;

import org.apache.log4j.Logger;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.java.queries.SourceForBinaryQuery;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;

import org.openide.ErrorManager;
import org.openide.cookies.EditCookie;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.text.NbDocument;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.WeakListeners;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.CharArrayWriter;
import java.io.Serializable;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JEditorPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;

/**
 * Top component which displays something.
 *
 * @version  $Revision$, $Date$
 */
final class LoggingTopComponent extends TopComponent {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(LoggingTopComponent.class);

    static final String ICON_PATH = "de/cismet/beanmill/res/log.png"; // NOI18N

    private static final String PREFERRED_ID = "LoggingTopComponent"; // NOI18N

    private static LoggingTopComponent instance;

    //~ Instance fields --------------------------------------------------------

    BeanMillPane bmp = null;
    private MySelectionListener mySelectionListener = new MySelectionListener();
    private String editedJavaSourceFullPath = null;
    private int editedSelectionFrom = -1;
    private int editedSelectionTo = -1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LoggingTopComponent object.
     */
    private LoggingTopComponent() {
        initComponents();

        TopComponent.getRegistry().addPropertyChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    try {
                        if (evt.getPropertyName().equals("activated")) {
                            final Node[] nodes = TopComponent.getRegistry().getActivatedNodes();

                            if ((nodes == null) || (nodes.length == 0)) {
                                // no activated nodes
                                return;
                            }

                            final EditorCookie ec = nodes[0].getCookie(EditorCookie.class);
                            JEditorPane editor = null;
                            final FileObject fo = NbEditorUtilities.getFileObject(ec.getDocument());
                            final ClassPath sourceCP = ClassPath.getClassPath(fo, ClassPath.SOURCE);
                            final String fullName = sourceCP.getResourceName(fo, '.', false);
                            if (ec != null) {
                                final JEditorPane[] panes = ec.getOpenedPanes();

                                if (panes != null) {
                                    final TopComponent activetc = TopComponent.getRegistry().getActivated();
                                    for (int i = 0; i < panes.length; i++) {
                                        if (activetc.isAncestorOf(panes[i])) {
                                            editor = panes[i];
                                        }
                                    }
                                }
                            }
                            if (editor != null) {
                                mySelectionListener.setEditor(editor);
                                mySelectionListener.setFullName(fullName);
                                fireSelectionChange(editor, fullName);
                                editor.addMouseMotionListener(
                                    WeakListeners.create(MouseMotionListener.class, mySelectionListener, editor));
                                editor.addCaretListener(
                                    WeakListeners.create(CaretListener.class, mySelectionListener, editor));
                            }
                        }
                    } catch (final Exception ex) {
                        LOG.warn("cannot process property change: " + evt, ex); // NOI18N
                    }
                }
            });

        setName(NbBundle.getMessage(LoggingTopComponent.class, "CTL_LoggingTopComponent"));         // NOI18N
        setToolTipText(NbBundle.getMessage(LoggingTopComponent.class, "HINT_LoggingTopComponent")); // NOI18N
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        try {
            final int port = NbPreferences.forModule(LoggingTopComponent.class)
                        .getInt(NetbeansPanel.PROP_LISTENER_PORT, 4445);
            bmp = new BeanMillPane(port);
            bmp.setActiveSelectionCheckBoxEnabled(true);
            bmp.getEventTable().addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            final int row = bmp.getEventTable().getSelectedRow();
                            final Event logEvent = ((Table)bmp.getEventTable().getModel()).getEvent(row);

                            if (logEvent != null) {
                                final String location = logEvent.getLocation();
                                final String nameSplit = location.split("\\(")[0];                              // NOI18N
                                final String fullname = nameSplit.substring(0, nameSplit.lastIndexOf('.'));
                                final int linenumber = new Integer(location.split("java:")[1].split("\\)")[0]); // NOI18N

                                openJava(fullname, linenumber);
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(final MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(final MouseEvent e) {
                    }

                    @Override
                    public void mousePressed(final MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(final MouseEvent e) {
                    }
                });

            bmp.getDetailPanel().addHyperlinkListener(new HyperlinkListener() {

                    @Override
                    public void hyperlinkUpdate(final HyperlinkEvent e) {
                        if (e.getEventType().equals(EventType.ACTIVATED)) {
                            try {
                                final String line = e.getDescription();
                                final String[] ar = line.split(":");
                                final String path = ar[0];
                                final String linenumber = ar[1];
                                final int lNo = new Integer(linenumber);
                                openJava(path, lNo);
                            } catch (Exception ex) {
                                ErrorManager.getDefault().notify(ex);
                            }
                        }
                    }
                });

            bmp.getLogActiveStateView().getClearButton().addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        bmp.getDetailPanel().setText("");
                    }
                });

            this.add(bmp, BorderLayout.CENTER);
        } catch (final Exception e) {
            LOG.error("cannot initialise logging top component", e); // NOI18N
            ErrorManager.getDefault().notify(e);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BeanMillPane getMillPane() {
        return bmp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  editor    DOCUMENT ME!
     * @param  fullName  DOCUMENT ME!
     */
    private void fireSelectionChange(final JEditorPane editor, final String fullName) {
        final int fromPos = editor.getSelectionStart();
        final int toPos = editor.getSelectionEnd();
        final StyledDocument doc = (StyledDocument)editor.getDocument();
        int from = org.openide.text.NbDocument.findLineNumber(doc, fromPos) + 1;
        int to = org.openide.text.NbDocument.findLineNumber(doc, toPos) + 1;

        if (!(fullName.equals(editedJavaSourceFullPath) && (editedSelectionFrom == from) && (editedSelectionTo
                            == to))) {
            if (fromPos == toPos) {
                from = -1;
                to = -1;
            }
            bmp.setSelection(fullName, from, to);
            editedJavaSourceFullPath = fullName;
            editedSelectionFrom = from;
            editedSelectionTo = to;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fullName    DOCUMENT ME!
     * @param  lineNumber  DOCUMENT ME!
     */
    public void openJava(final String fullName, final int lineNumber) {
        final String[] ar = fullName.split("\\."); // NOI18N
        final String className = ar[ar.length - 1];
        final FileObject fo = findResource(fullName, 0);

        if (fo != null) {
            openSourceFO(fo);
            final EditorCookie ec = TopComponent.getRegistry().getActivatedNodes()[0].getCookie(EditorCookie.class);
            JEditorPane editor = null;

            if (ec != null) {
                final JEditorPane[] panes = ec.getOpenedPanes();
                if (panes != null) {
                    final TopComponent activetc = TopComponent.getRegistry().getActivated();
                    for (int i = 0; i < panes.length; i++) {
                        if (activetc.isAncestorOf(panes[i])) {
                            editor = panes[i];
                        }
                    }
                }
            }
            if (editor != null) {
                try {
                    gotoLine(editor, lineNumber);
                } catch (Exception ex) {
                    LOG.warn("cannot goto line: " + lineNumber, ex); // NOI18N
                    gotoLine(editor, 1);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  txt   DOCUMENT ME!
     * @param  line  DOCUMENT ME!
     */
    private static void gotoLine(final JTextComponent txt, final int line) {
        final StyledDocument doc = (StyledDocument)txt.getDocument();
        txt.setCaretPosition(0);
        int caretPos = 0;

        for (; (NbDocument.findLineNumber(doc, caretPos) + 1) < line; ++caretPos) {
        }

        txt.setCaretPosition(caretPos);
    }
    /**
     * TODO: why order not final
     *
     * @param   fullName  DOCUMENT ME!
     * @param   order     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private FileObject findResource(final String fullName, int order) {
        final HashMap alreadyTested = new HashMap();
        final String name = fullName.replace('.', '/') + ".java"; // NOI18N
        // first check the SOURCE classpath
        final Set set = GlobalPathRegistry.getDefault().getPaths(ClassPath.SOURCE);
        Iterator it = set.iterator();
        while (it.hasNext()) {
            final ClassPath cp = (ClassPath)it.next();
            if (alreadyTested.get(cp) != null) {
                continue;
            }
            alreadyTested.put(cp, cp);

            final FileObject fo = cp.findResource(name);
            if (fo != null) {
                if (order == 0) {
                    return fo;
                } else {
                    // continue searching
                    order--;
                }
            } else {
                // nothing found
            }
        }
        // now check the COMPILE and BOOT classpaths
        final ArrayList<ClassPath> list = new ArrayList<ClassPath>(GlobalPathRegistry.getDefault().getPaths(
                    ClassPath.COMPILE));
        list.addAll(GlobalPathRegistry.getDefault().getPaths(ClassPath.BOOT));
        it = list.iterator();
        while (it.hasNext()) {
            final ClassPath cp = (ClassPath)it.next();

            if (alreadyTested.get(cp) != null) {
                continue;
            }

            alreadyTested.put(cp, cp);
            final Iterator it2 = cp.entries().iterator();
            while (it2.hasNext()) {
                final ClassPath.Entry entry = (ClassPath.Entry)it2.next();
                final FileObject[] sroots = SourceForBinaryQuery.findSourceRoots(entry.getURL()).getRoots();
                final List ll = Arrays.asList(sroots);

                if (alreadyTested.get(ll) != null) {
                    continue;
                }

                alreadyTested.put(ll, ll);
                if (sroots.length > 0) {
                    final ClassPath sources = ClassPathSupport.createClassPath(sroots);
                    final FileObject fo = sources.findResource(name);
                    if (fo != null) {
                        if (order == 0) {
                            return fo;
                        } else {
                            // continue searching
                            order--;
                        }
                    }
                }
            }
            final FileObject fo = cp.findResource(name);
            if (fo != null) {
                if (order == 0) {
                    return fo;
                } else {
                    // continue searching
                    order--;
                }
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fo  DOCUMENT ME!
     */
    private void openSourceFO(final FileObject fo) {
        DataObject dob;
        try {
            dob = DataObject.find(fo);
        } catch (final DataObjectNotFoundException e) {
            ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
            dob = null;
        }
        if (dob != null) {
            final EditCookie ec = dob.getCookie(EditCookie.class);
            if (ec != null) {
                ec.edit();
            } else {
                final OpenCookie oc = dob.getCookie(OpenCookie.class);
                if (oc != null) {
                    oc.open();
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    } // </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only, i.e. deserialization routines;
     * otherwise you could get a non-deserialized instance. To obtain the singleton instance, use {@link findInstance}.
     *
     * @return  DOCUMENT ME!
     */
    public static synchronized LoggingTopComponent getDefault() {
        if (instance == null) {
            instance = new LoggingTopComponent();
        }

        return instance;
    }

    /**
     * Obtain the LoggingTopComponent instance. Never call {@link #getDefault} directly!
     *
     * @return  DOCUMENT ME!
     */
    public static synchronized LoggingTopComponent findInstance() {
        final TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault()
                    .log(
                        ErrorManager.WARNING,
                        "Cannot find Logging component. It will not be located properly in the window system."); // NOI18N

            return getDefault();
        }

        if (win instanceof LoggingTopComponent) {
            return (LoggingTopComponent)win;
        }

        ErrorManager.getDefault()
                .log(
                    ErrorManager.WARNING,
                    "There seem to be multiple components with the '" // NOI18N
                    + PREFERRED_ID
                    + "' ID. That is a potential source of errors and unexpected behavior."); // NOI18N

        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    /**
     * replaces this in object stream.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Object writeReplace() {
        final Element e = bmp.getAllFilters();

        final XMLOutputter xout = new XMLOutputter();
        final CharArrayWriter charw = new CharArrayWriter();
        String s = "";                                   // NOI18N
        try {
            xout.output(e, charw);
            s = new String(charw.toCharArray());
        } catch (final Exception ex) {
            LOG.error("cannot write top component", ex); // NOI18N
        }

        return new ResolvableHelper(s, bmp.getSplitPaneDividerLocation());
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MySelectionListener implements MouseMotionListener, CaretListener {

        //~ Instance fields ----------------------------------------------------

        private JEditorPane editor = null;
        private String fullName = ""; // NOI18N

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  fullName  DOCUMENT ME!
         */
        public void setFullName(final String fullName) {
            this.fullName = fullName;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  editor  DOCUMENT ME!
         */
        public void setEditor(final JEditorPane editor) {
            this.editor = editor;
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            fireSelectionChange(editor, fullName);
        }

        @Override
        public void mouseMoved(final MouseEvent e) {
            fireSelectionChange(editor, fullName);
        }

        @Override
        public void caretUpdate(final CaretEvent e) {
            fireSelectionChange(editor, fullName);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static final class ResolvableHelper implements Serializable {

        //~ Static fields/initializers -----------------------------------------

        private static final long serialVersionUID = 1L;

        //~ Instance fields ----------------------------------------------------

        private final String storeString;
        private final int dividerLocation;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ResolvableHelper object.
         *
         * @param  storeString      DOCUMENT ME!
         * @param  dividerLocation  DOCUMENT ME!
         */
        public ResolvableHelper(final String storeString, final int dividerLocation) {
            this.storeString = storeString;
            if (dividerLocation == 0) {
                this.dividerLocation = 30;
            } else {
                this.dividerLocation = dividerLocation;
            }
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Object readResolve() {
            final LoggingTopComponent ltc = LoggingTopComponent.getDefault();
            try {
                final SAXBuilder sb = new SAXBuilder();
                final org.jdom.Document result = sb.build(new StringReader(storeString));
                ltc.bmp.setAllFilters(result.getRootElement());
            } catch (final Exception ex) {
                LOG.error("cannot read top component", ex); // NOI18N
            }

            ltc.bmp.setSplitPaneDividerLocation(dividerLocation);

            return ltc;
        }
    }
}
