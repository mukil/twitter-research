/*global jQuery, dm4c*/
(function ($, dm4c) {

    dm4c.add_simple_renderer('org.deepamehta.twitter.search_refresh_field', {

        render_info: function (model, $parent) {

            if (model.object.value === "") return

            $refreshButton = dm4c.ui.button(function(e) {

                var nextOrRefresh = false
                var queryId = dm4c.selected_object.id

                var requestUri = '/tweet/search/public/' + queryId + '/' +nextOrRefresh

                var response_data_type = response_data_type || "json"
                //
                $.ajax({
                    type: "GET", url: requestUri,
                    dataType: response_data_type, processData: false,
                    async: true,
                    success: function(data, text_status, jq_xhr) {
                        dm4c.do_select_topic(data.id, true)
                    },
                    error: function(jq_xhr, text_status, error_thrown) {
                        $('#page-content').html('<div class="field-label twitter-search-started">'
                            + 'An error occured: ' +error_thrown+ ' </div>')
                        throw "RESTClientError: GET request failed (" + text_status + ": "
                            + error_thrown + ")"
                    },
                    complete: function(jq_xhr, text_status) {
                        var status = text_status
                    }
                })

                $('#page-content').html('<div class="field-label twitter-search-started">'
                    + 'Asking https://search.twitter.com ... <br/><br/>'
                    + '</div>')

            }, "Refresh Search")
            $parent.append($refreshButton)
        },

        render_form: function (model, $parent) {

            return function () {

                return model.object.value // just return existing value
            }
        }
    })

}(jQuery, dm4c))
