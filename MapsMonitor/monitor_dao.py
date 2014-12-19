import sqlite3

class MonitorDAO():
  def __init__(self):
    print('Connecting to database...')
    self.conn = sqlite3.connect('monitor.db')
    print('Connected to database...')
    self.cursor = self.conn.cursor()

  def insert(self, request):
    map_provider = request['mapProvider']
    start = request['start']
    end = request['end']
    errors = request['errors']
    requests = request['requests']
    values = (map_provider, start, end, errors, requests)
    self.cursor.execute('INSERT INTO monitor VALUES (?,?,?,?,?)', values)

  def select(self):
    print 'selecting'
    l = self.cursor.execute('SELECT * FROM monitor')
    for row in l:
      print row
    return "HEy"