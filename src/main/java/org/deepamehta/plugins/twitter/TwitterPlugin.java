package org.deepamehta.plugins.twitter;

import com.sun.jersey.core.util.Base64;
import de.deepamehta.core.RelatedTopic;
import de.deepamehta.core.Topic;
import de.deepamehta.core.model.ChildTopicsModel;
import de.deepamehta.core.model.SimpleValue;
import de.deepamehta.core.model.TopicModel;
import de.deepamehta.core.osgi.PluginActivator;
import de.deepamehta.core.service.Inject;
import de.deepamehta.core.service.ResultList;
import de.deepamehta.core.service.Transactional;
import de.deepamehta.core.storage.spi.DeepaMehtaTransaction;
import de.deepamehta.plugins.accesscontrol.model.ACLEntry;
import de.deepamehta.plugins.accesscontrol.model.AccessControlList;
import de.deepamehta.plugins.accesscontrol.model.Operation;
import de.deepamehta.plugins.accesscontrol.model.UserRole;
import de.deepamehta.plugins.accesscontrol.service.AccessControlService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.deepamehta.plugins.twitter.service.TwitterService;

/**
 * A very basic client for researching the public Twitter Search API v1.1 with DeepaMehta 4.
 *
 * @author Malte Reißig (<malte@mikromedia.de>)
 * @version 1.3.3-SNAPSHOT
 * @website https://github.com/mukil/twitter-research
 *
 */

@Path("/tweet")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TwitterPlugin extends PluginActivator implements TwitterService {

    private Logger log = Logger.getLogger(getClass().getName());

    private final String DEEPAMEHTA_VERSION = "DeepaMehta 4.4";
    private final String TWITTER_RESEARCH_VERSION = "1.3.3-SNAPSHOT";
    private final String CHARSET = "UTF-8";

    private final static String CHILD_URI = "dm4.core.child";
    private final static String PARENT_URI = "dm4.core.parent";
    private final static String AGGREGATION = "dm4.core.aggregation";
    private final static String COMPOSITION = "dm4.core.composition";

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
    private final static String TWITTER_SEARCH_LANG_URI = "org.deepamehta.twitter.search_language";
    private final static String TWITTER_SEARCH_LOCATION_URI = "org.deepamehta.twitter.search_location";
    private final static String TWITTER_SEARCH_TYPE_URI = "org.deepamehta.twitter.search_result_type";
    private final static String TWITTER_SEARCH_NEXT_PAGE_URI = "org.deepamehta.twitter.search_next_page";
    private final static String TWITTER_SEARCH_REFRESH_URL_URI = "org.deepamehta.twitter.search_refresh_url";
    private final static String TWITTER_SEARCH_MAX_TWEET_URI = "org.deepamehta.twitter.search_last_tweet_id";
    private final static String TWITTER_SEARCH_RESULT_SIZE_URI = "org.deepamehta.twitter.search_result_size";
    private final static String TWITTER_SEARCH_TIME_URI = "org.deepamehta.twitter.last_search_time";

    private final static String TWITTER_AUTHENTICATION_URL = "https://api.twitter.com/oauth2/token";
    private final static String TWITTER_SEARCH_BASE_URL = "https://api.twitter.com/1.1/search/tweets.json";
    
    private final static String TWITTER_APPLICATION_KEY = "org.deepamehta.twitter.application_key";
    private final static String TWITTER_APPLICATION_SECRET = "org.deepamehta.twitter.application_secret";

    private final String GEO_COORDINATE_TOPIC_URI = "dm4.geomaps.geo_coordinate";
    private final String GEO_LONGITUDE_TYPE_URI = "dm4.geomaps.longitude";
    private final String GEO_LATITUDE_TYPE_URI = "dm4.geomaps.latitude";

    private boolean isInitialized = false;
    private boolean isAuthorized = false;
    private String bearerToken = null;
    
    @Inject
    private AccessControlService acService = null;



    /** Initialize the migrated soundsets ACL-Entries. */
    public void init() {
        isInitialized = true;
        configureIfReady();
    }

    private void configureIfReady() {
        if (isInitialized) {
            checkACLsOfMigration();
        }
    }

    private void authorizeSearchRequests () throws TwitterAPIException {
        Topic applicationKey  = dms.getTopic("uri", new SimpleValue(TWITTER_APPLICATION_KEY));
        Topic applicationSecret  = dms.getTopic("uri", new SimpleValue(TWITTER_APPLICATION_SECRET));
        try {
            StringBuilder resultBody = new StringBuilder();
            URL requestUri = new URL(TWITTER_AUTHENTICATION_URL);
            //
            String key = URLEncoder.encode(applicationKey.getSimpleValue().toString(), CHARSET);
            String secret = URLEncoder.encode(applicationSecret.getSimpleValue().toString(), CHARSET);
            // get base64 encoded secrets
            if (key.isEmpty() || secret.isEmpty()) {
                throw new TwitterAPIException("Bad Twitter secrets, please register your application.",
                        Status.UNAUTHORIZED);
            }
            String values =  key + ":" + secret;
            String credentials = new String(Base64.encode(values));
            // initiate request
            HttpURLConnection connection = (HttpURLConnection) requestUri.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "DeepaMehta "+DEEPAMEHTA_VERSION+" - "
                    + "Twitter Research " + TWITTER_RESEARCH_VERSION);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET);
            connection.setRequestProperty("Authorization", "Basic " + credentials);
            //
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            String parameters = "grant_type=client_credentials";
            writer.write(parameters);
            writer.flush();
            //
            int httpStatusCode = connection.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new TwitterAPIException("Error with HTTPConnection.", Status.INTERNAL_SERVER_ERROR);
            }
            // read in the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
            for (String input; (input = rd.readLine()) != null;) {
                resultBody.append(input);
            }
            rd.close();
            writer.close();
            // TODO: Check if answer is something like "403: Too many requests"
            if (resultBody.toString().isEmpty()) {
                throw new TwitterAPIException("Twitter just handed us an empty response ("+httpStatusCode+")",
                        Status.NO_CONTENT);
            }
            //
            JSONObject response = new JSONObject(resultBody.toString());
            bearerToken = response.getString("access_token");
            isAuthorized = true;
        } catch (JSONException ex) {
            throw new RuntimeException("Internal Server Error while parsing response " + ex.getMessage());
        } catch (IOException ex) {
            throw new RuntimeException("Internal Server Error HTTP I/O Error " + ex.getMessage());
        }
    }

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
    @Override
    @Transactional
    public Topic searchMoreTweets(@PathParam("id") long searchId, @PathParam("nextPage") boolean nextPage) {

        Topic query = dms.getTopic(searchId);
        StringBuffer resultBody = new StringBuffer();
        URL requestUri = null;
        try {
            // 0) Authorize request
            if (!isAuthorized) {
                authorizeSearchRequests();
                if (!isAuthorized) {  // check if authorization was sucessfull
                    throw new WebApplicationException(new Throwable("Bad Twitter Secrets. "
                        + "Consider to register your application at https://dev.twitter.com/apps/new."), 500);
                }
            }
            log.fine("Researching tweets for Twitter-Search (" +query.getId()+ ") next ? " + nextPage);
            // 1) loading search configuration
            if (nextPage) {
                // paging to next-page (query for older-tweets)
                String nextPageUrl = query.getChildTopics().getString(TWITTER_SEARCH_NEXT_PAGE_URI);
                if (nextPageUrl.isEmpty()) throw new RuntimeException("There is no next page. (204)");
                log.fine("Loading next page of tweets => " + TWITTER_SEARCH_BASE_URL + nextPageUrl);
                requestUri = new URL(TWITTER_SEARCH_BASE_URL + nextPageUrl);
            } else {
                // refreshing (query for new-tweets)
                String refreshPageUrl = query.getChildTopics().getString(TWITTER_SEARCH_REFRESH_URL_URI);
                requestUri = new URL(TWITTER_SEARCH_BASE_URL + refreshPageUrl);
                log.fine("Loading more recent tweets => " + TWITTER_SEARCH_BASE_URL + refreshPageUrl);
            }
            log.fine("Requesting => " + requestUri.toString());
            // 2) initiate request
            HttpURLConnection connection = (HttpURLConnection) requestUri.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "DeepaMehta "+DEEPAMEHTA_VERSION+" - "
                    + "Twitter Research " + TWITTER_RESEARCH_VERSION);
            connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            // 3) check the response
            int httpStatusCode = connection.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new WebApplicationException(new Throwable("Error with HTTPConnection."),
                        Status.INTERNAL_SERVER_ERROR);
            }
            // 4) read in the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
            for (String input; (input = rd.readLine()) != null;) {
                resultBody.append(input);
            }
            rd.close();
            // 5) process response // TODO: Check if answer is something like "403: Too many requests"
            if (resultBody.toString().isEmpty()) {
                throw new WebApplicationException(new RuntimeException("Twitter handed just us an empty response."),
                        Status.NO_CONTENT);
            } else {
                processTwitterSearchResponse(query, resultBody);
            }
            // update modification timestamp on parent (composite) topic 
            // to invalidate http caching (see also #510)
            dms.updateTopic(new TopicModel(query.getId(), query.getChildTopics().getModel()));
            query.loadChildTopics(); // load all child topics
        } catch (TwitterAPIException ex) {
            log.warning("TwitterApiException " + ex.getMessage());
            throw new WebApplicationException(new Throwable(ex.getMessage()), ex.getStatus());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not trigger existing search-topic.", e);
        } catch (IOException ioe) {
            throw new WebApplicationException(new Throwable("Most probably we made a mistake in constructing the query. "
                    + "We're sorry, please try again."), Status.BAD_REQUEST);
        }
        return query;
    }



    /**
     * Fetches public tweets matching the given <code>query</code>, maintains a search-query topic and
     * references existing tweets and users, as it should be.
     *
     * @param {id}          Twitter Search Topic Id
     * @param {resultType}  "mixed", "recent", "popular"
     * @param {lang}        ISO-639-1 Code (2 chars) (optional)
     * @param {location}    "lat,lng,radiuskm" (optional)
     */

    @GET
    @Path("/search/public/{id}/{query}/{resultType}/{lang}/{location}")
    @Override
    @Transactional
    public Topic searchPublicTweets(@PathParam("id") long searchId, @PathParam("query") String query,
            @PathParam("resultType") String resultType, @PathParam("lang") String lang,
            @PathParam("location") String location) {

        StringBuffer resultBody = new StringBuffer();
        try {
            // 0) Authorize request
            if (!isAuthorized) {
                authorizeSearchRequests();
                if (!isAuthorized) {  // check if authorization was sucessfull
                    throw new WebApplicationException(new Throwable("Bad Twitter Secrets. "
                        + "Consider to register your application at https://dev.twitter.com/apps/new."), 500);
                }
            }
            // 1) setup search container
            Topic twitterSearch = dms.getTopic(searchId);
            log.fine("Resarching Public Tweets " +query+ " (" +resultType+ ") "
                    + "in language: " + lang + " near loc: " + location);
            // 2) construct search query
            String queryUrl = TWITTER_SEARCH_BASE_URL + "?q=" + URLEncoder.encode(query.toString(), CHARSET)
                    + ";&include_entities=true;&result_type=" + resultType + ";"; // ;&rpp=" + querySize + "
            if (lang == null) lang = "";
            if (location == null) location = "";
            if (!lang.isEmpty() && !lang.equals("unspecified")) queryUrl += "&lang="+lang+";";
            if (!location.isEmpty() && !location.equals("none")) queryUrl += "&geocode="+location+";";
            URL requestUri = new URL(queryUrl);
            // 3) initiate request
            HttpURLConnection connection = (HttpURLConnection) requestUri.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "DeepaMehta "+DEEPAMEHTA_VERSION+" - "
                    + "Twitter Research " + TWITTER_RESEARCH_VERSION);
            connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            // 4) check the response
            int httpStatusCode = connection.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new WebApplicationException(new Throwable("Error with HTTPConnection."),
                        Status.INTERNAL_SERVER_ERROR);
            }
            // 5) process response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
            for (String input; (input = rd.readLine()) != null;) {
                resultBody.append(input);
            }
            rd.close();
            if (resultBody.toString().isEmpty()) {
                throw new WebApplicationException(new RuntimeException("Twitter just handed us an empty response."),
                        Status.NO_CONTENT);
            } else {
                processTwitterSearchResponse(twitterSearch, resultBody);
            }
            // looks like https://trac.deepamehta.de/ticket/510 is not yet solved 
            // update modification timestamp on parent (composite) topic to invalidate http caching
            dms.updateTopic(new TopicModel(twitterSearch.getId()));
            twitterSearch.loadChildTopics(); // load all child topics
            return twitterSearch;
        } catch (TwitterAPIException ex) {
            throw new WebApplicationException(new Throwable(ex.getMessage()), ex.getStatus());
        } catch (IOException e) {
            throw new WebApplicationException(new RuntimeException("HTTP I/O Error", e));
        }
    }



    /** Private Helper Methods */

    private void processTwitterSearchResponse(Topic twitterSearch, StringBuffer resultBody) {
        try {
            //
            JSONObject results = new JSONObject(resultBody.toString());
            // add or reference all new tweets and new twitter user accounts
            for (int i=0; i < results.getJSONArray("statuses").length(); i++) {
                JSONObject item = results.getJSONArray("statuses").getJSONObject(i);
                // gets an existing or creates a new "Twitter User"-Topic
                JSONObject user = item.getJSONObject("user");
                String userName = "", twitterUserId = "", profileImageUrl = "";
                if (user.has("name")) userName = user.getString("name");
                if (user.has("id_str")) twitterUserId = user.getString("id_str");
                if (user.has("profile_image_url")) profileImageUrl = user.getString("profile_image_url");

                Topic twitterUser = getTwitterUser(twitterUserId, userName, profileImageUrl);
                // gets an existing or creates a new "Tweet"-Topic
                Topic tweet = getTweet(item, twitterUser.getId());
                // associate "Tweet" with "Twitter User" fixme: check if there's already an association
                twitterSearch.setChildTopics(new ChildTopicsModel().addRef(TWEET_URI, tweet.getId()));
                // old style association
                /* dms.createAssociation(new AssociationModel(SEARCH_RESULT,
                        new TopicRoleModel(searchId, DEFAULT_URI),
                        new TopicRoleModel(tweet.getId(), DEFAULT_URI)), clientState); **/
            }

            // get current (overall) result size
            int size = twitterSearch.getRelatedTopics(AGGREGATION, PARENT_URI, CHILD_URI, TWEET_URI, 0)
                    .getTotalCount();
            // update our "Twitter Search"-Topic to reflect results after latest query
            String nextPage = "", maxTweetId = "", refreshUrl = "";
            JSONObject search_metadata;
            if (results.has("search_metadata")) {
                search_metadata = results.getJSONObject("search_metadata");
                //
                if (search_metadata.has("max_id_str")) maxTweetId = search_metadata.getString("max_id_str");
                if (search_metadata.has("next_results")) nextPage = search_metadata.getString("next_results");
                if (search_metadata.has("refresh_url")) refreshUrl = search_metadata.getString("refresh_url");
            }
            // update search cointainer
            twitterSearch.getChildTopics().set(TWITTER_SEARCH_NEXT_PAGE_URI, nextPage);
            twitterSearch.getChildTopics().set(TWITTER_SEARCH_RESULT_SIZE_URI, size);
            twitterSearch.getChildTopics().set(TWITTER_SEARCH_TIME_URI, new Date().getTime());
            twitterSearch.getChildTopics().set(TWITTER_SEARCH_MAX_TWEET_URI, maxTweetId);
            twitterSearch.getChildTopics().set(TWITTER_SEARCH_REFRESH_URL_URI, refreshUrl);
        } catch (JSONException e) {
            log.warning("ERROR: We could not parse the response properly " + e.getMessage());
            throw new RuntimeException("We could not parse the response properly." + e.getMessage());
        }
    }

    private Topic getTweet(JSONObject item, long userTopicId) {
        Topic tweet = null;
        DeepaMehtaTransaction tx = dms.beginTx();
        try {
            Topic tweetId = dms.getTopic(TWEET_ID_URI, new SimpleValue(item.getString("id_str")));
            if (tweetId != null) {
                tweet = tweetId.getRelatedTopic(COMPOSITION, CHILD_URI, PARENT_URI, TWEET_URI);
            } else {
                tweet = createTweet(item, userTopicId);
            }
            tweet.loadChildTopics(); // load all child topics
            tx.success();
        } catch (Exception e) {
            throw new RuntimeException("We could neither fetch nor create this \"Tweet\".");
        } finally {
            tx.finish();
        }
        return tweet;
    }

    private Topic createTweet(JSONObject item, long userTopicId) {
        Topic tweet = null;
        try {
            // find twitter's doc on a tweets fields (https://dev.twitter.com/docs/platform-objects/tweets)
            TopicModel topic = new TopicModel(TWEET_URI);
            TopicModel coordinate = null;
            String inReplyToStatus = "", withheldCopyright = "",
                    withheldInCountries = "", withheldScope = "", metadata = "", entities = "";
            int favourite_count = 0;
            if (item.has("place") && !item.isNull("place")) {
                JSONObject place = item.getJSONObject("place");
                if (place.has("bounding_box")) {
                    JSONObject box = place.getJSONObject("bounding_box");
                    JSONArray tudes = box.getJSONArray("coordinates");
                    // first value in array is always longitude
                    JSONArray container = tudes.getJSONArray(0);
                    JSONArray lower_left = container.getJSONArray(0);
                    JSONArray lower_right = container.getJSONArray(1);
                    JSONArray upper_right = container.getJSONArray(2);
                    JSONArray upper_left = container.getJSONArray(3);
                    double lng = (upper_left.getDouble(0) + upper_right.getDouble(0) + lower_left.getDouble(0) + lower_right.getDouble(0)) / 4;
                    double lat = (upper_left.getDouble(1) + upper_right.getDouble(1) + lower_left.getDouble(1) + lower_right.getDouble(1)) / 4;
                    coordinate = createGeoCoordinateTopicModel(lng, lat);
                }
            }
            // check for and create coordinates (simply overwrites place if given)
            if (item.has("coordinates") && !item.isNull("coordinates")) {
                JSONObject coordinates = item.getJSONObject("coordinates");
                if (coordinates.has("coordinates")) {
                    JSONArray tudes = coordinates.getJSONArray("coordinates");
                    log.fine("Writing coordinates OVER place..");
                    coordinate = createGeoCoordinateTopicModel(tudes.getDouble(0), tudes.getDouble(1));
                }
            }
            if (item.has("in_reply_to_status_id_str")) inReplyToStatus = item.getString("in_reply_to_status_id_str");
            if (item.has("withheld_copyright")) withheldCopyright = item.getString("withheld_copyright");
            if (item.has("withheld_in_countries")) withheldInCountries = item.getJSONArray("withheld_in_countries")
                    .toString();
            if (item.has("withheld_scope")) withheldScope = item.getString("withheld_scope");
            if (item.has("favorite_count")) favourite_count = item.getInt("favorite_count");
            if (item.has("place") && !item.isNull("place")) metadata = item.getJSONObject("place").toString();
            if (item.has("entities") && !item.isNull("entities")) entities = item.getJSONObject("entities").toString();
            ChildTopicsModel content = new ChildTopicsModel()
                .put(TWEET_CONTENT_URI, item.getString("text"))
                .put(TWEET_TIME_URI, item.getString("created_at")) // is utc-time
                .put(TWEET_ID_URI, item.getString("id_str"))
                .put(TWEET_ENTITIES_URI, entities) // is application-json/text
                .put(TWEET_METADATA_URI, metadata) // is application-json/text
                .put(TWEET_LOCATION_URI, "") // unused, to be removed (?)
                .put(TWEET_FAVOURITE_COUNT_URI, favourite_count)
                .put(TWEETED_TO_STATUS_ID, inReplyToStatus)
                .put(TWEET_WITHHELD_DMCA_URI, withheldCopyright) // a boolean indicating dmca-request
                .put(TWEET_WITHHELD_IN_URI, withheldInCountries) // is application-json/text (array)
                .put(TWEET_WITHHELD_SCOPE_URI, withheldScope) // is application-json/text (array)
                .put(TWEET_SOURCE_BUTTON_URI, item.getString("source"))
                .putRef(TWITTER_USER_URI, userTopicId);
            topic.setChildTopicsModel(content);
            if (coordinate != null) {
                content.put(GEO_COORDINATE_TOPIC_URI, coordinate.getChildTopicsModel());
            }
            tweet = dms.createTopic(topic);
        } catch (JSONException jex) {
            throw new RuntimeException(jex.getMessage());
        }
        return tweet;
    }

    private Topic getTwitterUser(String userId, String userName, String userImageUrl) {
        Topic identity = null;
        DeepaMehtaTransaction tx = dms.beginTx();
        try {
            if (userId == null) throw new RuntimeException("Search Result is invalid. Missing Twitter-User-Id");
            Topic twitterId = dms.getTopic(TWITTER_USER_ID_URI, new SimpleValue(userId));
            if (twitterId != null) {
                identity = twitterId.getRelatedTopic(COMPOSITION, CHILD_URI, PARENT_URI, TWITTER_USER_URI);
            } else {
                identity = createTwitterUser(userId, userName, userImageUrl);
            }
            tx.success();
        } catch (Exception ex) {
            log.info("Crashed query for twitter-id topic, trying to create new Twitter User Topic");
            throw new RuntimeException(ex);
        } finally {
            tx.finish();
        }
        return identity;
    }

    private Topic createTwitterUser(String userId, String userName, String userImageUrl) {
        TopicModel twitterUser = new TopicModel(TWITTER_USER_URI);
        twitterUser.setChildTopicsModel(new ChildTopicsModel()
                .put(TWITTER_USER_ID_URI, userId)
                .put(TWITTER_USER_NAME_URI, userName)
                .put(TWITTER_USER_IMAGE_URI, userImageUrl));
        return dms.createTopic(twitterUser);
    }

    private TopicModel createGeoCoordinateTopicModel(double lng, double lat) {
        TopicModel coordinates = new TopicModel(GEO_COORDINATE_TOPIC_URI);
        ChildTopicsModel model = new ChildTopicsModel();
        model.put(GEO_LONGITUDE_TYPE_URI, lng);
        model.put(GEO_LATITUDE_TYPE_URI, lat);
        coordinates.setChildTopicsModel(model);
        return coordinates;
    }

    /** Code running once, after plugin initialization. */

    private void checkACLsOfMigration() {
        // secrets
        ResultList<RelatedTopic> secrets = dms.getTopics("org.deepamehta.twitter.secret", 0);
        Iterator<RelatedTopic> secs = secrets.iterator();
        while (secs.hasNext()) {
            RelatedTopic secret = secs.next();
            DeepaMehtaTransaction dmx = dms.beginTx();
            try {
                if (acService.getCreator(secret) == null) {
                    log.fine("Running initial ACL update of twitter secret topics " + secret.getSimpleValue().toString());
                    Topic admin = acService.getUsername("admin");
                    String adminName = admin.getSimpleValue().toString();
                    acService.setCreator(secret, adminName);
                    acService.setOwner(secret, adminName);
                    acService.setACL(secret, new AccessControlList( //
                            new ACLEntry(Operation.WRITE, UserRole.OWNER)));
                }
                dmx.success();
            } catch (Exception ex) {
                log.warning(ex.getMessage());
                throw new RuntimeException(ex);
            } finally {
                dmx.finish();
            }
        }
        // keys
        ResultList<RelatedTopic> keys = dms.getTopics("org.deepamehta.twitter.key", 0);
        Iterator<RelatedTopic> ks = keys.iterator();
        while (ks.hasNext()) {
            RelatedTopic key = ks.next();
            DeepaMehtaTransaction dmx = dms.beginTx();
            try {
                if (acService.getCreator(key) == null) {
                    log.fine("Running initial ACL update of twitter key topics " + key.getSimpleValue().toString());
                    Topic admin = acService.getUsername("admin");
                    String adminName = admin.getSimpleValue().toString();
                    acService.setCreator(key, adminName);
                    acService.setOwner(key, adminName);
                    acService.setACL(key, new AccessControlList( //
                            new ACLEntry(Operation.WRITE, UserRole.OWNER)));
                }
                dmx.success();
           } catch (Exception ex) {
                log.warning(ex.getMessage());
                throw new RuntimeException(ex);
            } finally {
                dmx.finish();
            }
        }
    }

}
