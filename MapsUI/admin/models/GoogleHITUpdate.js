define([
  "models/Cookies",
  "../../util/URL"
],
  function(Cookies, URL){
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
          var cookies = new Cookies();
          var url = new URL();
          this.url = url.getURL() + this.mapProvider + '/updates/' + id + "?token=" + cookies.getCookie("token");
      },

      initialize: function(mapProvider) {
        this.mapProvider = mapProvider;
      }
    });
  return GoogleHITUpdate;
});
