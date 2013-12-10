package org.deepamehta.plugins.twitter.service;

import de.deepamehta.core.Topic;
import de.deepamehta.core.service.ClientState;
import de.deepamehta.core.service.PluginService;

/**
 * A basic service for the public Twitter Search API and DeepaMehta 4.
 *
 * @author Malte Reißig (<malte@mikromedia.de>)
 * @version 1.3.0-SNAPSHOT
 * @website https://github.com/mukil/twitter-research
 *
 */

public interface TwitterService extends PluginService {

    Topic searchPublicTweets(long searchTopicId, String query, String resultType,
            String lang, String location, ClientState clientstate);

    Topic searchMoreTweets(long searchTopicId, boolean fetchMore, ClientState clientstate);

}
