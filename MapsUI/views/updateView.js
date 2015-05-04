define([
    "text!templates/map.html",
    "views/adjacentImageView.js",
    "views/gifView.js",
    "views/formView.js"
  ], function(template, AdjacentImageView, GifView, FormView){
    return Backbone.View.extend({
      template: Handlebars.compile(template),

      initialize: function(options) {
        this.parent = options.parent;
        this.model = options.model;
        this.render();
      },

      next: function() {
        this.parent.next();
      },

      render: function() {
        console.log('NEW IMAGE PATH');
        console.log(this.model.get('newMap').path);
        var options = {
          img1: "https://achtung.ccs.neu.edu/~soelgary/img/" + this.model.get('newMap').path, 
          img2: "https://achtung.ccs.neu.edu/~soelgary/img/" + this.model.get('oldMap').path,
        };
        var formView = new FormView({model: this.model, parent: this, buttonText: 'Next'});
        var gifView = new GifView(options);
        var adjacentImageView = new AdjacentImageView(options)
      },
    });
});