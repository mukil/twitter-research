dm4c.add_multi_renderer('org.deepamehta.twitter.tweet_multi_renderer', {

    render_info: function (page_models, $parent) {

        sort_page_models()

        var list = $('"<ul class="twitter-list">')
        for (var i = 0; i < page_models.length; i++) {
            var item = page_models[i].object
            if (item != undefined) {
                if (item.id != -1) {
                    var username = ""
                    if (item.composite.hasOwnProperty('org.deepamehta.twitter.user')) {
                        username = item.composite['org.deepamehta.twitter.user'].composite['org.deepamehta.twitter.user_name'].value
                    }
                    var timestamp = ""
                    if (item.composite.hasOwnProperty('org.deepamehta.twitter.tweet_time')) {
                        timestamp = item.composite['org.deepamehta.twitter.tweet_time'].value
                    }
                    var text = ""
                    if (item.hasOwnProperty('value')) {
                        text = item.value
                    }
                    // give info-item some behaviour
                    $listItem = $('<div id="' +item.id+ '">')
                    $listItem.click(function(e) {
                        var topicId = this.id
                        dm4c.do_reveal_related_topic(topicId)
                    })
                    $listItem.append('<small>@' +username+ ' at ' +timestamp+ ' </small><br/><i>' + text + '</i>')
                    list.append($('<li class="tweet-item">').html($listItem))
                }
            }
        }
        $parent.append(list)

        function sort_page_models() {
            page_models.sort(function(pm_1, pm_2) {
                var val_1 = 0
                if (pm_1.object.composite.hasOwnProperty('org.deepamehta.twitter.tweet_time')) {
                    val_1 = pm_1.object.composite['org.deepamehta.twitter.tweet_time'].value
                }
                var val_2 = 0
                if (pm_2.object.composite.hasOwnProperty('org.deepamehta.twitter.tweet_time')) {
                    val_2 = pm_2.object.composite['org.deepamehta.twitter.tweet_time'].value
                }
                if (val_1 > val_2) // sort string descending
                    return -1
                if (val_1 < val_2)
                    return 1
                return 0 //default return value (no sorting)
                // return val_1 < val_2 ? -1 : val_1 == val_2 ? 0 : 1
            })
        }
    },

    render_form: function (page_models, $parent) {

        // user cannot edit aggregated tweets of a twitter-search within page panel

        return function () {
            var values = []
            // returning (and referencing) all previously aggregated items back in our submit-function
            for (var item in page_models) {
                var topic_id = page_models[item].object.id
                if (topic_id != -1) {
                    values.push(dm4c.REF_PREFIX + topic_id)
                }
            }
            return values
        }
    }
})
