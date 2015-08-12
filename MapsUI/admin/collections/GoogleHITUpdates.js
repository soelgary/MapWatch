define([
  "models/GoogleHITUpdate",
  "models/URL"
], function(GoogleHITUpdate, URL){
    var GoogleHITUpdates = Backbone.Collection.extend({
      model: GoogleHITUpdate,
      async: false,
      url: function() {
        var url = new URL();
        return url.getURL() + 'google/updates'
      },
      parse: function(response) {
        return response;
      }
    });
  return GoogleHITUpdates;
});
