define([],
  function(){
    var Update = Backbone.Model.extend({
      url: 'http://127.0.0.1:9090/maps/google/updates',
      defaults: {
        oldMap: null,
        newMap: null,
        id: null,
        notes: null,
        stage: null,
        needsInvestigation: null,
        lastUpdated: null,
        inProgress: null,
        mapProvider: null
      },
    });
  return Update;
});