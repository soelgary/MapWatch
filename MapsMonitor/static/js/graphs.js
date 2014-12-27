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

  function loadEmptyBing() {
    loadEmpty(bingChart, bingDataPoints);
  }

  function loadEmptyGoogle() {
    loadEmpty(googleChart, googleDataPoints);
  }

  function loadEmpty(chart, dataPoints) {
    var startTime = new Date().getTime() - (1000 * 10 * 120);
    var normalizedTime = normalizeTime(startTime);
    dataPoints[0] = {x: normalizedTime, y: 0, markerColor: "red"};
    var numDataPoints = 120;
    var next = startTime;
    for(var i = 1; i < numDataPoints; i+=1) {
      next += 1000 * 10; // milliseconds * 10 seconds
      dataPoints[i] = {x: next, y: 0, markerColor: "red"};
    }
    chart.options.data[0]['color'] = "red";
  }

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

  function addEmptyDataPoints() {
    addEmptyDataPoint(bingChart, bingDataPoints);
    addEmptyDataPoint(googleChart, googleDataPoints);
  }

  function addEmptyDataPoint(chart, dataPoints) {
    var now = new Date().getTime();
    var normalizedTime = normalizeTime(now);
    _.each(dataPoints, function(dataPoint) {
      if(dataPoint.x + (1000 * 9) <= normalizedTime) {
        return;
      }
    });
    dataPoints.push({x: normalizedTime, y: 0});
    chart.render();
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

  var path;
  if(window.location.href == '127.0.0.1') {
    path = window.location.href + ':' + window.location.port;
  } else {
    path = window.location.href;
  }
  console.log(path);
  var socket = io.connect(path + 'test');
  var d = new Date().getTime()
  var twenty_minutes = 1000 * 60 * 20;
  var start = new Date().getTime() - twenty_minutes;
  socket.emit('initialize', {time: start});
  
  socket.on('initialize', function(msg) {
    console.log(msg);
    var bingLoaded = false;
    var googleLoaded = false;
    _.each(msg.data, function(model) {
      if(model.mapProvider == "google") {
        addDataPoint(model, googleDataPoints);
        googleLoaded = true;
      } else {
        addDataPoint(model, bingDataPoints);
        bingLoaded = true;
      }
    })
    if(!bingLoaded) {
      loadEmptyBing();
    }
    if(!googleLoaded) {
      loadEmptyGoogle();
    }
    bingChart.render();
    googleChart.render();
    $("#bingLoader").remove()
    $("#googleLoader").remove()
  });

  socket.on('monitor', function(monitor) {
    console.log(monitor);
    if(monitor.mapProvider == "google") {
      addDataPoint(monitor, googleDataPoints);
      googleChart.render();
      googleChart.options.data[0]['color'] = "blue";
    } else {
      addDataPoint(monitor, bingDataPoints);
      bingChart.render();
      bingChart.options.data[0]['color'] = "blue";
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

  socket.on('change', function(change) {
    console.log(change);
  });

  var five_seconds = 5000;
  var ten_seconds = five_seconds * 2;
  // send ping every 5 seconds
  setInterval(function(){sendPing()}, five_seconds);
  // add empty data point every ten seconds if doesn't receive any data
  setInterval(function(){addEmptyDataPoints()}, ten_seconds);
})(jQuery);