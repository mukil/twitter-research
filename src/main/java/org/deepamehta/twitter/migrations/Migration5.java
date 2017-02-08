package org.deepamehta.twitter.migrations;

import de.deepamehta.core.service.Inject;
import de.deepamehta.core.service.Migration;
import de.deepamehta.workspaces.WorkspacesService;
import java.util.logging.Logger;

/** 
 * Doing nothing here - but some datbase may have a Migration5 installed from an earlier plugin version.
 */
public class Migration5 extends Migration {

    private Logger log = Logger.getLogger(getClass().getName());

    @Inject private WorkspacesService wsService = null;

    @Override
    public void run() {
        log.info("Migration5 is deprecated (but the number 5 is burned on older database instances) "
            + "so we're doing nothing but loggin a statement here.");
    }

}
