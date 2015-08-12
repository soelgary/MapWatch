define([
  "models/Cookies",
  "models/URL"
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

      setUrl: function(id, token) {
          var cookies = new Cookies();
          var url = new URL();
          this.url = url.getURL() + 'google/updates/' + id + "?token=" + cookies.getCookie("token");
      }
    });
  return GoogleHITUpdate;
});
