/** A simple renderer hiding and returning the server-side set value on the given topic. */
dm4c.add_simple_renderer('org.deepamehta.twitter.tweet_withheld_copyright_field', {

    render_info: function (model, $parent) {
        // a boolean indicating "wether this piece of content has been withheld due to a DMCA complaint"
        if (model.object.value) {
            $parent.append('<div class="field-item tweet">This tweet is "withheld due to a DMCA complaint".</div>')
        }
    },

    render_form: function (model, $parent) {
        // $parent.append('<div class="field-item tweet">' +model.object.value+ '</div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }
})
