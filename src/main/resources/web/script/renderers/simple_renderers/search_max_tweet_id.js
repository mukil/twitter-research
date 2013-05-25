/** A simple renderer hiding and returning the server-side set value on the given topic. */
dm4c.add_simple_renderer('org.deepamehta.twitter.search_max_tweet_id_field', {

    render_info: function (model, $parent) {
        $parent.append('<div class="field-label search-max-id">')
    },

    render_form: function (model, $parent) {

        return function () {
            return model.object.value // set dummy field after edit
        }
    }
})
