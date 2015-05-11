import requests
import time

url = "http://127.0.0.1:9092maps/google/hits?approved=true&count=1000"
ten_minutes = 60 * 10

while True:
  r = requests.get(url)
  time.sleep(ten_minutes)