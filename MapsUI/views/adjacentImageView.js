define([
    "text!templates/adjacentImages.html"
  ], function(template){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#right-col',

      initialize: function(options) {
        this.img1 = options.img1;
        this.img2 = options.img2;
        this.id = options.id;
        this.render();
      },
      
      render: function() {
        this.$el.html(this.template({img1: this.img1, img2: this.img2, id: this.id}));
      },
    });
});