define([
  "../../util/URL",
  "models/CountryData"
],
  function(URL, CountryData){
    var CountryData = Backbone.Collection.extend({

      model: CountryData,

      url: function() {
        var url = new URL();
        return url.getURL() + this.mapProvider + '/updates/' + this.googleHITUpdateId + '/cctld';
      },

      initialize: function(googleHITUpdateId, mapProvider) {
        this.googleHITUpdateId = googleHITUpdateId;
        this.mapProvider = mapProvider;
      },

    });
  return CountryData;
});
