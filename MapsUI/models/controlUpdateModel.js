define([
    "util/queryParameters.js"
  ], function(QueryParameters){
    return Backbone.Model.extend({
        url: function() {
          //return 'http://127.0.0.1:9092/maps/' + this.getMapProvider() + '/hits/' + this.getHITId() + '/control';
          return 'https://achtung.ccs.neu.edu/maps/maps/' + this.getMapProvider() + '/hits/' + this.getHITId() + '/control';
        },

        getMapProvider() {
          var queryParams = new QueryParameters();
          return queryParams.getMapProvider();
        },

        getHITId: function() {
          var queryParameters = new QueryParameters();
          return queryParameters.getHITId();
        },

        initialize: function(mapProvider) {
          this.mapProvider = mapProvider;
        }
    });
});
