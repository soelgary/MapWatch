define([
    "text!templates/map.html",
    "views/adjacentImageView.js",
    "views/gifView.js",
    "views/formView.js"
  ], function(template, AdjacentImageView, GifView, FormView){
    return Backbone.View.extend({
      el: '#maps',
      template: Handlebars.compile(template),

      initialize: function(options) {
        this.parent = options.parent;
        this.render();
      },

      next: function() {
        this.parent.next();
      },

      render: function() {
        var options = {
          el: this.$el,
          img1: "img/a9302e0a-cbf7-48e5-a150-9acc247c3eb9.png", 
          img2: "img/fe41928c-ab90-48c3-a6ff-725caa7ecc17.png",
          id: this.model.id
        };
        var formView = new FormView({el: this.$el, model: this.model, parent: this});
        var gifView = new GifView(options);
        var adjacentImageView = new AdjacentImageView(options)
      },
    });
});