define([
  "models/GoogleHITUpdate",
  "../../util/URL"
], function(GoogleHITUpdate, URL){
    var GoogleHITUpdates = Backbone.Collection.extend({
      model: GoogleHITUpdate,
      async: false,
      url: function() {
        var url = new URL();
        return url.getURL() + this.mapProvider + '/updates'
      },
      parse: function(response) {
        return response;
      },
      initialize: function(mapProvider) {
        this.mapProvider = mapProvider;
      }
    });
  return GoogleHITUpdates;
});
