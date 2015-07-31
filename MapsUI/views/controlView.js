define([
    "text!templates/form.html",
    "views/gifView.js",
    "views/adjacentImageView.js",
    "models/controlUpdateModel.js",
    "views/mturkSubmitView.js"
  ], function(template, GifView, AdjacentImageView, ControlUpdateModel, MTurkSubmitForm){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#left-col',

      initialize: function(options) {
        this.control = options.control;
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
          var controlUpdateModel = new ControlUpdateModel({id: 1, controlResponse: this.borderDifference});
          controlUpdateModel.save();
          var passedControl = this.control.hasBorderDifference.toString() == this.borderDifference;
          var mturkSubmitForm = new MTurkSubmitForm({passedControl: passedControl});
          mturkSubmitForm.render();
          mturkSubmitForm.submit();
        }
      },

      render: function() {
        var options = {
          img1: "https://achtung.ccs.neu.edu/~soelgary/img/" + this.control.newMap.path, 
          img2: "https://achtung.ccs.neu.edu/~soelgary/img/" + this.control.oldMap.path,
        };
        this.$el.html(this.template({buttonText: 'Finish'}));
        var gifView = new GifView(options);
        var adjacentImageView = new AdjacentImageView(options);
      }
    });
});
