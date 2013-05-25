/*global jQuery, dm4c*/
(function ($, dm4c) {

    dm4c.add_simple_renderer('org.deepamehta.twitter.search_query_size_field', {

        render_info: function (model, $parent) {

            var value = 100

            $parent.append('<div class="field-label">Max. Number of Query Results (0-100)</div>'
                + '<div class="twitter-query-size">' +value+ '</div>')
        },

        render_form: function (model, $parent) {

            var value = 100

            $parent.html('<div class="field-label">Max. Number of Search Results (0-100)</div>'
                + '<input type="text" class="search-max-size" value="' +value+ '"></input>')

            return function () {
                var querySize = parseInt($("input.search-max-size").val())
                if (querySize === "NaN" || typeof querySize === undefined) {
                    console.log("Max. Number of Search Results is not a number. Using 100.")
                    return 100
                }
                return querySize // set dummy field after edit
            }
        }
    })

}(jQuery, dm4c))
