dm4c.add_simple_renderer('org.deepamehta.twitter.tweet_metadata_field', {

    render_info: function (model, $parent) {
        console.log(model.object.value)
        // var metadata= JSON.parse(model.object.value)
        var retweets = 0 // fixme: this field is "broken"
        /* if (metadata.recent_retweets) retweets = metadata.recent_retweets
        if (retweets > 0) {
            $parent.append('<div class="field-label">Tweet Metadata</div>'
                + '<div class="field-item tweet-entities"><b>' +retweets+ '</b> recent Retweets</div>')
        } **/
    },

    render_form: function (model, $parent) {

        $parent.append('<div class="field-item tweet-entities">' +model.object.value+ '</div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }

})
