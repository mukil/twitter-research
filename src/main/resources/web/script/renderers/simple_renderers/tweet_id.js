dm4c.add_simple_renderer('org.deepamehta.twitter.tweet_id_field', {

    render_info: function (model, $parent) {
        // based on the tweet's id, we could use twitters anonymous oembed-service to just GET a tweet's embed HTML'
        // example request: https://api.twitter.com/1/statuses/oembed.json?id=99530515043983360
        // response: the response.html object "should contain a valid HTML-Embed"
        // see also https://dev.twitter.com/docs/api/1/get/statuses/oembed
        $parent.append('<div id="' +model.object.value+ '" class="field-item tweet-id"></div>')
    },

    render_form: function (model, $parent) {
        var pluginResults = {};
        $parent.append('<div id="' +model.object.value+ '" class="field-item tweet-id"></div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }

})