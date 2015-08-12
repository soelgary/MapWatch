define([],
  function(){
    var URL = Backbone.Model.extend({
      getURL: function() {
        var port;
        if(location.port) {
            port = 9092;
        }
        return location.protocol+'//'+location.hostname+(port ? ':'+port: '') + (port ? '/': '/maps/');
      }
    });
  return URL;
});
