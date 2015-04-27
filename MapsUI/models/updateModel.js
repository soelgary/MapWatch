define([],
  function(){
    var Update = Backbone.Model.extend({
      url: function() {
        return 'http://achtung.ccs.neu.edu:9092/maps/google/hits/mturk/' + this.hitId + '/update/' + this.id;
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