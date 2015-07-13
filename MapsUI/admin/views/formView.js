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
          this.update.save([], {
            success: function() {
              $('#status').text('Successfully saved results..');
            },
            error: function() {
              $('#status').text('An error occurred..')
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
