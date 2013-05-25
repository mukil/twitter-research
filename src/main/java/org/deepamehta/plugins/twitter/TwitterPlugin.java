package org.deepamehta.plugins.twitter;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;

import de.deepamehta.core.model.TopicModel;
import de.deepamehta.core.Topic;
import de.deepamehta.core.model.*;
import de.deepamehta.core.osgi.PluginActivator;
import de.deepamehta.core.service.ClientState;
import de.deepamehta.core.service.Directives;
import de.deepamehta.core.storage.spi.DeepaMehtaTransaction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import org.deepamehta.plugins.twitter.service.TwitterService;

/**
 * A basic wrapper for the public Twitter Search API and DeepaMehta 4.
 *
 * @author Malte Reißig (<malte@mikromedia.de>)
 * @website http://github.com/mukil/dm4-twitter-research
 * @version 1.0
 *
 */

@Path("/tweet")
@Consumes("application/json")
@Produces("application/json")
public class TwitterPlugin extends PluginActivator implements TwitterService {

    private Logger log = Logger.getLogger(getClass().getName());

    private final static String VERSION = "1.0";

    private final static String CHILD_URI = "dm4.core.child";
    private final static String PARENT_URI = "dm4.core.parent";
    private final static String DEFAULT_URI = "dm4.core.default";
    private final static String AGGREGATION = "dm4.core.aggregation";
    private final static String COMPOSITION = "dm4.core.composition";
    private final static String ASSOCIATION = "dm4.core.association";
    private final static String SEARCH_RESULT = "dm4.webclient.search_result_item";

    private final static String TWEET_URI = "org.deepamehta.twitter.tweet";
    private final static String TWEET_ID_URI = "org.deepamehta.twitter.tweet_id";
    private final static String TWEET_TIME_URI = "org.deepamehta.twitter.tweet_time";
    private final static String TWEET_CONTENT_URI = "org.deepamehta.twitter.tweet_content";
    private final static String TWEET_ENTITIES_URI = "org.deepamehta.twitter.tweet_entities";
    private final static String TWEET_METADATA_URI = "org.deepamehta.twitter.tweet_metadata";
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
    private final static String TWITTER_SEARCH_QUERY_SIZE = "org.deepamehta.twitter.query_size";
    private final static String TWITTER_SEARCH_LANG_URI = "org.deepamehta.twitter.search_language";
    private final static String TWITTER_SEARCH_LOCATION_URI = "org.deepamehta.twitter.search_location";
    private final static String TWITTER_SEARCH_TYPE_URI = "org.deepamehta.twitter.search_result_type";
    private final static String TWITTER_SEARCH_NEXT_PAGE_URI = "org.deepamehta.twitter.search_next_page";
    private final static String TWITTER_SEARCH_REFRESH_URL_URI = "org.deepamehta.twitter.search_refresh_url";
    private final static String TWITTER_SEARCH_MAX_TWEET_URI = "org.deepamehta.twitter.search_last_tweet_id";
    private final static String TWITTER_SEARCH_RESULT_SIZE_URI = "org.deepamehta.twitter.search_result_size";
    private final static String TWITTER_SEARCH_TIME_URI = "org.deepamehta.twitter.last_search_time";

    private final static String SEARCH_BASE_URL = "https://search.twitter.com/search.json";

    private final String CHARSET = "UTF-8";



    /**
     *  This method executes an existing search-query to either:
     *  (a) fetch more (older) tweets for the same query or
     *  (b) fetch new tweets and assign them to the current search result
     *
     *  @param {searchId}   id of the "Twitter-Search"-Topic to operate on
     *  @param {nextPage}   <code>true</code> for paging query to next page;
     *                      <code>false</code> for fetching most recent tweets
     */

    @GET
    @Path("/search/public/{id}/{nextPage}")
    @Produces("application/json")
    public Topic searchMoreTweets(@PathParam("id") long searchId,
            @PathParam("nextPage") boolean nextPage, @HeaderParam("Cookie") ClientState clientState) {

        Topic query = dms.getTopic(searchId, true, clientState);
        StringBuffer resultBody = new StringBuffer();
        URL requestUri = null;
        DeepaMehtaTransaction tx = dms.beginTx();
        try {
            log.info("Researching tweets for Twitter-Search (" +query.getId()+ ") next ? " + nextPage);
            if (nextPage) {
                // paging to next-page (query for older-tweets)
                String nextPageUrl = query.getCompositeValue().getString(TWITTER_SEARCH_NEXT_PAGE_URI);
                requestUri = new URL(SEARCH_BASE_URL + nextPageUrl);
            } else {
                // refreshing (query for new-tweets)
                String refreshPageUrl = query.getCompositeValue().getString(TWITTER_SEARCH_REFRESH_URL_URI);
                requestUri = new URL(SEARCH_BASE_URL + refreshPageUrl);
            }
            log.info("Requesting => " + requestUri.toString());
            // initiate request
            HttpURLConnection connection = (HttpURLConnection) requestUri.openConnection();
            connection.setRequestProperty("User-Agent", "DeepaMehta Software Platform/4.1 - "
                    + "org.deepamehta Twitter Search 1.0-SNAPSHOT");
            int httpStatusCode = connection.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new WebApplicationException(new RuntimeException("Error with HTTPConnection"), httpStatusCode);
            }
            // read in the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
            for (String input; (input = rd.readLine()) != null;) {
                resultBody.append(input);
            }
            rd.close();
            // TODO: Check if answer is something like "403: Too many requests"
            if (resultBody.toString().isEmpty()) {
                log.info("Twitter just handed us an empty response.");
                throw new WebApplicationException(new RuntimeException("Twitter handed us an empty result."), 404);
            } else {
                processTwitterSearchResponse(query, resultBody, clientState);
            }
            tx.success();
        } catch (MalformedURLException e) {
            throw new WebApplicationException(new RuntimeException("Could not trigger existing search-topic."), 500);
        } catch (IOException ioe) {
            throw new WebApplicationException(new RuntimeException("HTTP I/O Error"), 500);
        } finally {
            tx.finish();
        }
        return query;
    }



    /**
     * Fetches public tweets matching the given <code>query</code>, maintains a search-query topic and
     * references existing tweets and users, as it should be.
     *
     * @param {querySize}   1-100
     * @param {resultType}  "mixed", "recent", "popular"
     * @param {lang}        ISO-639-1 Code (2 chars)
     * @param {location}    "lat,lng,radiuskm"
     */

    @GET
    @Path("/search/public/{id}/{query}/{querySize}/{resultType}/{lang}/{location}")
    @Produces("application/json")
    public Topic searchPublicTweets(@PathParam("id") long searchId, @PathParam("query") String query,
            @PathParam("querySize") int querySize, @PathParam("resultType") String resultType,
            @PathParam("lang") String lang, @PathParam("location") String location,
            @HeaderParam("Cookie") ClientState clientState) {

        StringBuffer resultBody = new StringBuffer();
        DeepaMehtaTransaction tx = dms.beginTx();
        try {
            // load search-topic created on client-side
            Topic twitterSearch = dms.getTopic(searchId, true, clientState);
            log.info("Resarching Public Tweets " +query+ " (" +resultType+ ") "
                    + "in language: " + lang + " near loc: " + location);
            // construct search query
            String queryUrl = SEARCH_BASE_URL + "?q=" + URLEncoder.encode(query.toString(), "UTF-8")
                    + ";&rpp=" + querySize + ";&include_entities=true;&result_type=" + resultType + ";";
            if (!lang.isEmpty() && !lang.equals("unspecified")) queryUrl += "&lang="+lang+";";
            if (!location.isEmpty() && !location.equals("none")) queryUrl += "&geocode="+location+";";
            URL requestUri = new URL(queryUrl);
            log.info("Requesting: " + requestUri.toString());

            // initiate request
            HttpURLConnection connection = (HttpURLConnection) requestUri.openConnection();
            connection.setRequestProperty("User-Agent", "DeepaMehta Software Platform ("
                    + "org.deepamehta Twitter Search " + VERSION + ")");
            int httpStatusCode = connection.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new WebApplicationException(new RuntimeException("Error with HTTPConnection"), httpStatusCode);
            }

			// read in the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
            for (String input; (input = rd.readLine()) != null;) {
                resultBody.append(input);
            }
            rd.close();
            if (resultBody.toString().isEmpty()) {
                log.info("Twitter just handed us an empty response.");
                throw new WebApplicationException(new RuntimeException("Twitter handed us an empty result."), 404);
            } else {
                processTwitterSearchResponse(twitterSearch, resultBody, clientState);
            }
            tx.success();
            return twitterSearch;
        } catch (IOException e) {
            throw new WebApplicationException(new RuntimeException("HTTP I/O Error", e));
        } finally {
            tx.finish();
        }
    }



    /** Private Helper Methods */

    private void processTwitterSearchResponse(Topic twitterSearch, StringBuffer resultBody, ClientState clientState) {
        DeepaMehtaTransaction tx = dms.beginTx();
        try {
            //
            JSONObject results = new JSONObject(resultBody.toString());
            // add or reference all new tweets and new twitter user accounts
            for (int i=0; i < results.getJSONArray("results").length(); i++) {
                JSONObject item = results.getJSONArray("results").getJSONObject(i);
                // gets an existing or creates a new "Twitter User"-Topic
                String userName = "", twitterUserId = "", profileImageUrl = "";
                if (item.has("from_user")) userName = item.getString("from_user");
                if (item.has("from_user_id_str")) twitterUserId = item.getString("from_user_id_str");
                if (item.has("profile_image_url")) profileImageUrl = item.getString("profile_image_url");
                Topic twitterUser = getTwitterUser(twitterUserId, userName, profileImageUrl, clientState);
                // gets an existing or creates a new "Tweet"-Topic
                Topic tweet = getTweet(item, twitterUser.getId(), clientState);
                // associate "Tweet" with "Twitter User" fixme: check if there's already an association
                twitterSearch.setCompositeValue(new CompositeValueModel().addRef(TWEET_URI, tweet.getId()),
                        clientState, new Directives());
                // old style association
                /* dms.createAssociation(new AssociationModel(SEARCH_RESULT,
                        new TopicRoleModel(searchId, DEFAULT_URI),
                        new TopicRoleModel(tweet.getId(), DEFAULT_URI)), clientState); **/
            }

            // get current (overall) result size
            int size = twitterSearch.getRelatedTopics(AGGREGATION, PARENT_URI, CHILD_URI, TWEET_URI, false, false,
                    0, clientState).getTotalCount();
            // update our "Twitter Search"-Topic to reflect results after latest query
            String nextPage = "", maxTweetId = "", refreshUrl = "";
            if (results.has("max_id_str")) maxTweetId = results.getString("max_id_str");
            if (results.has("next_page")) nextPage = results.getString("next_page");
            if (results.has("refresh_url")) refreshUrl = results.getString("refresh_url");

            twitterSearch.getCompositeValue().set(TWITTER_SEARCH_NEXT_PAGE_URI,
                    nextPage, clientState, new Directives());
            twitterSearch.getCompositeValue().set(TWITTER_SEARCH_RESULT_SIZE_URI, size,
                    clientState, new Directives());
            twitterSearch.getCompositeValue().set(TWITTER_SEARCH_TIME_URI, new Date().getTime(), clientState,
                    new Directives());
            twitterSearch.getCompositeValue().set(TWITTER_SEARCH_MAX_TWEET_URI,
                    maxTweetId, clientState, new Directives());
            twitterSearch.getCompositeValue().set(TWITTER_SEARCH_REFRESH_URL_URI,
                    refreshUrl, clientState, new Directives());
            tx.success();
        } catch (JSONException e) {
            throw new WebApplicationException(new RuntimeException("We could not parse the response properly."
                + e.getMessage()), 500);
        } finally {
            tx.finish();
        }
    }

    private Topic getTweet(JSONObject item, long userTopicId, ClientState clientState) {
        Topic tweet = null;
        DeepaMehtaTransaction tx = dms.beginTx();
        try {
            Topic tweetId = dms.getTopic(TWEET_ID_URI, new SimpleValue(item.getString("id_str")), true, clientState);
            if (tweetId != null) {
                tweet = tweetId.getRelatedTopic(COMPOSITION, CHILD_URI, PARENT_URI, TWEET_URI,
                        true, false, clientState);
            } else {
                tweet = createTweet(item, userTopicId, clientState);
            }
            tx.success();
        } catch (Exception e) {
            throw new WebApplicationException(new RuntimeException("We could neither fetch nor create a Tweet."
                    + e.getMessage()), 500);
        } finally {
            tx.finish();
        }
        return tweet;
    }

    private Topic createTweet(JSONObject item, long userTopicId, ClientState clientState) {
        Topic tweet = null;
        DeepaMehtaTransaction tx = dms.beginTx();
        try {
            // find twitter's doc on a tweets fields (https://dev.twitter.com/docs/platform-objects/tweets)
            TopicModel topic = new TopicModel(TWEET_URI);
            String coordinates = "", inReplyToStatus = "", withheldCopyright = "",
                    withheldInCountries = "", withheldScope = "";
            int favourite_count = 0;
            if (item.has("coordinates")) coordinates = item.getJSONObject("coordinates").toString();
            if (item.has("in_reply_to_status_id_str")) inReplyToStatus = item.getString("in_reply_to_status_id_str");
            if (item.has("withheld_copyright")) withheldCopyright = item.getString("withheld_copyright");
            if (item.has("withheld_in_countries")) withheldInCountries = item.getJSONArray("withheld_in_countries")
                    .toString();
            if (item.has("withheld_scope")) withheldScope = item.getString("withheld_scope");
            if (item.has("favourite_count")) favourite_count = item.getInt("favourites_count");
            CompositeValueModel content = new CompositeValueModel()
                .put(TWEET_CONTENT_URI, item.getString("text"))
                .put(TWEET_TIME_URI, item.getString("created_at")) // is utc-time
                .put(TWEET_ID_URI, item.getString("id_str"))
                .put(TWEET_ENTITIES_URI, item.getJSONObject("entities").toString()) // is application-json/text
                .put(TWEET_METADATA_URI, item.getJSONObject("metadata").toString()) // is application-json/text
                .put(TWEET_LOCATION_URI, coordinates) // is application-json/text
                .put(TWEET_FAVOURITE_COUNT_URI, favourite_count)
                .put(TWEETED_TO_STATUS_ID, inReplyToStatus)
                .put(TWEET_WITHHELD_DMCA_URI, withheldCopyright) // a boolean indicating dmca-request
                .put(TWEET_WITHHELD_IN_URI, withheldInCountries) // is application-json/text (array)
                .put(TWEET_WITHHELD_SCOPE_URI, withheldScope) // is application-json/text (array)
                .put(TWEET_SOURCE_BUTTON_URI, item.getString("source"))
                .putRef(TWITTER_USER_URI, userTopicId);
            topic.setCompositeValue(content);
            tweet = dms.createTopic(topic, clientState);
            tx.success();
        } catch (JSONException jex) {
            log.info(jex.getMessage());
        } finally {
            tx.finish();
        }
        return tweet;
    }

    private Topic getTwitterUser(String userId, String userName, String userImageUrl, ClientState clientState) {
        Topic identity = null;
        DeepaMehtaTransaction tx = dms.beginTx();
        try {
            if (userId == null) throw new RuntimeException("Search Result is invalid. Missing Twitter-User-Id");
            Topic twitterId = dms.getTopic(TWITTER_USER_ID_URI, new SimpleValue(userId), true, clientState);
            if (twitterId != null) {
                identity = twitterId.getRelatedTopic(COMPOSITION, CHILD_URI, PARENT_URI, TWITTER_USER_URI,
                        true, false, clientState);
            } else {
                identity = createTwitterUser(userId, userName, userImageUrl, clientState);
            }
            tx.success();
        } catch (Exception ex) {
            log.info("Crashed query for twitter-id topic, trying to create new Twitter User Topic");
            log.info(ex.getMessage());
        } finally {
            tx.finish();
        }
        return identity;
    }

    private Topic createTwitterUser(String userId, String userName, String userImageUrl, ClientState clientState) {
        TopicModel twitterUser = new TopicModel(TWITTER_USER_URI);
        twitterUser.setCompositeValue(new CompositeValueModel()
                .put(TWITTER_USER_ID_URI, userId)
                .put(TWITTER_USER_NAME_URI, userName)
                .put(TWITTER_USER_IMAGE_URI, userImageUrl));
        return dms.createTopic(twitterUser, clientState);
    }

}
