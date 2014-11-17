
# DeepaMehta 4 Twitter Research Plugin

This plugin allows users of DeepaMehta to [query](https://dev.twitter.com/docs/using-search) [Twitters Public Search API v1.1.](https://dev.twitter.com/docs/api/1.1/get/search/tweets) and start qualitative research around events based on the messages sent by users of twitter.com. This plugin basically wraps all meaningful options of the API (e.g. Language, Location and Query-Input). The "Twitter Search"-Topic enables to "Load More Tweets" (paging) and to "Refresh Search" loading new tweets (since last query).


## Download & Installation

You can find the latest bundle-file to install at [http://download.deepamehta.de/nightly](http://download.deepamehta.de/nightly/).

Copy the downloaded `twitter-research-1.X.jar` file into your DeepaMehta bundle repository and re-start your [DeepaMehta installation](https://github.com/jri/deepamehta#requirements).


## Usage

The Twitter Research Plugin has to rely on [Twitter Application-only authentication](https://dev.twitter.com/docs/auth/application-only-auth), which makes it necessary that you register your application scenario ([where this thread might prove helpful](https://dev.twitter.com/discussions/631)) and hand over the consumer key and secret to your plugin installation. After being logged-in/registered at twitter.com you can do so [here](https://apps.twitter.com/app/new).

You must do so by revealing and editing 2 specific topics. You find those two topics via the DeepaMehta Toolbar and clicking (1) "Search" -> "By Type" -> "Twitter Secret" and (2) "Search" -> "By Type" -> "Twitter Key" and then revealing the respective (and only) search results for each of the searches. 

When having selected a "Twitter Key" press "Edit" (in the Detail Panel) and enter your registered Twitter applications "Consumer Key". When having selected a "Twitter Secret" press "Edit" (in the Detail Panel) and enter your registered Twitter applications "Consumer Secret".

The [Twitters Public Search API v1.1.](https://dev.twitter.com/docs/api/1.1/get/search/tweets) makes available most (not all) tweeted contents from within the last 6-9 days. Once queried all tweets can be accessed through DeepaMehtas fulltext search (and usage of the lucene-search commands).

The plugin currently does not comply to the [Twitter Developer Display Requirements](https://dev.twitter.com/terms/display-requirements), so please make sure to comply with those before making your plugin available to others online (as Twitter Inc. then may want argue with you about your interface design).


## Functionality

This plugin currently does not support User-based authentication to twitter.com but instead is build to work with OAuth2 [Application-only authentication](https://dev.twitter.com/docs/auth/application-only-auth). From the docs: "Twitter's implementation is based on the [Client Credentials Grant](http://tools.ietf.org/html/rfc6749#section-4.4) flow of the [OAuth 2 specification](http://tools.ietf.org/html/rfc6749). Note that OAuth 1.0a is still required to issue requests on behalf of users."

A "Twitter Search" can be configured to be restricted to a certain "geolocation" and/or set to be restricted to a certain "language" in which the tweets (seem) to be written in (for twitter.com).

The "Twitter Search"-Topic enables to "Load More Tweets" (paging) and to "Refresh Search" loading new tweets (since last query).

The plugin extracts "Twitter Users" uniquely and thus associates all tweets found across various queries with the correct twitter.com user.

Around "Tweets" this plugin makes avaible some of the information provided by twitter through their API which seems invisible in their web-interface, namely "Tweet Withheld in Countries" and the "Copyright Withheld"-Flag (while both fields are just rendered _if_ values are set).

This plugin currently ignores the following fields of informaton on ["Tweets"](https://dev.twitter.com/docs/platform-objects/tweets): Annotations, Contributors, Current User Retweet, Filter Level, In reply to, Language, Possibly sensitive, Scopes, Retweet(ed) Count.



## GNU Public License

This DeepaMehta plugin is released under the terms of the GNU General Public License in Version 3.0, 2007. You can find a copy of that in the root directory of this repository or read it [here](http://www.gnu.org/licenses/gpl).


## Developer Information

Accessing [Twitter's Public Search API](https://dev.twitter.com/docs/using-search) with [DeepaMehta 4](http://deepamehta.de) supporting public research on recently tweeted topics.

This plugin is realized as a part of the [Deepamehta Webclient](https://github.com/jri/deepamehta) and was made to explore much of the webclients own GUI Framework, thus providing working examples for _Simple Renderer_, _Multi Renderer_ and _Custom Commands_ in DeepaMehta 4.


## Further Development

Noteworthy is that the Twitter Public Search API enables us to easily extract to URLs and Hashtags from Tweets (via the "Entities"-Field), thus making a potential tighter integration of this Twitter Search Plugin with the DeepaMehta Webbrowser or the DeepaMehta Tags Module (at some time point in the future) appear seamless for end-users and other applications that build on "Tags" and "Web Resources".


## Icons License

The "Twitter OAuth" icon used in this application comes from here [Twitter OAuth  Application Icon](https://abs.twimg.com/a/1378701295/images/oauth_application.png) and it's license remains unclear (since the publishers did not mention anyone).

The "Twitter Search" icon used in this application is licensed under a [Creative Commons (Attribution 3.0 Unported)](http://creativecommons.org/licenses/by/3.0/) and was designed by Aha-Soft.

The "Tweet" icon used in this application is "Free for commercial user" and was designed by [Andy Gongea](http://www.graphicrating.com/).

The "Twitter User" icon used in this application is "Free for commercial use" and was designed by "Gasper Vidovic s.p.".


## History

**1.3.3**, Upcoming
- Compatible with DeepaMehta 4.4

**1.3.2**, Nov 17, 2014 
- Compatible with DeepaMehta 4.3

**1.3.1**, Mar 08 2014
- Release compatible with DM 4.2

**1.3.0**, Mar 08 2014
- Upgraded to be compatible with DM 4.1.3

**1.2**, Sep 24, 2013

- implemented a Client Credentials Grant of OAuth2
- upgraded to be compatible Twitters REST Search API v1.1
- upgraded to be compatible with DeepaMehta 4.1.2
- improved error handling
- slightly improved changed look & feel

Release name: "Unsatisfied with the look & feel (as always) but functional."

**1.1**, May 27, 2013

- fixing ACL-installation issues

**1.0**, May 25, 2013

- initial release of this plugin

1.0-SNAPSHOT, May 19, 2013

- initialization of this plugin

-------------------------------
Author: Malte Reißig, 2013-2014

