dm4c.add_simple_renderer('org.deepamehta.twitter.tweet_source_field', {

    render_info: function (model, $parent) {
        // $parent.html(model.object.value)
    },

    render_form: function (model, $parent) {
        var pluginResults = {};
        $parent.append('<div class="field-item tweet-id">Placeholder Tweet Source Button</div>')

        return function () {
            return model.object.value // set dummy field after edit
        }
    }

})
