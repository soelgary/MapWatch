define([
    "text!templates/form.html",
  ], function(template, NextButton, QueryParameters){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#form',

      initialize: function(options) {
        this.update = options.model;
        this.borderDifference = false;
        this.checkedAnswer = false;
        this.render();
      },

      events: {
        'click input[name=borderDifference]:checked': 'onRadioClick',
        "click #submit-button": "submit"
      },

      onRadioClick: function() {
        this.checkedAnswer = true;
        this.borderDifference = $('input[name=borderDifference]:checked').val();
      },

      submit: function() {
        $('#status').text('');
        if(this.checkedAnswer) {
          this.update.set('hasBorderChange', this.borderDifference);
          var self = this;
          this.update.save([], {
            success: function(model, response, options) {
              $('#status').text('Successfully saved results..');
              var id = '#icon-' + model.id;
              $(id).html('<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>');
            },
            error: function(model, response, options) {
              $('#status').text('An error occurred..');
              var id = '#icon-' + model.id;
              $(id).html('<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>');
            }
          });
          this.undelegateEvents();
        }
      },

      render: function() {
        console.log('rendering');
        this.$el.html(this.template({}));
      },
    });
});
