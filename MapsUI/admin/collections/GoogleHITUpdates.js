define([
  "models/GoogleHITUpdate",
], function(GoogleHITUpdate){
    var GoogleHITUpdates = Backbone.Collection.extend({
      model: GoogleHITUpdate,
      async: false,
      url: function() {
        return 'http://127.0.0.1:9092/google/updates';
        //return 'https://achtung.ccs.neu.edu/maps/google/updates'
      },
      parse: function(response) {
        return response;
      }
    });
  return GoogleHITUpdates;
});
