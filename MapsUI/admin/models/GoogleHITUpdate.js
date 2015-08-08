define([
  "models/Cookies"
],
  function(Cookies){
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

      setUrl: function(id, token) {
          var cookies = new Cookies();
          //this.url = 'http://127.0.0.1:9092/google/updates/' + id;
          this.url = 'https://achtung.ccs.neu.edu/maps/google/updates/' + id + "?token=" + cookies.getCookie("token");
      }
    });
  return GoogleHITUpdate;
});
