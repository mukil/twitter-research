/*global jQuery, dm4c*/
(function ($, dm4c) {


    dm4c.add_plugin('org.deepamehta.twitter-search', function () {

        function searchPublicTweets(menu_item, pos_x, pos_y) {

            var query = $('#page-content .twitter-search-query').text()
            var language = $('#page-content .twitter-search-lang').text()
            var location = $('#page-content .twitter-search-location').text()
            var resultType = $('#page-content .twitter-query-type').text()

            if (query === "") return undefined

            if (language === "") language = "unspecified"
            if (location === "") location = "none"
            if (resultType === "") resultType = "recent"

            var requestUri = '/tweet/search/public/' + dm4c.selected_object.id + '/' +encodeURIComponent(query) + '/'
                + resultType + '/' + language + '/' + location

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
                    throw "RESTClientError: GET request failed (" + text_status + ": " + error_thrown + ")"
                },
                complete: function(jq_xhr, text_status) {
                    var status = text_status
                }
            })

            $('#page-content').html('<div class="field-label twitter-search-started">'
                + 'Asking https://search.twitter.com ... </div>')

            // Notes:
            // - page_renderer() selbst übernehmen, und simple und multi-renderer trotzdem ausführen (direkt aufrufen)
            //   see webclient simple title_renderer:
            //   "dm4c.get_simple_renderer("dm4.webclient.text_renderer").render_form(page_model, parent_element)"
            // - see webclient migration to get name of all default renderer
            // - defualt_text_renderer (assoc_def, wenn aggregation dann rendert dieser die combobox plus eingabefeld)

        }

        // configure menu and type commands
        dm4c.add_listener('topic_commands', function (topic) {
            if (!dm4c.has_create_permission('org.deepamehta.twitter.search')) {
                return
            }
            var commands = []
            if (topic.type_uri === 'org.deepamehta.twitter.search') {
                commands.push({is_separator: true, context: 'context-menu'})
                commands.push({
                    label: 'Search',
                    handler: searchPublicTweets,
                    context: ['context-menu', 'detail-panel-show']
                })
            }
            return commands
        })

    })

}(jQuery, dm4c))
