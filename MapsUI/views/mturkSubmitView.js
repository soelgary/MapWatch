define([
    "text!templates/mturkSubmitForm.html",
    "util/queryParameters.js"
  ], function(template, QueryParameters){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#header',

      initialize: function(options) {
        this.passedControl = options.passedControl;
      },

      submit: function() {
        $('#mturk_form').submit();
      },

      render: function() {
        var queryParams = new QueryParameters();
        var params = {
          assignmentId: queryParams.getAssignmentId(),
          hitId: queryParams.getHITId(),
          passedControl: this.passedControl
        }
        this.$el.append(this.template(params))
      }
    });
});
