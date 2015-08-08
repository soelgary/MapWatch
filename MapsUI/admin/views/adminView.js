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
        finished = true,
        count = 20,
        offset = 0;
        this.render();
      },

      events: {
        "click .fetchButton": "fetch"
      },

      fetch: function() {
        var finished = $('#finished').is(':checked');
        var offset = Number($('#offset').val());
        var totalCount = Number($('#totalCount').val());
        var listView = new ListView({finished: finished, offset: offset, count: totalCount});
      },

      render: function() {
        this.$el.html(this.template({}));
      }
    });
});
