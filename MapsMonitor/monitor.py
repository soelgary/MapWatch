import time
from monitor_error_dao import MonitorErrorDAO
from monitor_dao import MonitorDAO
import datetime, time
from smtp import SMTP

monitor_error = MonitorErrorDAO()
monitor = MonitorDAO()

email = SMTP()

eleven_minutes = 1000 * 60 * 11 # milliseconds * seconds * 11 minutes
three_seconds = 1000 * 3 # 3 seconds

def run_monitor():
  while True:
    print 'start'
    time.sleep(5)
    print 'awake'
    eleven_minutes_millis = int(round(time.time() * 1000)) - eleven_minutes
    eleven_minutes_google = monitor.select_map_provider(eleven_minutes_millis, 'google')
    eleven_minutes_bing = monitor.select_map_provider(eleven_minutes_millis, 'bing')
    three_second_millis = int(round(time.time() * 1000)) - three_seconds
    three_second_google = monitor.select_map_provider(three_second_millis, 'google')
    three_second_bing = monitor.select_map_provider(three_second_millis, 'bing')
    if not eleven_minutes_google and len(eleven_minutes_google) > 0:
      email.send()
      data = {
        "mapProvider": "google",
        "time": int(round(time.time() * 1000)),
        "level": "fatal"
      }
      monitor_error.insert(data)
    elif not three_second_google and len(three_second_google) > 0:
      data = {
        "mapProvider": "google",
        "time": int(round(time.time() * 1000)),
        "level": "low" 
      }
      monitor_error.insert(data)

    if not eleven_minutes_bing and len(eleven_minutes_bing) > 0:
      email.send()
      data = {
        "mapProvider": "bing",
        "time": int(round(time.time() * 1000)),
        "level": "fatal"
      }
      monitor_error.insert(data)
    elif not three_second_bing and len(three_second_bing) > 0:
      data = {
        "mapProvider": "bing",
        "time": int(round(time.time() * 1000)),
        "level": "low" 
      }
      monitor_error.insert(data)
    
if __name__ == '__main__':
  run_monitor()