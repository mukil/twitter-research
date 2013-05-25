/*global jQuery, dm4c*/
(function ($, dm4c) {

    dm4c.add_simple_renderer('org.deepamehta.twitter.search_field', {

        render_info: function (model, $parent) {
            $parent.append('<div class="field-label">Twitter Query</div>'
                + '<div class="twitter-search-query">' +model.object.value+ '</div>')
        },

        render_form: function (model, $parent) {
            $parent.html('<div class="field-label">Twitter Search Query '
                + '<a href="https://dev.twitter.com/docs/using-search" target="_blank" title="How to us twitters '
                + ' search: see section on Search Operators">(Help-Page)</a></div>'
                + '<input type="text" class="search-query" value="' +model.object.value+ '"></input>')

            return function () {
                var query = $("input.search-query").val()
                if (typeof query === undefined) return ""
                return query // set dummy field after edit
            }
        }
    })

}(jQuery, dm4c))
