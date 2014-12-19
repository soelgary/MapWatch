(function($){
  var MonitorRequest = Backbone.Model.extend({});

  var GoogleMonitorRequests = Backbone.Collection.extend({

    model: MonitorRequest,

    url: 'http://127.0.0.1:5000/google/1418360400000',

    parse: function(response) {
      return response.data;
    },

    fetchSuccess: function (collection, response) {
      console.log(collection);
      console.log(response);
    }
  });

  function getDate(milli) {
    console.log(milli);
    console.log(new Date(milli));
    return new Date(milli);
  }

  function successfulFetch(collection) {
    $("#loader").remove()
    var lineData = [{x: 1418360400000, y: 5}, {x: 1418446800000, y: 20}, {x: 1418533200000, y: 10}, {x: 1418619600000, y: 40}, {x: 1418706000000, y: 5}, {x: 1418792400000, y: 60}];
    lineData = [];
    console.log(lineData);
    _.each(collection.models, function(model) {
      console.log(model.toJSON());
      console.log(model.get('start'));
      lineData.push({x: model.get('start'), y: model.get('requests')});
    })
    var vis = d3.select('#visualisation'),
      WIDTH = 1000,
      HEIGHT = 250,
      MARGINS = {
        top: 20,
        right: 20,
        bottom: 20,
        left: 30
      },

      minDate = getDate(lineData[0].x),
      maxDate = getDate(lineData[lineData.length - 1].x),

      xRange = d3.time.scale().domain([minDate, maxDate]).range([0, WIDTH]),

      //xRange = d3.scale.linear().range([MARGINS.left, WIDTH - MARGINS.right]).domain([d3.min(lineData, function(d) {
      //  return d.x;
      //}), d3.max(lineData, function(d) {
      //  return d.x;
      //})]),

      yRange = d3.scale.linear().range([HEIGHT - MARGINS.top, MARGINS.bottom]).domain([d3.min(lineData, function(d) {
        return d.y;
      }), d3.max(lineData, function(d) {
        return d.y;
      })]),
      xAxis = d3.svg.axis()
        .scale(xRange)
        .tickSize(5)
        .tickSubdivide(true),
      yAxis = d3.svg.axis()
        .scale(yRange)
        .tickSize(5)
        .orient('left')
        .tickSubdivide(true);
   
      vis.append('svg:g')
        .attr('class', 'x axis')
        .attr('transform', 'translate(0,' + (HEIGHT - MARGINS.bottom) + ')')
        .call(xAxis);
       
      vis.append('svg:g')
        .attr('class', 'y axis')
        .attr('transform', 'translate(' + (MARGINS.left) + ',0)')
        .call(yAxis);

      var lineFunc = d3.svg.line()
        .x(function(d) {
          return xRange(getDate(d.x));
        })
        .y(function(d) {
          return yRange(d.y);
        })
        .interpolate('linear');

      function getInterpolation() {
        var interpolate = d3.scale.quantile()
          .domain([0,1])
          .range(d3.range(1, lineData.length + 1));
        return function(t) {
          var interpolatedLine = lineData.slice(0, interpolate(t));
          return lineFunc(interpolatedLine);
        }
      }

      vis.append('svg:path')
        .transition()
        .duration(500)
        .attrTween('d', getInterpolation)
        .attr('stroke', 'blue')
        .attr('stroke-width', 2)
        .attr('fill', 'none');
    }

  var requests = new GoogleMonitorRequests();
  requests.fetch({success: successfulFetch});


})(jQuery);