import smtplib
import email.utils
from email.mime.text import MIMEText
import getpass
import ConfigParser

class SMTP():

  def send(self):
    config = ConfigParser.ConfigParser()
    config.read('config.cfg')
    fromaddr = 'mapspersonalization@gmail.com'
    toaddrs  = 'mapspersonalization@gmail.com'
    subject = "Maps Went Down"
    text = 'This is a warning that the maps fetcher has gone down. Please fix it ASAP'
    message = 'Subject: %s\n\n%s' % (subject, text)

    # Credentials (if needed)
    username = 'mapspersonalization'
    password = config.get('Section1', 'password')

    # The actual mail send
    server = smtplib.SMTP('smtp.gmail.com:587')
    server.starttls()
    server.login(username,password)
    server.sendmail(fromaddr, toaddrs, message)
    server.quit()

