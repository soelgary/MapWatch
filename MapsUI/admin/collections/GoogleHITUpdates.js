define([
  "models/GoogleHITUpdate",
], function(GoogleHITUpdate){
    var GoogleHITUpdates = Backbone.Collection.extend({
      model: GoogleHITUpdate,
      async: false,
      url: function() {
        return 'http://127.0.0.1:9092/google/updates';
      },

      initialize: function(finished, count, offset){
      },

      fetchSuccess: function (collection, response) {
          console.log('Collection fetch success', response);
          console.log('Collection models: ', collection.models);
      },

      fetchError: function (collection, response) {
          throw new Error("Maps fetch error");
      },

      parse: function(response) {
        return response;
      }
    });
  return GoogleHITUpdates;
});
