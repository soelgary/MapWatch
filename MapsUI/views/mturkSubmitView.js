define([
    "text!templates/mturkSubmitForm.html",
    "util/queryParameters.js"
  ], function(template, QueryParameters){
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      el: '#header',


      submit: function() {
        //$('#mturk_form').submit();
      },

      render: function() {
        var queryParams = new QueryParameters();
        this.$el.append(this.template({assignmentId: queryParams.getAssignmentId()}))
      }
    });
});
