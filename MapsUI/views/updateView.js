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
          img1: "https://achtung.ccs.neu.edu/~soelgary/img/" + this.model.get('newMap').path, 
          img2: "https://achtung.ccs.neu.edu/~soelgary/img/" + this.model.get('oldMap').path,
          id: this.model.id
        };
        var formView = new FormView({el: this.$el, model: this.model, parent: this});
        var gifView = new GifView(options);
        var adjacentImageView = new AdjacentImageView(options)
      },
    });
});