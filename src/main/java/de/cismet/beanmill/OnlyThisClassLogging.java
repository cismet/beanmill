/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.beanmill;

//import org.netbeans.jmi.javamodel.Resource;

import com.traxel.lumbermill.filter.FilterView;

import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.modules.editor.NbEditorUtilities;

import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;

import de.cismet.beanmill.filter.ListView;

/**
 * import org.netbeans.modules.javacore.api.JavaModel;.
 *
 * @version  $Revision$, $Date$
 */
public final class OnlyThisClassLogging extends CallableSystemAction {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void performAction() {
//        // TODO implement action body
        final TopComponent activeTc = TopComponent.getRegistry().getActivated();
        final EditorCookie ec = (EditorCookie)TopComponent.getRegistry().getActivatedNodes()[0].getCookie(
                EditorCookie.class);
        final FileObject fo = NbEditorUtilities.getFileObject(ec.getDocument());
        final ClassPath sourceCP = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        final String fullName = sourceCP.getResourceName(fo, '.', false);
        final FilterView fv = LoggingTopComponent.getDefault().getMillPane().getActiveFilterView();
        if (fv instanceof ListView) {
            ((ListView)fv).addJavaClassName(fullName);
        } else {
            final ListView fv2 = LoggingTopComponent.getDefault().getMillPane().addListView();
            fv2.addJavaClassName(fullName);
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(OnlyThisClassLogging.class, "CTL_OnlyThisClassLogging");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
