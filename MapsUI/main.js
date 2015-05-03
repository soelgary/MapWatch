require([
  "views/appView"
  ], function(AppView) {
    //loads resources and starts the app
    app = new AppView({
      "el": $("#left-col"),
    });
});