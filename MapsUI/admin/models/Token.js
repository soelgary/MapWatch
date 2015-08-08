define([],
  function(){
    var Token = Backbone.Model.extend({

      url: function() {
        //return 'http://127.0.0.1:9092/auth/token/' + this.tokenValue;
        return 'https://achtung.ccs.neu.edu/maps/auth/token/' + this.tokenValue;
      },

      initialize: function(tokenValue) {
        this.tokenValue = tokenValue;
      },

      parse: function(response) {
        return response;
      },

    });
  return Token;
});
