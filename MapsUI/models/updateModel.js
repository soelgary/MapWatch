define([
    "util/queryParameters.js"
  ], function(QueryParameters){
    var Update = Backbone.Model.extend({
      url: function() {
        return 'https://achtung.ccs.neu.edu/maps/maps/google/hits/mturk/' + this.getHITId() + '/update/' + this.id;
      },

      getHITId: function() {
        var queryParams = new QueryParameters();
        return queryParams.getHITId();
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