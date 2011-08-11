/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ListView.java
 *
 * Created on 24. August 2007, 16:44
 */
package de.cismet.beanmill.filter;

import com.traxel.lumbermill.event.Event;
import com.traxel.lumbermill.filter.Filter;
import com.traxel.lumbermill.filter.FilterListener;
import com.traxel.lumbermill.filter.FilterSet;
import com.traxel.lumbermill.filter.FilterView;

import org.jdom.Element;

import java.awt.Component;

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class ListView extends JPanel implements FilterView, Filter {

    //~ Instance fields --------------------------------------------------------

    private final ImageIcon classIcon;
    private final DefaultListModel listModel;
    private final FilterSet filterSet;
    private final Vector<FilterListener> listeners;
    private boolean active;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstClasses;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ListView.
     *
     * @param  fs  DOCUMENT ME!
     */
    public ListView(final FilterSet fs) {
        initComponents();
        classIcon = new ImageIcon(getClass().getResource(
                    "/de/cismet/beanmill/res/class.png"));
        listModel = new DefaultListModel();
        listeners = new Vector<FilterListener>();
        filterSet = fs;
        active = true;
        lstClasses.setModel(listModel);
        lstClasses.setCellRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList arg0,
                        final Object arg1,
                        final int arg2,
                        final boolean arg3,
                        final boolean arg4) {
                    final JLabel l = (JLabel)super.getListCellRendererComponent(
                            arg0,
                            arg1,
                            arg2,
                            arg3,
                            arg4);
                    l.setIcon(classIcon);
                    return l;
                }
            });
    }

    /**
     * Creates a new ListView object.
     *
     * @param  fs   DOCUMENT ME!
     * @param  xml  DOCUMENT ME!
     */
    public ListView(final FilterSet fs, final Element xml) {
        this(fs);
        if (xml != null) {
            final List children = xml.getChildren("javaclass");
            for (final Object o : children) {
                final Element e = (Element)o;
                final String path = e.getAttributeValue("fullpath");
                addJavaClassName(path);
            }
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getFilterComponent() {
        return this;
    }

    @Override
    public Filter getFilter() {
        return this;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public String getName() {
        return "ListFilter";
    }

    @Override
    public boolean isVisible(final Event e) {
        return listModel.contains(e.getSource());
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }

    @Override
    public void removeFilterListener(final FilterListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    @Override
    public void addFilterListener(final FilterListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    public void addJavaClassName(final String name) {
        if (!listModel.contains(name)) {
            listModel.addElement(name);
            filterSet.fireFilterAdded(null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Element getXML() {
        final Element lf = new Element("listfilter");
        for (final Object o : listModel.toArray()) {
            final Element javaclass = new Element("javaclass");
            javaclass.setAttribute("fullpath", o.toString());
            lf.addContent(javaclass);
        }
        return lf;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        lstClasses = new javax.swing.JList();

        setLayout(new java.awt.BorderLayout());

        lstClasses.setModel(new javax.swing.AbstractListModel() {

                String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

                @Override
                public int getSize() {
                    return strings.length;
                }
                @Override
                public Object getElementAt(final int i) {
                    return strings[i];
                }
            });
        jScrollPane1.setViewportView(lstClasses);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents
}
