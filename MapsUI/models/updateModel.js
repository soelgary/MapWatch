define([],
  function(){
    var Update = Backbone.Model.extend({
      url: function() {
        return 'https://achtung.ccs.neu.edu/maps/maps/google/hits/mturk/' + this.getHITId() + '/update/' + this.id;
      },

      getHITId: function() {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
          var pair = vars[i].split("=");
          if(pair[0] == 'hitId'){
            return pair[1].replace('/', '');
          }
       }
       return(false);
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