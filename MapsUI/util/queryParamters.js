define([],
  function(){
    return Backbone.Model.extend({

      getHITId: function() {
        return this.getQueryParameter('hitId');
      },

      getAssignmentId: function() {
        return this.getQueryParameter('assignmentId');
      },

      getQueryParameter: function(param) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
          var pair = vars[i].split("=");
          if(pair[0] == param){
            return pair[1].replace('/', '');
          }
       }
       return(false);
      }
    });
});