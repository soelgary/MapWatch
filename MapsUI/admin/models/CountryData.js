define([
  "../../util/URL"
],
  function(URL){
    var CountryData = Backbone.Model.extend({
      defaults: {
        dateTime: null,
        region: null
      }
    });
  return CountryData;
});
