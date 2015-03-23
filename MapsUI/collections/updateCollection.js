define(["models/updateModel.js"],
  function(Update){
    var Updates = Backbone.Collection.extend({
      model: Update,
      async: false,
      url: 'http://' + window.location.hostname + ':' + window.location.port + '/maps/google/updates?reserve=false',
      parse : function(resp) {
        return resp;
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
    }
    });
  return Updates;
});