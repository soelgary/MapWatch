define([
    "util/queryParameters.js"
  ], function(QueryParameters){
    return Backbone.Model.extend({
        url: function() {
          return 'https://achtung.ccs.neu.edu/maps/maps/google/hits/' + this.getHITId() + '/control';
        },

        getHITId: function() {
          var queryParameters = new QueryParameters();
          return queryParameters.getHITId();
        },
    });
});