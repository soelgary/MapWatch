define(["models/updateModel.js"],
  function(Update){
    var Updates = Backbone.Collection.extend({
      model: Update,
      async: false,
      url: function() {
        return 'http://127.0.0.1:9092/maps/google/hits/mturk/' + this.getHITId()
      },
      //url: 'http://' + window.location.hostname + ':' + window.location.port + '/maps/google/updates?reserve=false',
      //url: 'http://127.0.0.1:9092/maps/google/updates?reserve=false',

      getHITId: function() {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
          var pair = vars[i].split("=");
          if(pair[0] == 'hitId'){
            return pair[1];
          }
       }
       return(false);
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