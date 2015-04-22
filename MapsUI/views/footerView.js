define([
  ], function(){
    return Backbone.View.extend({

      initialize: function(options) {
        this.render();
      },

      render: function() {
        this.$el.append("Footer");
      },
    });
});