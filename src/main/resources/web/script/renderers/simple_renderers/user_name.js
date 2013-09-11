/** A simple renderer rendering a direct-link to the (external) user profile-page on twitter.com */
dm4c.add_simple_renderer('org.deepamehta.twitter.user_name_field', {

    render_info: function (model, $parent) {
        var username = model.object.value
        $parent.append('<div class="field-label">Twitter User</div>'
                + '<div class="field-item twitter-username">@' +username+ '</a></div>')
                /* '(<a target="_blank" '
                + 'title="Link to twitter.com/@"'+username+'" '
                + 'href="http://www.twitter.com/' + username+ '">External Link</a>)</div>') **/
    },

    render_form: function (model, $parent) {
        // $parent.html('<input type="text" class="username" value="">' +model.object.value+ '</input>')

        return function () {
            // var query = $("input.username").val()
            // if (typeof model.object.value === undefined) return ""
            return model.object.value // set dummy field after edit
        }
    }

})
