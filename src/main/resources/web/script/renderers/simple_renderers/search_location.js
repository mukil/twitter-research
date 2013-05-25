/*global jQuery, dm4c*/
(function ($, dm4c) {

    dm4c.add_simple_renderer('org.deepamehta.twitter.search_location_field', {

        render_info: function (model, $parent) {
            var locationValue = model.object.value
            $parent.append('<div class="field-label">Search Near Location</div>'
                + '<div class="twitter-search-location">' +locationValue+ '</div>')
        },

        render_form: function (model, $parent) {

            var locationValue = "none"

            if (model.object.value != "") locationValue = model.object.value

            $parent.html('<div class="field-label">Search Near Location<br/>'
                + '<br/>Info: "The parameter value is specified by "latitude,longitude,radius", where radius units must '
                + 'be specified as either "mi" (miles) or "km" (kilometers)."<br/>'
                + 'Example: "37.781157,-122.398720,1mi"<br/><br/></div>'
                + '<input type="text" class="search-location" value="' +locationValue+ '"></input>')

            return function () {
                var query = $("input.search-location").val()
                if (typeof query === undefined) return "none"
                query = encodeURIComponent(query)
                return query // set dummy field after edit
            }
        }
    })

}(jQuery, dm4c))
