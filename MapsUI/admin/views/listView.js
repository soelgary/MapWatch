define([
    "text!templates/listUpdates.html",
    "collections/GoogleHITUpdates",
    "views/analyzeView",
    "models/Cookies",
    "text!templates/unauthorized.html"
  ], function(template, GoogleHITUpdates, AnalyzeView, Cookie, UnauthorizedTemplate){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      unauthorizedTemplate: Handlebars.compile(UnauthorizedTemplate),
      el: '#table',

      initialize: function(options) {
        finished = options.finished,
        count = options.count,
        offset = options.offset;
        this.cookies = new Cookie();
        token = this.cookies.getCookie('token');
        console.log(token);
        this.updates = new GoogleHITUpdates();
        var self = this;
        this.updates.fetch(
          {
            success: function(collection, response, options) {
              console.log('success fetching');
              Handlebars.registerHelper('list', function(items, options) {
                var out = "";
                for(var i=0, len=items.length; i<len; i++) {
                  out = out + "<tr>";
                  out = out + "<th>" + items[i].get('id') + "</th>";
                  out = out + "<th>" + items[i].get('hasBorderChange') + "</th>";
                  out = out + "<th>" + items[i].get('finished') + "</th>";
                  out = out + "<th><button data-update='" + items[i].get('id') + "' class='analyze-button'>Analyze</button><th>";
                  out = out + "<th id='icon-" + items[i].get('id') + "'></th>"
                  out = out + "</tr>";
                }
                return out;
              });
              self.render();
            },
            error: function(collection, response, options) {
              console.log('error fetching');
              $('#searchErrorMessage').html(self.unauthorizedTemplate({message: "Unauthorized -- Please login"}));
            },
            processData: true,
            data: {
              finished: finished,
              count: count,
              offset: offset,
              token: token
            }
          }
        ).complete(function(){

        });
      },

      events: {
        "click .analyze-button": "analyze"
      },

      analyze: function(ev){
        var id = $(ev.currentTarget).data('update');
        var analyzeView = new AnalyzeView({id: id, token: this.token});
      },

      render: function() {
        this.$el.html(this.template({updates: this.updates.models}));
        $('#example').dataTable( {
          "aaData": this.updates.models,
          "aoColumns": [
            {
              "mDataProp": "attributes.id",
              "title": "Id"
            },
            {
              "mDataProp": "attributes.hasBorderChange",
              "title": "Has Border Difference?"
            },
            {
              "mDataProp": "attributes.finished",
              "title": "Finished?"
            },
            {
                "title": "analyze"
            },
            {
              "title": "status"
            }
          ],
            "columnDefs": [{
              "targets": -2,
              "data": null,
              "render": function(data, type, row) {
                  return "<button data-update='" + data.get('id') + "' class='analyze-button'>Analyze</button>"
              }
            },
            {
              "targets": -1,
              "data": null,
              "render": function(data, type, row) {
                return "<div id='icon-" + data.get('id') + "'></div>"
              }
            }
          ]
        });
      }
    });
});
