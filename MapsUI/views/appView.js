define([
  "collections/updateCollection.js",
  "views/updateView.js",
  "views/footerView",
  "text!templates/cta.html",
  "views/headerView"
  ], function(UpdateCollection, UpdateView, FooterView, cta, HeaderView){
    return Backbone.View.extend({
      tagName: "div",
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
        this.currentModel += 1;
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
        this.numPages = this.collection.models.length + 1;
        var headerView = new HeaderView({currentPage: this.currentPage, numPages: this.numPages});
        if(renderControl) {
        } else {
          this.numPages = this.collection.models.length + 1;
          var updateView = new UpdateView({model: this.collection.models[this.currentModel], parent: this});
        }
      },
    });
});
