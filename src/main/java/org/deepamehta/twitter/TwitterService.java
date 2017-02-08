package org.deepamehta.twitter;

import de.deepamehta.core.Topic;

/**
 * A very basic client for researching the public Twitter Search API v1.1 with DeepaMehta 4.
 *
 * @author Malte Reißig (<malte@mikromedia.de>)
 * @version 1.3.4-SNAPSHOT
 * @website https://github.com/mukil/twitter-research
 *
 */

public interface TwitterService {

    public final String DEEPAMEHTA_VERSION = "DeepaMehta 4.8.4";
    public final String TWITTER_RESEARCH_VERSION = "1.3.4-SNAPSHOT";
    public final String CHARSET = "UTF-8";

    public final static String TWITTER_AUTHENTICATION_URL = "https://api.twitter.com/oauth2/token";
    public final static String TWITTER_SEARCH_BASE_URL = "https://api.twitter.com/1.1/search/tweets.json";

    public final String WS_WEB_RESEARCH_URI = "org.deepamehta.workspaces.twitter";
    public final String WS_WEB_RESEARCH_NAME = "Twitter";

    public final String COMPOSITION_DEF_EDGE_TYPE = "dm4.core.composition_def";
    public final String ROLE_PARENT_TYPE_URI = "dm4.core.parent_type";
    public final String ROLE_CHILD_TYPE_URI = "dm4.core.child_type";
    public final String DEEPAMEHTA_USERNAME_URI = "dm4.accesscontrol.username";

    public final static String CHILD_URI = "dm4.core.child";
    public final static String PARENT_URI = "dm4.core.parent";
    public final static String AGGREGATION = "dm4.core.aggregation";
    public final static String COMPOSITION = "dm4.core.composition";

    public final static String TWEET_URI = "org.deepamehta.twitter.tweet";
    public final static String TWEET_ID_URI = "org.deepamehta.twitter.tweet_id";
    public final static String TWEET_TIME_URI = "org.deepamehta.twitter.tweet_time";
    public final static String TWEET_CONTENT_URI = "org.deepamehta.twitter.tweet_content";
    public final static String TWEET_ENTITIES_URI = "org.deepamehta.twitter.tweet_entities";
    public final static String TWEET_METADATA_URI = "org.deepamehta.twitter.tweet_metadata";
    public final static String TWEET_SOURCE_BUTTON_URI = "org.deepamehta.twitter.tweet_source_button";
    public final static String TWEET_LOCATION_URI = "org.deepamehta.twitter.tweet_location";
    public final static String TWEET_FAVOURITE_COUNT_URI = "org.deepamehta.twitter.tweet_favourite_count";
    public final static String TWEET_WITHHELD_DMCA_URI = "org.deepamehta.twitter.tweet_withheld_copyright";
    public final static String TWEET_WITHHELD_IN_URI = "org.deepamehta.twitter.tweet_withheld_in";
    public final static String TWEET_WITHHELD_SCOPE_URI = "org.deepamehta.twitter.tweet_withheld_scope";
    public final static String TWEETED_TO_STATUS_ID = "org.deepamehta.twitter.tweeted_to_status_id";

    public final static String TWITTER_USER_URI = "org.deepamehta.twitter.user";
    public final static String TWITTER_USER_ID_URI = "org.deepamehta.twitter.user_id";
    public final static String TWITTER_USER_NAME_URI = "org.deepamehta.twitter.user_name";
    public final static String TWITTER_USER_IMAGE_URI = "org.deepamehta.twitter.user_image_url";

    public final static String TWITTER_SEARCH_URI = "org.deepamehta.twitter.search";
    public final static String TWITTER_SEARCH_LANG_URI = "org.deepamehta.twitter.search_language";
    public final static String TWITTER_SEARCH_LOCATION_URI = "org.deepamehta.twitter.search_location";
    public final static String TWITTER_SEARCH_TYPE_URI = "org.deepamehta.twitter.search_result_type";
    public final static String TWITTER_SEARCH_NEXT_PAGE_URI = "org.deepamehta.twitter.search_next_page";
    public final static String TWITTER_SEARCH_REFRESH_URL_URI = "org.deepamehta.twitter.search_refresh_url";
    public final static String TWITTER_SEARCH_MAX_TWEET_URI = "org.deepamehta.twitter.search_last_tweet_id";
    public final static String TWITTER_SEARCH_RESULT_SIZE_URI = "org.deepamehta.twitter.search_result_size";
    public final static String TWITTER_SEARCH_TIME_URI = "org.deepamehta.twitter.last_search_time";

    public final static String TWITTER_KEY = "org.deepamehta.twitter.key";
    public final static String TWITTER_SECRET = "org.deepamehta.twitter.secret";
    public final static String TWITTER_APPLICATION_KEY = "org.deepamehta.twitter.application_key";
    public final static String TWITTER_APPLICATION_SECRET = "org.deepamehta.twitter.application_secret";

    public final String GEO_COORDINATE_TOPIC_URI = "dm4.geomaps.geo_coordinate";
    public final String GEO_LONGITUDE_TYPE_URI = "dm4.geomaps.longitude";
    public final String GEO_LATITUDE_TYPE_URI = "dm4.geomaps.latitude";

    Topic searchPublicTweets(long searchTopicId, String query, String resultType,
            String lang, String location);

    Topic searchMoreTweets(long searchTopicId, boolean fetchMore);

}
