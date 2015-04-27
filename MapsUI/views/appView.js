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
        this.currentPage = 1;
        this.collection.fetch().complete(function(){
          self.render();
        });
      },

      next: function() {
        this.currentPage += 1;
        if(this.currentPage >= this.numPages) {
          this.render(true)
        } else {
          this.render(false);
        }
      },

      renderControl: function() {
        console.log('controlllllll');
      },
      
      render: function(renderControl) {
        if(renderControl) {
          this.$el.html(this.header({numPages: this.numPages, currentPage: this.currentPage}));
        } else {
          this.numPages = this.collection.models.length + 1;
          this.$el.html(this.header({numPages: this.numPages, currentPage: this.currentPage}));
          var updateView = new UpdateView({model: this.collection.models[this.currentModel], parent: this});
        }
      },
    });
});
