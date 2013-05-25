dm4c.add_simple_renderer('org.deepamehta.twitter.search_result_size_field', {

    render_info: function (model, $parent) {
        var resultSize = parseInt(model.object.value)
        if (resultSize > 1) {
            $parent.append('<div class="field-label twitter-search-result-size">' +resultSize+ ' results</div>')
        }
    },

    render_form: function (model, $parent) {

        $parent.append('<div class="field-label search-size-empty>')

        return function () {

            return model.object.value // set dummy field after edit
        }
    }

})
