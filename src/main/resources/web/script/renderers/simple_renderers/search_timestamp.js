dm4c.add_simple_renderer('org.deepamehta.twitter.search_timestamp_field', {

    render_info: function (model, $parent) {
        var time = new Date(parseInt(model.object.value))
        if (time > 0) {
            $parent.append('<div class="field-label twitter-search-timestamp">Searched at '
                +time.getHours()+ ':' +time.getMinutes()+ ':' +time.getSeconds()+ ', '
                +time.getDate()+ '.' + (time.getMonth()+1) + ' ' +time.getFullYear()+ '</div>')
        }
    },

    render_form: function (model, $parent) {

        return function () {

            return model.object.value // just return existing value
        }
    }

})
