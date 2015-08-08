define([
    "text!templates/analyze.html",
    "models/GoogleHITUpdate",
    "views/gifView",
    "views/formView"
  ], function(template, GoogleHITUpdate, GifView, FormView){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#right-col',

      initialize: function(options) {
        this.id = options.id;
        this.model = new GoogleHITUpdate();
        this.token = options.token;
        var self = this;

        this.model.setUrl(this.id);
        this.model.fetch().complete(function(){
          self.render();
        });
      },

      render: function() {
        this.$el.html(this.template({model: this.model}));
      },

      render: function() {
        this.$el.html(this.template({}));
        var options = {
          img1: "https://achtung.ccs.neu.edu/~soelgary/img/" + this.model.get('newMap').path,
          img2: "https://achtung.ccs.neu.edu/~soelgary/img/" + this.model.get('oldMap').path,
        };
        var formView = new FormView({model: this.model, token: this.token});
        //var gifView = new GifView(options);
      },
    });
});
