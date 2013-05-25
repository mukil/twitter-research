/*global jQuery, dm4c*/
(function ($, dm4c) {

    dm4c.add_simple_renderer('org.deepamehta.twitter.search_lang_field', {

        render_info: function (model, $parent) {
            $parent.append('<div class="field-label">Search Language</div>'
                + '<div class="twitter-search-lang">' +model.object.value+ '</div>')
        },

        render_form: function (model, $parent) {

            var defaultLanguage = "unspecified"

            if (model.object.value != "") defaultLanguage = model.object.value

            $parent.html('<div class="field-label">Search Language<br/>'
                + '<br/>Help: List of <a target="_blank" href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">'
                + 'ISO-639-1 language codes</a> (Wikipedia)' // fixme: ask wikidata :`)
                + '<br/><br/></div>'
                + '<input type="text" class="search-lang" value="' +defaultLanguage+ '"></input>')

            return function () {
                var query = $("input.search-lang").val()
                if (typeof query === undefined) return defaultLanguage
                return query // set dummy field after edit
            }
        }
    })

}(jQuery, dm4c))
