define([
  "../../util/URL"
],
  function(URL){
    var Token = Backbone.Model.extend({

      url: function() {
        var url = new URL();
        return url.getURL() + 'auth/token/' + this.tokenValue;
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
