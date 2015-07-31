define([
  "text!templates/header.html"
  ], function(header){
    return Backbone.View.extend({
      header: Handlebars.compile(header),
      el: "#header",
      
      initialize: function(options) {
        this.currentPage = options.currentPage;
        this.numPages = options.numPages;
        this.render();
      },
      
      render: function(renderControl) {
        this.$el.html(this.header({numPages: this.numPages, currentPage: this.currentPage}));
      },
    });
});