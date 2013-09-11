
dm4c.add_simple_renderer('org.deepamehta.twitter.tweet_location_field', {

    /** A simple renderer hiding and returning the server-side set value on the given topic. */

    render_info: function (model, $parent) {
        $parent.append('<div class="field-item tweet"><b>' +model.object.value+ '</b>')
    },

    render_form: function (model, $parent) {

        // $parent.append('<div class="field-item tweet">' +model.object.value+ '</div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }
})
