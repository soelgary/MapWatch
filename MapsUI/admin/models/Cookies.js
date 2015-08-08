define([],
  function(){
    var Cookie = Backbone.Model.extend({

      getCookie: function(cookieName) {
        var name = cookieName + "=";
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1);
            if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
        }
        return "";
      },

      setCookie: function(key, value, expiration) {
        var d = new Date();
        d.setTime(d.getTime() + (expiration*24*60*60*1000));
        var expires = "expires="+d.toUTCString();
        document.cookie = key + "=" + value + "; " + expires;
      }

    });
  return Cookie;
});
