dm4c.add_simple_renderer('org.deepamehta.twitter.tweet_entities_field', {

    render_info: function (model, $parent) {
        if (model.object != undefined && model.object.value !== "") {
            var entities = JSON.parse(model.object.value)
            $parent.append('<div class="field-label">Tweet Entities</div>')
            if (entities.hasOwnProperty('media')) {
                for (var i=0; i < entities.media.length; i++) {
                    var media = entities.media[i]
                    $parent.append('<div class="field-item tweet-entities media"><a href="' +media["expanded_url"]+ '">'
                        +media["display_url"]+ '</a></div>')
                }
            }
            if (entities.hasOwnProperty('urls')) {
                for (var i=0; i < entities.urls.length; i++) {
                    var url = entities.urls[i]
                    $parent.append('<div class="field-item tweet-entities urls"><a href="' +url["expanded_url"]+ '">'
                        +url["display_url"]+ '</a></div>')
                }
            }

        }
    },

    render_form: function (model, $parent) {
        var pluginResults = {};
        $parent.append('<div class="field-item tweet-entities">' +model.object.value+ '</div>')

        return function () {
            if (model.object != undefined) {
                return model.object.value // set dummy field after edit
            } else {
                return ""
            }
        }
    }

})
