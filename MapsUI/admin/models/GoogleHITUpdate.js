define([],
  function(){
    var GoogleHITUpdate = Backbone.Model.extend({
      defaults: {
        oldMap: null,
        newMap: null,
        id: null,
        notes: null,
        hasBorderChange: null,
        finished: null,
        hitId: null
      },

      setUrl: function(id) {
          this.url = 'https://achtung.ccs.neu.edu/~soelgary/maps/google/updates/' + id;
      }
    });
  return GoogleHITUpdate;
});
