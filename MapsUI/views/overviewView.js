define([
    "text!templates/overview.html",
    "views/appView.js"
  ], function(template, AppView){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#middle-col',

      initialize: function(options) {
        this.render();
      },

      events: {
        "click #start-button": "start"
      },

      start: function() {
        var app = new AppView({
          "el": $("#left-col"),
        });
      },

      render: function() {
        this.$el.html(this.template());
      }
    });
});
