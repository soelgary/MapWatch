define([
    "text!templates/country.html",
    "collections/CountryDataCollection",
    "text!templates/unauthorized.html"
  ], function(template, CountryDataCollection, UnauthorizedTemplate) {
    return Backbone.View.extend({
      template: Handlebars.compile(template),
      errorTemplate: Handlebars.compile(UnauthorizedTemplate),
      el: '#base-modal',

      initialize: function(options) {
        this.token = options.token;
        this.id = options.id;
        this.mapProvider = options.mapProvider;
        this.collection = new CountryDataCollection(this.id, this.mapProvider);
        var self = this;
        this.collection.fetch({
          error: function(collection, response, options) {
            $('#searchErrorMessage').html(self.errorTemplate({message: "An error occurred fetching country data"}));
          },
          success: function() {
            self.render();
          }
        })
      },

      render: function() {
        this.$el.html(this.template({models: this.collection.models}));
        jQuery.noConflict();
        $('#countryModal').modal('show');
        $('#countryDataTable').dataTable( {
          "aaData": this.collection.models,
          "aoColumns": [
            {
              "mDataProp": "attributes.region",
              "title": "Region"
            },
            {
              "mDataProp": "attributes.dateTime",
              "title": "Time Fetched",
              "mRender": function(data, type, full) {
                return new Date(data);
              }
            }
          ]
        });
      },
    });
});
