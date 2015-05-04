define([
    "text!templates/gif.html"
  ], function(template){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#middle-col',

      initialize: function(options) {
        this.img1 = options.img1;
        this.img2 = options.img2;
        this.render();
      },

      render: function() {
        
        this.$el.html(this.template({img1: this.img1, img2: this.img2}));
          gifshot.createGIF({
            'images': [this.img1, this.img2],
            'interval': 1,
            'gifWidth': 450,
            'gifHeight': 450
          },function(obj) {
              if(!obj.error) {
                  var image = obj.image;
                  console.log(image);
                  document.getElementById('gif').src = image;
              } else {
                console.log(obj);
              }
          });
      },
    });
});