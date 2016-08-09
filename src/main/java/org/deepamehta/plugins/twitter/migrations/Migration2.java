package org.deepamehta.plugins.twitter.migrations;

import de.deepamehta.core.Topic;
import de.deepamehta.core.TopicType;
import de.deepamehta.core.model.*;
import de.deepamehta.core.service.Inject;
import de.deepamehta.core.service.Migration;
import de.deepamehta.workspaces.WorkspacesService;
import java.util.logging.Logger;


/**
 * A very basic client for researching the public Twitter Search API v1.1 with DeepaMehta 4.
 *
 * @version 1.3.4-SNAPSHOT
 * @author Malte Reißig (<malte@mikromedia.de>)
 * @website https://github.com/mukil/twitter-research
 *
 */
public class Migration2 extends Migration {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Inject private WorkspacesService wsService = null;

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

    private final static String GEO_COORDINATE_URI = "dm4.geomaps.geo_coordinate";

    private String COMPOSITION_DEF_EDGE_TYPE = "dm4.core.composition_def";
    private String ROLE_PARENT_TYPE_URI = "dm4.core.parent_type";
    private String ROLE_CHILD_TYPE_URI = "dm4.core.child_type";

    private final static String DEEPAMEHTA_USERNAME_URI = "dm4.accesscontrol.username";


    private String WS_WEB_RESEARCH_URI = "org.deepamehta.workspaces.web_research";

    @Override
    public void run() {

        // 1) create "Twitter Research"-Workspace
        TopicModel workspace = mf.newTopicModel(WS_WEB_RESEARCH_URI, "dm4.workspaces.workspace");
        Topic ws = dm4.createTopic(workspace);
        ws.setSimpleValue("Twitter Research");
        // 2) assign "admin" username to "Twitter Research"-Workspace
        Topic administrator = dm4.getTopicByValue(DEEPAMEHTA_USERNAME_URI, new SimpleValue("admin"));
        assignToWorkspace(administrator);
        // 3) assign all types to our new workspace
        TopicType twitterSearchType = dm4.getTopicType(TWITTER_SEARCH_URI);
        TopicType searchLang = dm4.getTopicType(TWITTER_SEARCH_LANG_URI);
        TopicType searchLocation = dm4.getTopicType(TWITTER_SEARCH_LOCATION_URI);
        TopicType searchType = dm4.getTopicType(TWITTER_SEARCH_TYPE_URI);
        TopicType searchNextPage = dm4.getTopicType(TWITTER_SEARCH_NEXT_PAGE_URI);
        TopicType searchRefresh = dm4.getTopicType(TWITTER_SEARCH_REFRESH_URL_URI);
        TopicType searchLastId = dm4.getTopicType(TWITTER_SEARCH_MAX_TWEET_URI);
        TopicType searchResultSize = dm4.getTopicType(TWITTER_SEARCH_RESULT_SIZE_URI);
        TopicType searchTime = dm4.getTopicType(TWITTER_SEARCH_TIME_URI);
        //
        TopicType user = dm4.getTopicType(TWITTER_USER_URI);
        TopicType userId = dm4.getTopicType(TWITTER_USER_ID_URI);
        TopicType userName = dm4.getTopicType(TWITTER_USER_NAME_URI);
        TopicType userImageUrl = dm4.getTopicType(TWITTER_USER_IMAGE_URI);
        //
        TopicType tweet = dm4.getTopicType(TWEET_URI);
        TopicType tweetId = dm4.getTopicType(TWEET_ID_URI);
        TopicType tweetContent = dm4.getTopicType(TWEET_CONTENT_URI);
        TopicType tweetTimestamp = dm4.getTopicType(TWEET_TIME_URI);
        TopicType tweetEntities = dm4.getTopicType(TWEET_ENTITIES_URI);
        TopicType tweetMetadata = dm4.getTopicType(TWEET_METADADA_URI);
        TopicType tweetSourceButton = dm4.getTopicType(TWEET_SOURCE_BUTTON_URI);
        TopicType tweetLocation = dm4.getTopicType(TWEET_LOCATION_URI);
        TopicType tweetFavouriteCount = dm4.getTopicType(TWEET_FAVOURITE_COUNT_URI);
        TopicType tweetWithheldCopy = dm4.getTopicType(TWEET_WITHHELD_DMCA_URI);
        TopicType tweetWithheldIn = dm4.getTopicType(TWEET_WITHHELD_IN_URI);
        TopicType tweetWithheldScope = dm4.getTopicType(TWEET_WITHHELD_SCOPE_URI);
        TopicType tweetedToStatusId = dm4.getTopicType(TWEETED_TO_STATUS_ID);
        //
        assignToWorkspace(twitterSearchType);
        assignToWorkspace(searchLang);
        assignToWorkspace(searchLocation);
        assignToWorkspace(searchType);
        assignToWorkspace(searchNextPage);
        assignToWorkspace(searchRefresh);
        assignToWorkspace(searchLastId);
        assignToWorkspace(searchResultSize);
        assignToWorkspace(searchTime);
        //
        assignToWorkspace(user);
        assignToWorkspace(userId);
        assignToWorkspace(userName);
        assignToWorkspace(userImageUrl);
        //
        assignToWorkspace(tweet);
        assignToWorkspace(tweetId);
        assignToWorkspace(tweetContent);
        assignToWorkspace(tweetTimestamp);
        assignToWorkspace(tweetEntities);
        assignToWorkspace(tweetSourceButton);
        assignToWorkspace(tweetMetadata);
        assignToWorkspace(tweetLocation);
        assignToWorkspace(tweetFavouriteCount);
        assignToWorkspace(tweetWithheldCopy);
        assignToWorkspace(tweetWithheldScope);
        assignToWorkspace(tweetWithheldIn);
        assignToWorkspace(tweetedToStatusId);
        // 4) Model "Geo Coordinate" to "Tweet"
        TopicType tweet_type = dm4.getTopicType(TWEET_URI);
        tweet_type.addAssocDef(mf.newAssociationDefinitionModel(
                COMPOSITION_DEF_EDGE_TYPE, TWEET_URI, GEO_COORDINATE_URI, "dm4.core.one", "dm4.core.one"));
    }

    // === Workspace ===

    private void assignToWorkspace(Topic topic) {
        Topic twitterWorkspace = dm4.getTopicByUri(WS_WEB_RESEARCH_URI);
        wsService.assignToWorkspace(topic, twitterWorkspace.getId());
    }

}
