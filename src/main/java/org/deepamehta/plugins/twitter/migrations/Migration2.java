package org.deepamehta.plugins.twitter.migrations;

import de.deepamehta.core.Topic;
import de.deepamehta.core.TopicType;
import de.deepamehta.core.model.AssociationModel;
import de.deepamehta.core.model.SimpleValue;
import de.deepamehta.core.model.TopicModel;
import de.deepamehta.core.model.TopicRoleModel;
import de.deepamehta.core.service.Migration;
import java.util.logging.Logger;


/**
 * A basic wrapper for the public Twitter Search API and DeepaMehta 4.
 *
 * @version 1.2
 * @author Malte Reißig (<malte@mikromedia.de>)
 * @website http://github.com/mukil
 *
 */

public class Migration2 extends Migration {

    private Logger logger = Logger.getLogger(getClass().getName());

    private final static String TWEET_URI = "org.deepamehta.twitter.tweet";
    private final static String TWEET_ID_URI = "org.deepamehta.twitter.tweet_id";
    private final static String TWEET_TIME_URI = "org.deepamehta.twitter.tweet_time";
    private final static String TWEET_CONTENT_URI = "org.deepamehta.twitter.tweet_content";
    private final static String TWEET_ENTITIES_URI = "org.deepamehta.twitter.tweet_entities";
    private final static String TWEET_METADADA_URI = "org.deepamehta.twitter.tweet_metadata";
    private final static String TWEET_SOURCE_BUTTON_URI = "org.deepamehta.twitter.tweet_source_button";
    private final static String TWEET_LOCATION_URI = "org.deepamehta.twitter.tweet_location";
    private final static String TWEET_FAVOURITE_COUNT_URI = "org.deepamehta.twitter.tweet_favourite_count";
    private final static String TWEET_WITHHELD_DMCA_URI = "org.deepamehta.twitter.tweet_withheld_copyright";
    private final static String TWEET_WITHHELD_IN_URI = "org.deepamehta.twitter.tweet_withheld_in";
    private final static String TWEET_WITHHELD_SCOPE_URI = "org.deepamehta.twitter.tweet_withheld_scope";
    private final static String TWEETED_TO_STATUS_ID = "org.deepamehta.twitter.tweeted_to_status_id";

    private final static String TWITTER_USER_URI = "org.deepamehta.twitter.user";
    private final static String TWITTER_USER_ID_URI = "org.deepamehta.twitter.user_id";
    private final static String TWITTER_USER_NAME_URI = "org.deepamehta.twitter.user_name";
    private final static String TWITTER_USER_IMAGE_URI = "org.deepamehta.twitter.user_image_url";

    private final static String TWITTER_SEARCH_URI = "org.deepamehta.twitter.search";
    private final static String TWITTER_SEARCH_LANG_URI = "org.deepamehta.twitter.search_language";
    private final static String TWITTER_SEARCH_LOCATION_URI = "org.deepamehta.twitter.search_location";
    private final static String TWITTER_SEARCH_TYPE_URI = "org.deepamehta.twitter.search_result_type";
    private final static String TWITTER_SEARCH_NEXT_PAGE_URI = "org.deepamehta.twitter.search_next_page";
    private final static String TWITTER_SEARCH_REFRESH_URL_URI = "org.deepamehta.twitter.search_refresh_url";
    private final static String TWITTER_SEARCH_MAX_TWEET_URI = "org.deepamehta.twitter.search_last_tweet_id";
    private final static String TWITTER_SEARCH_RESULT_SIZE_URI = "org.deepamehta.twitter.search_result_size";
    private final static String TWITTER_SEARCH_TIME_URI = "org.deepamehta.twitter.last_search_time";

    private final static String DEEPAMEHTA_USERNAME_URI = "dm4.accesscontrol.username";


    private String WS_WEB_RESEARCH_URI = "org.deepamehta.workspaces.web_research";

    @Override
    public void run() {

        // create "Twitter Research"-Workspace
        TopicModel workspace = new TopicModel(WS_WEB_RESEARCH_URI, "dm4.workspaces.workspace");
        Topic ws = dms.createTopic(workspace, null);
        ws.setSimpleValue("Twitter Research");
        // assign "admin" username to "Twitter Research"-Workspace
        Topic administrator = dms.getTopic(DEEPAMEHTA_USERNAME_URI, new SimpleValue("admin"), true);
        // logger.info("Assigning user \"" + administrator.getSimpleValue() + "\" to new WS: \"Twitter Research\"");
        assignWorkspace(administrator);
        // assign all Types
        TopicType twitterSearchType = dms.getTopicType(TWITTER_SEARCH_URI);
        TopicType searchLang = dms.getTopicType(TWITTER_SEARCH_LANG_URI);
        TopicType searchLocation = dms.getTopicType(TWITTER_SEARCH_LOCATION_URI);
        TopicType searchType = dms.getTopicType(TWITTER_SEARCH_TYPE_URI);
        TopicType searchNextPage = dms.getTopicType(TWITTER_SEARCH_NEXT_PAGE_URI);
        TopicType searchRefresh = dms.getTopicType(TWITTER_SEARCH_REFRESH_URL_URI);
        TopicType searchLastId = dms.getTopicType(TWITTER_SEARCH_MAX_TWEET_URI);
        TopicType searchResultSize = dms.getTopicType(TWITTER_SEARCH_RESULT_SIZE_URI);
        TopicType searchTime = dms.getTopicType(TWITTER_SEARCH_TIME_URI);
        //
        TopicType user = dms.getTopicType(TWITTER_USER_URI);
        TopicType userId = dms.getTopicType(TWITTER_USER_ID_URI);
        TopicType userName = dms.getTopicType(TWITTER_USER_NAME_URI);
        TopicType userImageUrl = dms.getTopicType(TWITTER_USER_IMAGE_URI);
        //
        TopicType tweet = dms.getTopicType(TWEET_URI);
        TopicType tweetId = dms.getTopicType(TWEET_ID_URI);
        TopicType tweetContent = dms.getTopicType(TWEET_CONTENT_URI);
        TopicType tweetTimestamp = dms.getTopicType(TWEET_TIME_URI);
        TopicType tweetEntities = dms.getTopicType(TWEET_ENTITIES_URI);
        TopicType tweetMetadata = dms.getTopicType(TWEET_METADADA_URI);
        TopicType tweetSourceButton = dms.getTopicType(TWEET_SOURCE_BUTTON_URI);
        TopicType tweetLocation = dms.getTopicType(TWEET_LOCATION_URI);
        TopicType tweetFavouriteCount = dms.getTopicType(TWEET_FAVOURITE_COUNT_URI);
        TopicType tweetWithheldCopy = dms.getTopicType(TWEET_WITHHELD_DMCA_URI);
        TopicType tweetWithheldIn = dms.getTopicType(TWEET_WITHHELD_IN_URI);
        TopicType tweetWithheldScope = dms.getTopicType(TWEET_WITHHELD_SCOPE_URI);
        TopicType tweetedToStatusId = dms.getTopicType(TWEETED_TO_STATUS_ID);
        //
        assignWorkspace(twitterSearchType);
        assignWorkspace(searchLang);
        assignWorkspace(searchLocation);
        assignWorkspace(searchType);
        assignWorkspace(searchNextPage);
        assignWorkspace(searchRefresh);
        assignWorkspace(searchLastId);
        assignWorkspace(searchResultSize);
        assignWorkspace(searchTime);
        //
        assignWorkspace(user);
        assignWorkspace(userId);
        assignWorkspace(userName);
        assignWorkspace(userImageUrl);
        //
        assignWorkspace(tweet);
        assignWorkspace(tweetId);
        assignWorkspace(tweetContent);
        assignWorkspace(tweetTimestamp);
        assignWorkspace(tweetEntities);
        assignWorkspace(tweetSourceButton);
        assignWorkspace(tweetMetadata);
        assignWorkspace(tweetLocation);
        assignWorkspace(tweetFavouriteCount);
        assignWorkspace(tweetWithheldCopy);
        assignWorkspace(tweetWithheldScope);
        assignWorkspace(tweetWithheldIn);
        assignWorkspace(tweetedToStatusId);

    }

    // === Workspace ===

    private void assignWorkspace(Topic topic) {
        Topic defaultWorkspace = dms.getTopic("uri", new SimpleValue(WS_WEB_RESEARCH_URI), false);
        dms.createAssociation(new AssociationModel("dm4.core.aggregation",
            new TopicRoleModel(topic.getId(), "dm4.core.parent"),
            new TopicRoleModel(defaultWorkspace.getId(), "dm4.core.child")
        ), null);
    }

}
