define([
    "text!templates/gif.html"
  ], function(template){
    return Backbone.View.extend({
      template: Handlebars.compile(template),

      initialize: function(options) {
        this.img1 = options.img1;
        this.img2 = options.img2;
        this.id = options.id;
        this.render();
      },

      render: function() {
        
        this.$el.append(this.template({img1: this.img1, img2: this.img2, id: this.id}));
          gifshot.createGIF({
            'images': [this.img1, this.img2],
            'interval': 1,
            'gifWidth': 600,
            'gifHeight': 600
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