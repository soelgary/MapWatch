define([
    "util/queryParameters.js"
  ], function(QueryParameters){
    var Update = Backbone.Model.extend({
      url: function() {
        return 'http://127.0.0.1:9092/maps/' + this.getMapProvider() + '/hits/mturk/' + this.getHITId() + '/update/' + this.id;
        //return 'https://achtung.ccs.neu.edu/maps/maps/google/hits/mturk/' + this.getHITId() + '/update/' + this.id;
      },

      getMapProvider() {
        var queryParams = new QueryParameters();
        return queryParams.getMapProvider();
      },

      getHITId: function() {
        var queryParams = new QueryParameters();
        return queryParams.getHITId();
      },

      initialize: function(mapProvider) {
        this.mapProvider = mapProvider;
      },

      defaults: {
        oldMap: null,
        newMap: null,
        id: null,
        notes: null,
        hasBorderChange: null,
        finished: null,
        hitId: null
      },
    });
  return Update;
});
