/** A simple renderer hiding and returning the server-side set value on the given topic. */
dm4c.add_simple_renderer('org.deepamehta.twitter.tweet_withheld_scope_field', {

    render_info: function (model, $parent) {
        // a value (either "User" or "Status" indicating what is withheld
        if (model.object.value != undefined && model.object.value !== "") {
            $parent.append('<div class="field-item tweet-withheld-scope"><b>' +model.object.value+ '</b>-scope')
        }
    },

    render_form: function (model, $parent) {
        // $parent.append('<div class="field-item tweet">' +model.object.value+ '</div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }
})
