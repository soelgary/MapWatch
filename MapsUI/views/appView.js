define([
  "collections/updateCollection.js",
  "views/updateView.js",
  "views/footerView",
  "text!templates/cta.html",
  "views/headerView",
  "views/controlView",
  "models/updateModel"
  ], function(UpdateCollection, UpdateView, FooterView, cta, HeaderView, ControlView, UpdateModel){
    return Backbone.View.extend({
      tagName: "div",
      cta: Handlebars.compile(cta),
      
      initialize: function() {
        this.collection = new UpdateCollection();
        var self = this;
        this.currentModel = 0;
        this.currentPage = 1;
        this.collection.fetch().complete(function(){
          self.render(false);
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
      
      render: function(renderControl) {
        this.numPages = this.collection.models.length + 1;
        var headerView = new HeaderView({currentPage: this.currentPage, numPages: this.numPages});
        if(renderControl) {
          var controlView = new ControlView({control: this.collection.control});
        } else {
          this.numPages = this.collection.models.length + 1;
          var updateView = new UpdateView({model: this.collection.models[this.currentModel], parent: this});
        }
      },
    });
});
