define([
    "text!templates/form.html",
    "text!templates/nextButton.html",
    "util/queryParameters.js"
  ], function(template, NextButton, QueryParameters){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      nextButton: Handlebars.compile(NextButton),
      el: '#left-col',

      initialize: function(options) {
        this.update = options.model;
        this.borderDifference = false;
        this.checkedAnswer = false;
        this.parent = options.parent;
        this.buttonText = options.buttonText;
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
        var queryParameters = new QueryParameters();
        if(this.checkedAnswer 
          && queryParameters.getAssignmentId()
          && queryParameters.getAssignmentId() != 'ASSIGNMENT_ID_NOT_AVAILABLE') {
          this.update.set('hasBorderChange', this.borderDifference);
          this.update.save();
          this.undelegateEvents();
          this.parent.next();
        }
      },
      
      render: function() {
        this.$el.html(this.template({buttonText: this.buttonText}));
      },

      renderNextButton: function() {
        this.$el.html(this.nextButton());
      }
    });
});