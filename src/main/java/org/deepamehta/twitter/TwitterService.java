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

    Topic searchPublicTweets(long searchTopicId, String query, String resultType,
            String lang, String location);

    Topic searchMoreTweets(long searchTopicId, boolean fetchMore);

}
