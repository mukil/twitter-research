/** A simple renderer hiding and returning the server-side set value on the given topic. */
dm4c.add_simple_renderer('org.deepamehta.twitter.user_id_field', {

    render_info: function (model, $parent) {
        $parent.append('<div id="' +model.object.value+ '" class="field-item twitter-user-id"></div>')
    },

    render_form: function (model, $parent) {
        $parent.append('<div class="field-item twitter-user-id">Hidden User ID Field</div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }

})