import requests

url = 'http://127.0.0.1:9090/maps/google/updates?count=1'

for i in range(0, 30):
  r = requests.get(url)
  print r.status_code
  print r.reason
  print r.json()
