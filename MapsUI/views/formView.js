define([
    "text!templates/form.html",
    "text!templates/nextButton.html"
  ], function(template, NextButton){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      nextButton: Handlebars.compile(NextButton),

      initialize: function(options) {
        this.update = options.model;
        this.visibleDifference = false;
        this.borderDifference = false;
        this.notes = "";
        this.parent = options.parent;
        this.render();
      },

      events: {
        "click #q1-checkbox": "q1Clicked",
        "click #q2-checkbox": "q2Clicked",
        "click #q3-checkbox": "q3Clicked",
        "click #submit-button": "submit"
      },

      submit: function() {
        console.log('submitting');
        var appearDifferent = $('#q1-checkbox').is(':checked');
        var borderChange = $('#q2-checkbox').is(':checked');
        var comments = $('#comment').val();
        var notes = "Appear different - " + appearDifferent + " | Border Change - " + borderChange + " | " + comments;
        this.update.set('notes', notes);
        this.update.set('hasBorderChange', borderChange)
        this.update.save();
        this.undelegateEvents();
        this.parent.next();

        //this.renderNextButton()
      },

      q1Clicked: function() {
        console.log('q1');
        console.log($('#q1-checkbox').is(':checked'));
        console.log(this.update);
        if($('#q1-checkbox').is(':checked')) {
          $('#q2-checkbox').attr("disabled", false);
          $('#q3-checkbox').attr("disabled", false);
        } else {
          $('#q2-checkbox').attr("disabled", true);
          $('#q3-checkbox').attr("disabled", true);
        }
      },

      q2Clicked: function() {
        console.log('q2');
        console.log($('#q2-checkbox').is(':checked'));
        console.log(this.update);
        if($('#q2-checkbox').is(':checked')) {
          this.borderDifference = true;
        } else {
          this.borderDifference = false;
        }
      },

      q3Clicked: function() {
        console.log('q3');
        console.log($('#q3-checkbox').is(':checked'));
        console.log(this.update);
        if($('#q3-checkbox').is(':checked')) {
          
        } else {
          
        }
      },
      
      render: function() {
        this.$el.append(this.template());
      },

      renderNextButton: function() {
        this.$el.append(this.nextButton());
      }
    });
});