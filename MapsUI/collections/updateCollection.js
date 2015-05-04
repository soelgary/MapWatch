define([
  "models/updateModel.js",
  "util/queryParameters.js"
  ], function(Update, QueryParameters){
    var Updates = Backbone.Collection.extend({
      model: Update,
      async: false,
      url: function() {
        return 'https://achtung.ccs.neu.edu/maps/maps/google/hits/mturk/' + this.getHITId()
      },

      getHITId: function() {
        var queryParams = new QueryParameters();
        return queryParams.getHITId();
      },

      initialize: function(){
        this.fetch({
            success: this.fetchSuccess,
            error: this.fetchError
        });
      },

      fetchSuccess: function (collection, response) {
          console.log('Collection fetch success', response);
          console.log('Collection models: ', collection.models);
      },

      fetchError: function (collection, response) {
          throw new Error("Maps fetch error");
      },

      parse: function(response) {
        this.control = response.control;
        return response.updates;
      }
    });
  return Updates;
});