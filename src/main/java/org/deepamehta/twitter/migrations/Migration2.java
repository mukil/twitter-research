package org.deepamehta.twitter.migrations;

import de.deepamehta.accesscontrol.AccessControlService;
import de.deepamehta.core.Topic;
import de.deepamehta.core.TopicType;
import de.deepamehta.core.model.*;
import de.deepamehta.core.service.Inject;
import de.deepamehta.core.service.Migration;
import de.deepamehta.core.service.accesscontrol.SharingMode;
import de.deepamehta.workspaces.WorkspacesService;
import java.util.logging.Logger;
import static org.deepamehta.twitter.TwitterService.COMPOSITION_DEF_EDGE_TYPE;
import static org.deepamehta.twitter.TwitterService.DEEPAMEHTA_USERNAME_URI;
import static org.deepamehta.twitter.TwitterService.GEO_COORDINATE_TOPIC_URI;
import static org.deepamehta.twitter.TwitterService.TWEETED_TO_STATUS_ID;
import static org.deepamehta.twitter.TwitterService.TWEET_CONTENT_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_ENTITIES_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_FAVOURITE_COUNT_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_ID_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_LOCATION_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_METADATA_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_SOURCE_BUTTON_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_TIME_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_WITHHELD_DMCA_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_WITHHELD_IN_URI;
import static org.deepamehta.twitter.TwitterService.TWEET_WITHHELD_SCOPE_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_LANG_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_LOCATION_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_MAX_TWEET_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_NEXT_PAGE_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_REFRESH_URL_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_RESULT_SIZE_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_TIME_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_TYPE_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_SEARCH_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_USER_ID_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_USER_IMAGE_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_USER_NAME_URI;
import static org.deepamehta.twitter.TwitterService.TWITTER_USER_URI;
import static org.deepamehta.twitter.TwitterService.WS_WEB_RESEARCH_NAME;
import static org.deepamehta.twitter.TwitterService.WS_WEB_RESEARCH_URI;


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

    @Inject private WorkspacesService workspaces = null;
    @Inject private AccessControlService acService = null;

    @Override
    public void run() {

        // 1) create "Twitter Research"-Workspace
        Topic workspace = workspaces.createWorkspace(WS_WEB_RESEARCH_NAME, WS_WEB_RESEARCH_URI, SharingMode.COLLABORATIVE);
        acService.setWorkspaceOwner(workspace, AccessControlService.ADMIN_USERNAME);

        // 2) assign all types to our new workspace
        TopicType twitterSearchType = dm4.getTopicType(TWITTER_SEARCH_URI);
        TopicType searchLang = dm4.getTopicType(TWITTER_SEARCH_LANG_URI);
        TopicType searchLocation = dm4.getTopicType(TWITTER_SEARCH_LOCATION_URI);
        TopicType searchType = dm4.getTopicType(TWITTER_SEARCH_TYPE_URI);
        TopicType searchNextPage = dm4.getTopicType(TWITTER_SEARCH_NEXT_PAGE_URI);
        TopicType searchRefresh = dm4.getTopicType(TWITTER_SEARCH_REFRESH_URL_URI);
        TopicType searchLastId = dm4.getTopicType(TWITTER_SEARCH_MAX_TWEET_URI);
        TopicType searchResultSize = dm4.getTopicType(TWITTER_SEARCH_RESULT_SIZE_URI);
        TopicType searchTime = dm4.getTopicType(TWITTER_SEARCH_TIME_URI);
        TopicType user = dm4.getTopicType(TWITTER_USER_URI);
        TopicType userId = dm4.getTopicType(TWITTER_USER_ID_URI);
        TopicType userName = dm4.getTopicType(TWITTER_USER_NAME_URI);
        TopicType userImageUrl = dm4.getTopicType(TWITTER_USER_IMAGE_URI);
        TopicType tweet = dm4.getTopicType(TWEET_URI);
        TopicType tweetId = dm4.getTopicType(TWEET_ID_URI);
        TopicType tweetContent = dm4.getTopicType(TWEET_CONTENT_URI);
        TopicType tweetTimestamp = dm4.getTopicType(TWEET_TIME_URI);
        TopicType tweetEntities = dm4.getTopicType(TWEET_ENTITIES_URI);
        TopicType tweetMetadata = dm4.getTopicType(TWEET_METADATA_URI);
        TopicType tweetSourceButton = dm4.getTopicType(TWEET_SOURCE_BUTTON_URI);
        TopicType tweetLocation = dm4.getTopicType(TWEET_LOCATION_URI);
        TopicType tweetFavouriteCount = dm4.getTopicType(TWEET_FAVOURITE_COUNT_URI);
        TopicType tweetWithheldCopy = dm4.getTopicType(TWEET_WITHHELD_DMCA_URI);
        TopicType tweetWithheldIn = dm4.getTopicType(TWEET_WITHHELD_IN_URI);
        TopicType tweetWithheldScope = dm4.getTopicType(TWEET_WITHHELD_SCOPE_URI);
        TopicType tweetedToStatusId = dm4.getTopicType(TWEETED_TO_STATUS_ID);
        assignToWorkspace(twitterSearchType);
        assignToWorkspace(searchLang);
        assignToWorkspace(searchLocation);
        assignToWorkspace(searchType);
        assignToWorkspace(searchNextPage);
        assignToWorkspace(searchRefresh);
        assignToWorkspace(searchLastId);
        assignToWorkspace(searchResultSize);
        assignToWorkspace(searchTime);
        assignToWorkspace(user);
        assignToWorkspace(userId);
        assignToWorkspace(userName);
        assignToWorkspace(userImageUrl);
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

        // 3) Model "Geo Coordinate" to "Tweet"
        TopicType tweet_type = dm4.getTopicType(TWEET_URI);
        tweet_type.addAssocDef(mf.newAssociationDefinitionModel(
                COMPOSITION_DEF_EDGE_TYPE, TWEET_URI, GEO_COORDINATE_TOPIC_URI, "dm4.core.one", "dm4.core.one"));
    }

    // === Workspace ===

    private void assignToWorkspace(Topic topic) {
        Topic twitterWorkspace = dm4.getTopicByUri(WS_WEB_RESEARCH_URI);
        workspaces.assignToWorkspace(topic, twitterWorkspace.getId());
    }

}
