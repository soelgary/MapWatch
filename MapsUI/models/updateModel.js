define([],
  function(){
    var Update = Backbone.Model.extend({
      url: 'http://api.test.gsoeller.com/maps/google/updates',
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