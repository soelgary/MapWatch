define([],
  function(){
    var GoogleHITUpdate = Backbone.Model.extend({

      url: function() {
        return 'http://127.0.0.1:9092/auth/generate-token';
      },

      initialize: function(username, password) {
        this.username = username;
        this.password = password;
      },

      parse: function(response) {
        return response;
      },

      fetch: function(options) {
        console.log('fetching');
        options.headers = {
          'Authorization': 'Basic ' + btoa(this.username + ':' + this.password)
        };
        return Backbone.Collection.prototype.fetch.call(this, options);
      },

    });
  return GoogleHITUpdate;
});
