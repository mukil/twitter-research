dm4c.add_simple_renderer('org.deepamehta.twitter.user_image_field', {

    render_info: function (model, $parent) {
        $parent.append('<img class="twitter-user-image" src="' +model.object.value+ '">')
    },

    render_form: function (model, $parent) {
        $parent.append('<img class="twitter-user-image" src="' +model.object.value+ '">')

        return function () {
            if (typeof model.object.value === undefined) return ""
            return model.object.value // set dummy field after edit
        }
    }

})
