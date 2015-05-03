define([
    "text!templates/form.html",
    "text!templates/nextButton.html"
  ], function(template, NextButton){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      nextButton: Handlebars.compile(NextButton),
      el: '#left-col',

      initialize: function(options) {
        this.update = options.model;
        this.borderDifference = false;
        this.checkedAnswer = false;
        this.parent = options.parent;
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
        if(this.checkedAnswer) {
          this.update.set('hasBorderChange', this.borderDifference);
          this.update.save();
          this.undelegateEvents();
          this.parent.next();
        }
      },
      
      render: function() {
        this.$el.html(this.template());
      },

      renderNextButton: function() {
        this.$el.html(this.nextButton());
      }
    });
});