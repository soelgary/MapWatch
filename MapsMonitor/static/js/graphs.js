(function($){
  var startTime = new Date();
  
  function normalizeTime(monitorTime) {
    var monitorMillis = monitorTime;//monitorTime.getTime();
    var millisDifference = monitorMillis - startTime.getTime();
    var secondsDifference = millisDifference / 1000;
    var index = Math.floor(secondsDifference / 10);
    return new Date(index * 10 * 1000 + startTime.getTime()).getTime()

  }
    
  var dps = []; 
  var googleDataPoints = [];
  var bingDataPoints = [];
  var pingTime = new Date();
  var status;

  var bingChart = new CanvasJS.Chart("bingContainer",{
      title :{
        text: "Bing Monitor"
      },
      
      data: [{
        type: "line",
        dataPoints: bingDataPoints,
        xValueType: "dateTime"
      }]
    });

  var googleChart = new CanvasJS.Chart("googleContainer",{
      title :{
        text: "Google Monitor"
      },
      
      data: [{
        type: "line",
        dataPoints: googleDataPoints,
        xValueType: "dateTime"
      }]
    });

  function addDataPoint(dataPoint, chartDataPoints) {
    var normalizedTime = normalizeTime(dataPoint.time);
    
    for(var i = 0; i < chartDataPoints.length; i+=1) {
      if(chartDataPoints[i].x == normalizedTime) {
        chartDataPoints[i].y = chartDataPoints[i].y + 1;
        return;
      }
    }
    chartDataPoints.push({x: normalizedTime, y:1});
  }

  function addDataPointError(errorDataPoint, chartDataPoints) {
    var normalizedTime = normalizeTime(errorDataPoint.time);
    if(errorDataPoint.level = "fatal" || status == "fatal") {
      chartDataPoints.push({x: normalizedTime, y: 0, markerColor: "red"});
    } else {
      chartDataPoints.push({x: normalizedTime, y: 0});
    }
  }

  function sendPing() {
    socket.emit('ping', {time: pingTime.getTime()});
    pingTime = new Date();
  }

  var socket = io.connect('http://127.0.0.1:5000/test');
  var d = new Date().getTime()
  var twenty_minutes = 1000 * 60 * 20;
  var start = new Date().getTime() - twenty_minutes;
  socket.emit('initialize', {time: start});
  
  socket.on('initialize', function(msg) {
    console.log(msg);
    _.each(msg.data, function(model) {
      if(model.mapProvider == "google") {
        addDataPoint(model, googleDataPoints);
        googleChart.render();
      } else {
        addDataPoint(model, bingDataPoints);
        bingChart.render();
      }
    })
    $("#loader").remove()
  });

  socket.on('monitor', function(monitor) {
    console.log(monitor);
    if(monitor.mapProvider == "google") {
      addDataPoint(monitor, googleDataPoints);
      googleChart.render();
    } else {
      addDataPoint(monitor, bingDataPoints);
      bingChart.render();
    }
  });

  socket.on('ping', function(monitor_errors) {
    console.log(monitor_errors);
    if(monitor_errors.errors.length == 0) {
      status = "normal";
    }
    _.each(monitor_errors.errors, function(error) {
      if(error.mapProvider == "google") {
        addDataPointError(error, googleDataPoints);
        googleChart.render();  
      } else {
        addDataPointError(error, bingDataPoints);
        bingChart.render();  
      }
    });
  });

  var five_seconds = 5000;
  // send ping every 5 seconds
  setInterval(function(){sendPing()}, five_seconds);
})(jQuery);