/** A simple renderer hiding and returning the server-side set value on the given topic. */
dm4c.add_simple_renderer('org.deepamehta.twitter.tweeted_to_status_id_field', {

    render_info: function (model, $parent) {
        // $parent.append('<div class="field-item tweet"><b>' +model.object.value+ '</b>')
    },

    render_form: function (model, $parent) {
        // $parent.append('<div class="field-item tweet">' +model.object.value+ '</div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }
})
