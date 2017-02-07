package org.deepamehta.twitter.migrations;

import de.deepamehta.accesscontrol.AccessControlService;
import de.deepamehta.core.Topic;
import de.deepamehta.core.TopicType;
import de.deepamehta.core.service.Inject;
import de.deepamehta.core.service.Migration;
import de.deepamehta.workspaces.WorkspacesService;
import org.deepamehta.twitter.TwitterService;

/** 
 * Migrate Twitter API settings topics into the (as of DM 4.8 new) "Administration" workspace.
 */
public class Migration4 extends Migration {

    @Inject private WorkspacesService wsService = null;

    @Override
    public void run() {
        Topic administrationWs = dm4.getTopicByUri(AccessControlService.ADMINISTRATION_WORKSPACE_URI);
        TopicType keyType = dm4.getTopicType(TwitterService.TWITTER_KEY);
        wsService.assignToWorkspace(keyType, administrationWs.getId());
        TopicType secretType = dm4.getTopicType(TwitterService.TWITTER_SECRET);
        wsService.assignToWorkspace(secretType, administrationWs.getId());
        Topic applicationKey = dm4.getTopicByUri(TwitterService.TWITTER_KEY);
        wsService.assignToWorkspace(applicationKey, administrationWs.getId());
        Topic appSecret = dm4.getTopicByUri(TwitterService.TWITTER_APPLICATION_SECRET);
        wsService.assignToWorkspace(appSecret, administrationWs.getId());
    }

}
