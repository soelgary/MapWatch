define([
  "models/URL",
  "models/CountryData"
],
  function(URL, CountryData){
    var CountryData = Backbone.Collection.extend({

      model: CountryData,

      url: function() {
        var url = new URL();
        return url.getURL() + 'google/updates/' + this.googleHITUpdateId + '/cctld';
      },

      initialize: function(googleHITUpdateId) {
        this.googleHITUpdateId = googleHITUpdateId;
      },

    });
  return CountryData;
});
