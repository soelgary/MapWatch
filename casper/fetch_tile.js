var http = require('http');
var fs = require('fs');

var headers = {'user-agent': "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36"};
var host = 'www.google.com';

var options = {
  host: host,
  path: '/maps/vt/pb=!1m4!1m3!1i14!2i4955!3i6063!2m3!1e0!2sm!3i274264187!3m7!2sen!3sus!5e1105!12m1!1e47!12m1!1e38!4e0!5m1!5f2!20m1!1b1',
  headers: headers
};

var returnImage = function(data) {
  console.log(data);
}

var temp = 'herro';

callback = function(res) {
  var imagedata = ''
    res.setEncoding('binary')

    res.on('data', function(chunk){
        imagedata += chunk;
        //console.log(chunk);
    })

    res.on('end', function(){
        console.log(temp);
        fs.writeFile('logo.png', imagedata, 'binary', function(err){
            if (err) throw err
            console.log('File saved.')
        })
    })
   returnImage(imagedata);
}

http.request(options, callback).end();

'https://www.google.com/maps/vt/pb=!1m4!1m3!1i14!2i4955!3i6063!2m3!1e0!2sm!3i274264187!3m7!2sen!3sus!5e1105!12m1!1e47!12m1!1e38!4e0!5m1!5f2!20m1!1b1'
'https://www.google.com/maps/vt/pb=!1m4!1m3!1i15!2i9913!3i12122!2m3!1e0!2sm!3i275290430!3m7!2sen!3sus!5e1105!12m1!1e47!12m1!1e38!4e0!5m1!5f2!20m1!1b1'