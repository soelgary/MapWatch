
casper = require('casper').create()

casper.userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36")

casper.start()

casper.then ->
  @download('https://www.google.com/maps/vt', 'pb=!1m4!1m3!1i14!2i4955!3i6063!2m3!1e0!2sm!3i274264187!3m7!2sen!3sus!5e1105!12m1!1e47!12m1!1e38!4e0!5m1!5f2!20m1!1b1')

  

casper.run ->
  @echo('Exiting').exit()