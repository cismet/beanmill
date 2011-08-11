/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * BeanMillPane.java
 *
 * Created on 22. Mai 2007, 11:27
 */
package de.cismet.beanmill;

import com.traxel.lumbermill.ServerMill;
import com.traxel.lumbermill.event.Event;
import com.traxel.lumbermill.event.EventsPanel;
import com.traxel.lumbermill.event.EventsPanelControl;
import com.traxel.lumbermill.event.Table;
import com.traxel.lumbermill.event.TableControl;
import com.traxel.lumbermill.event.XMLEvent;
import com.traxel.lumbermill.filter.Filter;
import com.traxel.lumbermill.filter.FilterSet;
import com.traxel.lumbermill.filter.FilterSetControl;
import com.traxel.lumbermill.filter.FilterSetView;
import com.traxel.lumbermill.filter.FilterView;
import com.traxel.lumbermill.filter.Regex;
import com.traxel.lumbermill.filter.Tree;
import com.traxel.lumbermill.log.Log;
import com.traxel.lumbermill.log.LogActiveStateView;
import com.traxel.lumbermill.log.LogControl;

import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import de.cismet.beanmill.filter.ActiveSelectionFilter;
import de.cismet.beanmill.filter.ListView;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public final class BeanMillPane extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(BeanMillPane.class);
    private static BeanMillPane instance = null;

    //~ Instance fields --------------------------------------------------------

    private final JTextField txtRegExp;
    private final JCheckBox chkSelectionOnly;
    private final JButton cmdExport;
    private final JButton cmdImport;
    private final Regex regexFilter;
    private final ActiveSelectionFilter activeSelection;
    private final JFileChooser fc;
    private final EventsPanelControl eventsPanelControl;
    private final Border border;
    private final ImageIcon addTab;
    private final ImageIcon removeTab;
    private final ImageIcon follow;
    private final JLabel lblfollow;
    // keep reference to the mill
    private final ServerMill mill;
    private boolean deleteFilteredEvents = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSplitPane splMain;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form BeanMillPane.
     *
     * @param   port  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public BeanMillPane(final int port) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("creating new BeanMillPane using port: " + port);
        }
        // server init
        if (LOG.isDebugEnabled()) {
            LOG.debug("creating new ServerMill with log: " + getLog());
        }

        initComponents();

        txtRegExp = new JTextField("");
        chkSelectionOnly = new JCheckBox("follow active selection");
        cmdExport = new JButton("");
        cmdImport = new JButton("");
        regexFilter = new Regex();
        activeSelection = new ActiveSelectionFilter();
        fc = new JFileChooser();
        eventsPanelControl = new EventsPanelControl();
        border = new EmptyBorder(3, 3, 3, 3);
        addTab = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/tabNew.png"));
        removeTab = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/tabEx.png"));
        follow = new ImageIcon(getClass().getResource("/de/cismet/beanmill/res/selection.png"));
        lblfollow = new JLabel(follow);
        instance = this;

        mill = new ServerMill(port, getLog());
        chkSelectionOnly.setEnabled(false);
        getFilterSetControl().getFilterSet().add(regexFilter);
        getFilterSetControl().getFilterSet().add(activeSelection);
        cmdExport.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final FileFilter ff = new FileFilter() {

                            @Override
                            public boolean accept(final File f) {
                                return f.getName().toLowerCase().endsWith(".log4j"); // NOI18N
                            }

                            @Override
                            public String getDescription() {
                                return "log4j";
                            }
                        };
                    fc.setFileFilter(ff);
                    fc.setMultiSelectionEnabled(false);

                    final int state = fc.showSaveDialog(BeanMillPane.this);
                    if (JFileChooser.APPROVE_OPTION == state) {
                        final File file = fc.getSelectedFile();
                        String name = file.getAbsolutePath();
                        name = name.toLowerCase();
                        if (name.endsWith(".log4j")) { // NOI18N
                            saveEvents(name);
                        } else {
                            saveEvents(name + ".log4j");
                        }
                    }
                }
            });
        cmdImport.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final FileFilter ff = new FileFilter() {

                            @Override
                            public boolean accept(final File f) {
                                return f.getName().toLowerCase().endsWith(".log4j");
                            }

                            @Override
                            public String getDescription() {
                                return "log4j";
                            }
                        };
                    fc.setFileFilter(ff);
                    fc.setMultiSelectionEnabled(false);
                    final int state = fc.showOpenDialog(BeanMillPane.this);
                    if (state == JFileChooser.APPROVE_OPTION) {
                        final File file = fc.getSelectedFile();
                        // log.debug("file:" + file);
                        String name = file.getAbsolutePath();
                        name = name.toLowerCase();
                        if (name.endsWith(".log4j")) {
                            loadEvents(name);
                        } else {
                            loadEvents(name + ".log4j");
                        }
                    }
                }
            });
        getLogActiveStateView().setBorder(border);
        getFilterSetView().setBorder(border);
        getEventsPanel().setBorder(border);
        splMain.setBorder(border);
        add(getLogActiveStateView(), BorderLayout.NORTH);
        splMain.setLeftComponent(getFilterSetView());
        splMain.setRightComponent(getEventsPanel());
        getToolBar().add(cmdImport, 0);
        getToolBar().add(cmdExport, 1);
        final JLabel space = new JLabel();
        space.setPreferredSize(new Dimension(10, 1));
        getToolBar().add(space, 2);
        cmdImport.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/de/cismet/beanmill/res/import.png")));
        cmdImport.setToolTipText("Import Events");
        cmdExport.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/de/cismet/beanmill/res/export.png")));
        cmdExport.setToolTipText("Export Events");
        cmdExport.setOpaque(false);
        cmdImport.setOpaque(false);

        final JButton cmdAddTab = new JButton(addTab);
        final JButton cmdRemoveTab = new JButton(removeTab);
        getToolBar().add(cmdAddTab);
        getToolBar().add(cmdRemoveTab);
        cmdAddTab.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    getFilterSetControl().addTree();
                    cmdRemoveTab.setEnabled(true);
                }
            });
        cmdRemoveTab.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final FilterSetControl control = getFilterSetControl();
                    final Filter f = control.getActiveComponent().getFilter();
                    final FilterSetView view = control.getFilterSetView();
                    view.removeTabAt(view.getSelectedIndex());
                    control.getFilterSet().remove(f);
                    if (control.getActiveComponent() == null) {
                        cmdRemoveTab.setEnabled(false);
                    }
                }
            });

        final JCheckBox chkDeleteFilteredEvents = new JCheckBox("delete filtered Events");
        chkDeleteFilteredEvents.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    setDeleteFilteredEvents(chkDeleteFilteredEvents.isSelected());
                    getFilterSetControl().getFilterSetView().fireStateChanged();
                }
            });
        getToolBar().add(chkDeleteFilteredEvents);

        final JLabel space2 = new JLabel();
        space2.setPreferredSize(new Dimension(10, 1));
        getToolBar().add(space2, 7);
        chkSelectionOnly.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (chkSelectionOnly.isSelected()
                                && !getFilterSetControl().getFilterSet().getFilters().contains(activeSelection)) {
                        getFilterSetControl().getFilterSet().add(activeSelection);
                    }
                    activeSelection.setActive(chkSelectionOnly.isSelected());
                }
            });

        final JPanel panRightToolbarWhole = new JPanel();
        final JPanel panRightToolbar = new JPanel();
        final JPanel panEmpty = new JPanel();
        final JLabel lblregExp = new JLabel("RegExp ");
        panRightToolbar.setLayout(new FlowLayout());
        lblregExp.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/de/cismet/beanmill/res/regexp.png")));
        lblregExp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtRegExp.setMinimumSize(new Dimension(100, txtRegExp.getMinimumSize().height));
        txtRegExp.setPreferredSize(new Dimension(100, txtRegExp.getPreferredSize().height));
        panRightToolbarWhole.setLayout(new BorderLayout());
        panRightToolbarWhole.add(panEmpty, BorderLayout.CENTER);
        panRightToolbarWhole.add(panRightToolbar, BorderLayout.EAST);
        panRightToolbar.add(lblfollow);
        panRightToolbar.add(chkSelectionOnly);
        panRightToolbar.add(lblregExp);
        panRightToolbar.add(txtRegExp);
        getToolBar().add(panRightToolbarWhole);
        txtRegExp.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(final DocumentEvent e) {
                    change();
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    change();
                }

                @Override
                public void changedUpdate(final DocumentEvent e) {
                    change();
                }

                private void change() {
                    regexFilter.setActive(true);
                    final FilterSet set = getFilterSetControl().getFilterSet();
                    if (!set.getFilters().contains(regexFilter)) {
                        set.add(regexFilter);
                    }
                    try {
                        regexFilter.setRegex(txtRegExp.getText());
                        txtRegExp.setForeground(Color.BLACK);
                        String regex = ".*" + txtRegExp.getText() + ".*";
                        regex = regex.replaceAll("(\\.\\*)+", ".*");
                        txtRegExp.setToolTipText("<html>" + "nice RegExp ;-)<br>"
                                    + regex + "</html>");
                    } catch (final Exception e) {
                        String message = e.getMessage();
                        message = message.replaceAll("\n", "<br>");
                        txtRegExp.setToolTipText("<html>" + message + "</html>");
                        txtRegExp.setForeground(Color.RED);
                    }
                }
            });
        getEventTable().setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * TODO: extremely errorprone singleton implementation
     *
     * @return  DOCUMENT ME!
     */
    public static BeanMillPane getInstance() {
        return instance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  b  DOCUMENT ME!
     */
    public void setActiveSelectionCheckBoxEnabled(final boolean b) {
        chkSelectionOnly.setEnabled(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  filepath  DOCUMENT ME!
     */
    private void saveEvents(final String filepath) {
        try {
            final Event[] allEvents = getLog().toArray();
            final Element root = new Element("BeanmillLog");
            for (final Event e : allEvents) {
                root.addContent(new XMLEvent(e).getXMLElement());
            }
            final Document doc = new Document(root);
            final Format format = Format.getPrettyFormat();
            final XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
            final File file = new File(filepath);
            final FileWriter writer = new FileWriter(file);
            serializer.output(doc, writer);
            writer.flush();
            writer.close();
        } catch (final Throwable t) {
            LOG.error("could not save events to file: " + filepath, t);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  filepath  DOCUMENT ME!
     */
    private void loadEvents(final String filepath) {
        try {
            final SAXBuilder builder = new SAXBuilder(false);
            final Document doc = builder.build(new File(filepath));
            final Element rootObject = doc.getRootElement();
            final List<XMLEvent> v = new ArrayList<XMLEvent>();
            final Table model = (Table)getEventTable().getModel();
            final List l = rootObject.getChildren();
            for (final Object o : l) {
                if (o instanceof Element) {
                    final XMLEvent xmlEvent = new XMLEvent((Element)o);
                    v.add(xmlEvent);
                    model.getLog().add(xmlEvent);
                }
            }
        } catch (final Exception e) {
            LOG.error("could not load events from file: " + filepath, e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Element getAllFilters() {
        final Element r = new Element("BeanmillFilters");
        final List fList = getFilterSetControl().getFilterSet().getFilters();
        for (int i = 0; i < fList.size(); ++i) {
            final Filter f = (Filter)fList.get(i);
            if (f instanceof Tree) {
                r.addContent(((Tree)f).getXML());
            } else if (f instanceof ListView) {
                r.addContent(((ListView)f).getXML());
            }
        }
        return r;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rootObject  DOCUMENT ME!
     */
    public void setAllFilters(final Element rootObject) {
        getFilterSetView().removeAll();
        getFilterSetControl().getFilterSet().removeAll();
        final List allFilters = rootObject.getChildren();
        for (final Object e : allFilters) {
            final Element tf = (Element)e;
            if (tf.getName().equals("treefilter")) {
                final Tree tree = new Tree(tf);
                getFilterSetControl().addTree(tree);
                tree.setActive(false);
            } else if (tf.getName().equals("listfilter")) {
                final ListView lv = getFilterSetControl().addListView(tf);
                lv.setActive(false);
            }
        }
        getFilterSetView().setSelectedIndex(0);
        getFilterSetControl().getActiveComponent().getFilter().setActive(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fullPath  DOCUMENT ME!
     * @param  from      DOCUMENT ME!
     * @param  to        DOCUMENT ME!
     */
    public void setSelection(final String fullPath, final int from, final int to) {
        activeSelection.setSelection(fullPath, from, to);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JTable getEventTable() {
        return getEventsPanel().getEventTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JEditorPane getDetailPanel() {
        return getEventsPanel().getEventView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JToolBar getToolBar() {
        return getLogActiveStateView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FilterView getActiveFilterView() {
        return getFilterSetControl().getActiveComponent();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ListView addListView() {
        return getFilterSetControl().addListView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Log getLog() {
        return getLogControl().getLog();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private LogControl getLogControl() {
        return getTableControl().getLogControl();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public LogActiveStateView getLogActiveStateView() {
        return getLogControl().getActiveStateView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private EventsPanel getEventsPanel() {
        return eventsPanelControl.getView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private FilterSetView getFilterSetView() {
        return getFilterSetControl().getFilterSetView();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private FilterSetControl getFilterSetControl() {
        return getTableControl().getFilterSetControl();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private TableControl getTableControl() {
        return eventsPanelControl.getTableControl();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  location  DOCUMENT ME!
     */
    public void setSplitPaneDividerLocation(final int location) {
        splMain.setDividerLocation(location);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getSplitPaneDividerLocation() {
        return splMain.getDividerLocation();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isDeleteFilteredEvents() {
        return deleteFilteredEvents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  deleteFilteredEvents  DOCUMENT ME!
     */
    public void setDeleteFilteredEvents(final boolean deleteFilteredEvents) {
        this.deleteFilteredEvents = deleteFilteredEvents;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        splMain = new javax.swing.JSplitPane();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("jLabel1");
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        setLayout(new java.awt.BorderLayout());
        add(splMain, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents
}
