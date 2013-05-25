dm4c.add_simple_renderer('org.deepamehta.twitter.search_type_field', {

    render_info: function (model, $parent) {
        
        $parent.append('<div class="field-label">Twitter Search Type</div>'
                + '<div class="twitter-query-type">' +model.object.value+ '</div>')
    },

    render_form: function (model, $parent) {

        var value = model.object.value
        if (value === "") value = "mixed"
        $parent.html('<div class="field-label">Twitter Search Type (mixed, recent, popular)</div>'
            +'<input type="text" class="search-type" value="' +value+ '"></input>')

        return function () {
            var query = $("input.search-type").val()
            if (typeof query === undefined) return "mixed"
            return query // set dummy field after edit
        }
    }

})
