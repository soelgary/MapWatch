define([
    "text!templates/listUpdates.html",
    "collections/GoogleHITUpdates",
    "views/analyzeView"
  ], function(template, GoogleHITUpdates, AnalyzeView){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#left-col',

      initialize: function(options) {
        finished = true,
        count = 20,
        offset = 0;
        this.updates = new GoogleHITUpdates(finished, count, offset);
        var self = this;
        this.updates.fetch(
          {
            processData: true,
            data: {
              finished: finished,
              count: count,
              offset: offset
            }
          }
        ).complete(function(){
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
        });
      },

      events: {
        "click .analyze-button": "analyze"
      },

      analyze: function(ev){
        var id = $(ev.currentTarget).data('update');
        var analyzeView = new AnalyzeView({id: id});
      },

      render: function() {
        this.$el.html(this.template({updates: this.updates.models}));
        console.log(JSON.stringify(this.updates.models));
        console.log(this.updates.models);
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
            }],
            "columnDefs": [ {
              "targets": -1,
              "data": null,
              "render": function(data, type, row) {
                  return "<button data-update='" + data.get('id') + "' class='analyze-button'>Analyze</button>"
              }
            }]
        });
      }
    });
});
