package org.deepamehta.twitter;

import de.deepamehta.core.Topic;
import de.deepamehta.core.service.Migration;

/** 
 * This migration changes the Workspace name to "Twitter" and its uri to be simply
 * "org.deepamehta.workspaces.twitter".
 */
public class Migration3 extends Migration {

    @Override
    public void run() {

        // 1) Changing WS-Name Topic
        Topic workspace = dm4.getTopicByUri("org.deepamehta.workspaces.web_research")
            .loadChildTopics();
        workspace.getChildTopics().set("dm4.workspaces.name", "Twitter");
        workspace.setUri("org.deepamehta.workspaces.twitter");
    }

}
