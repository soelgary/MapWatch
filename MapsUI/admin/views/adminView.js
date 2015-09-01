define([
    "text!templates/adminFilter.html",
    "views/listView",
    "views/navView"
  ], function(template, ListView, NavView){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#filterForm',

      initialize: function(options) {
        var nav = new NavView();
        this.render();
      },

      events: {
        "click .fetchButton": "fetch"
      },

      fetch: function() {
        if(this.listView) {
          this.listView.$el.empty();
          this.listView.undelegateEvents();
          //this.listView.$el.empty().off();
          //this.listView.stopListening();
          //this.listView.remove();
        }
        var finished = $('#finished').is(':checked');
        var offset = Number($('#offset').val());
        var totalCount = Number($('#totalCount').val());
        var hasBorderDifference = $('#hasChange').is(':checked');
        var mapProvider = $("input[name=mapProvidersRadios]:checked").val();
        this.listView = new ListView(
          {
            finished: finished,
            offset: offset,
            count: totalCount,
            hasBorderDifference: hasBorderDifference,
            mapProvider: mapProvider
          }
        );
      },

      render: function() {
        this.$el.html(this.template({}));
      }
    });
});
