package org.deepamehta.twitter.migrations;

import de.deepamehta.core.service.Migration;
import org.deepamehta.twitter.TwitterService;

/** 
 * This migration changes the Workspace name to "Twitter" and its uri to be simply
 * "org.deepamehta.workspaces.twitter".
 */
public class Migration3 extends Migration {

    @Override
    public void run() {
        // 1) Configure new user profile icon 
        dm4.getTopicType(TwitterService.TWITTER_USER_URI).getViewConfig().addSetting("dm4.webclient.view_config",
                "dm4.webclient.icon", "/org.deepamehta.twitter-research/images/profile_twitter-32-42.png");
        // 2) Configure new search bucket icon
        dm4.getTopicType(TwitterService.TWITTER_SEARCH_URI).getViewConfig().addSetting("dm4.webclient.view_config",
                "dm4.webclient.icon", "/org.deepamehta.twitter-research/images/search_twitter-32.png");
    }

}
