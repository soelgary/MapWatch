define([
  "js/jquery-1.11.1.minjs",
  "routes.js"
  ], function($, Router){
    return $(function() {
      new Router();
      return Backbone.history.start({
        root: ''
      });
    });
});