package org.deepamehta.plugins.twitter.service;

import de.deepamehta.core.Topic;
import de.deepamehta.core.RelatedTopic;
import de.deepamehta.core.model.TopicModel;
import de.deepamehta.core.service.ClientState;
import de.deepamehta.core.service.PluginService;
import java.util.Set;

/**
 * A basic service for the public Twitter Search API and DeepaMehta 4.
 *
 * @version 1.0
 * @author Malte Reißig (<malte@mikromedia.de>)
 * @website http://github.com/mukil
 *
 */
public interface TwitterService extends PluginService {

    Topic searchPublicTweets(long searchTopicId, String query, int querySize, String resultType,
            String lang, String location, ClientState clientstate);

    Topic searchMoreTweets(long searchTopicId, boolean fetchMore, ClientState clientstate);

}
