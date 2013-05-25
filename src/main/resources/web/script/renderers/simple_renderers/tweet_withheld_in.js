/** A simple renderer hiding and returning the server-side set value on the given topic. */
dm4c.add_simple_renderer('org.deepamehta.twitter.tweet_withheld_in_field', {

    render_info: function (model, $parent) {
        // values are two-letter country codes as visible on http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
        // twitter-documentation see https://dev.twitter.com/docs/platform-objects/tweets
        if (model.object.value != undefined && model.object.value !== "") {
            $parent.append('<div class="field-item tweet">Tweet withheld in ' +model.object.value+ '')
        }
    },

    render_form: function (model, $parent) {
        // $parent.append('<div class="field-item tweet">' +model.object.value+ '</div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }
})
