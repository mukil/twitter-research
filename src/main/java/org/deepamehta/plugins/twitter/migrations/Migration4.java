package org.deepamehta.plugins.twitter.migrations;

import de.deepamehta.core.service.Migration;

/** 
 * This migration changes the Workspace name to "Twitter" and its uri to be simply
 * "org.deepamehta.workspaces.twitter".
 */
public class Migration4 extends Migration {

    @Override
    public void run() {

        // 1) Configure new user profile icon 
        dm4.getTopicType("org.deepamehta.twitter.user").getViewConfig().addSetting("dm4.webclient.view_config",
                "dm4.webclient.icon", "/org.deepamehta.twitter-research/images/profile_twitter-32-42.png");
        // 2) Configure new search bucket icon
        dm4.getTopicType("org.deepamehta.twitter.search").getViewConfig().addSetting("dm4.webclient.view_config",
                "dm4.webclient.icon", "/org.deepamehta.twitter-research/images/search_twitter-32.png");

    }

}
