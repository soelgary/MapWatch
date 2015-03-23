define([
  "collections/updateCollection.js",
  "views/updateView.js",
  "text!templates/header.html",
  "views/footerView",
  "text!templates/cta.html"
  ], function(UpdateCollection, UpdateView, header, FooterView, cta){
    return Backbone.View.extend({
      tagName: "div",
      header: Handlebars.compile(header),
      cta: Handlebars.compile(cta),
      
      initialize: function() {
        this.collection = new UpdateCollection();
        var self = this;
        this.currentModel = 0;
        this.collection.fetch().complete(function(){
          self.render();
        });
      },

      next: function() {
        this.currentModel += 1;
        this.render();
      },
      
      render: function() {
        var currentPage = this.currentModel + 1;
        var numPages = this.collection.models.length;
        this.$el.html(this.header({numPages: numPages, currentPage: currentPage}));
        var updateView = new UpdateView({model: this.collection.models[this.currentModel], parent: this});
        //this.$el.append(this.cta({count: this.currentModel}));
        //var footerView = new FooterView();
      },
    });
});
