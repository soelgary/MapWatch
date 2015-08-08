define([
    "text!templates/nav.html",
    "models/User",
    "models/Token",
    "cookie"
  ], function(template, User, Token, cookie){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#nav',
      token: {},

      initialize: function(options) {
        this.render();
        $('#loginFailed').hide();
        // need to check if there is a token
        // if token exists, check backend to see if it is valid
        // if valid, set the token to be that and skip the login process
        var token = $.cookie.get('token');
        if(token) {
          var fetchedToken = new Token(token);
          fetchedToken.fetch(
            {
              success: function(model, response, options) {
                self.token = response;
                self.render();
              }
            }
          );
        }
      },

      events: {
        "click #login-button": "signin"
      },

      signin: function() {
        var username = $('#inputUsername').val();
        var password = $('#inputPassword').val();
        var options = {
          success: function(model, response, options) {
            console.log('success fetching');
            self.token = response;
            $.cookie('token', response.value, {expires: 1, path: '/'});
            self.render();
          },
          error: function() {
            $('#loginFailed').show();
          }
        };
        this.user = new User(username, password);
        var self = this;
        this.user.fetch(options);
      },

      render: function() {
        console.log(this.token);
        this.$el.html(this.template({token: this.token}));
      }
    });
});
