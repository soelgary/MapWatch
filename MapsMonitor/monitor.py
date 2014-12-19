import time
from monitor_dao import MonitorDAO
import datetime, time
from smtp import SMTP

monitor = MonitorDAO()

email = SMTP()

diff = 1000 * 60 * 11 # milliseconds * seconds * 11 minutes

def run_monitor():
  while True:
    time.sleep(5)
    millis = int(round(time.time() * 1000)) - diff
    rows = monitor.select(millis)
    if not rows:
      email.send()


if __name__ == '__main__':
  run_monitor()