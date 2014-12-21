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
  var indexOffset = 0;
  var pingTime = new Date();
  var status;

  var chart = new CanvasJS.Chart("chartContainer",{
      title :{
        text: "Live Random Data"
      },
      
      data: [{
        type: "line",
        dataPoints: googleDataPoints,
        xValueType: "dateTime"
      }]
    });

  function addDataPoint(dataPoint) {
    var normalizedTime = normalizeTime(dataPoint.time);
    console.log(normalizedTime);
    for(var i = 0; i < googleDataPoints.length; i+=1) {
      if(googleDataPoints[i].x == normalizedTime) {
        googleDataPoints[i].y = googleDataPoints[i].y + 1;
        return;
      }
    }
    googleDataPoints.push({x: normalizedTime, y:1});
  }

  function addDataPointError(errorDataPoint) {
    var normalizedTime = normalizeTime(errorDataPoint.time);
    if(errorDataPoint.level = "fatal" || status == "fatal") {
      googleDataPoints.push({x: normalizedTime, y: 0, markerColor: "red"});
    } else {
      googleDataPoints.push({x: normalizedTime, y: 0});
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
      addDataPoint(model);
    })
    console.log(googleDataPoints);
    chart.render();
    $("#loader").remove()
  });

  socket.on('monitor', function(monitor) {
    console.log(monitor);
    addDataPoint(monitor);
    chart.render();
  });

  socket.on('ping', function(monitor_errors) {
    console.log(monitor_errors);
    if(monitor_errors.errors.length == 0) {
      status = "normal";
    }
    _.each(monitor_errors.errors, function(error) {
      addDataPointError(error);
      chart.render();
    });
  });

  var five_seconds = 5000;
  // send ping every 5 seconds
  setInterval(function(){sendPing()}, five_seconds);
  

   
})(jQuery);