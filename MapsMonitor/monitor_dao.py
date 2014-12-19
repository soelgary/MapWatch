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
    self.conn.commit()

  def select(self, start):
    response = []
    for row in self.cursor.execute('SELECT * FROM monitor WHERE start >= ?', (start,)):
      response.append({"mapProvider": row[0], "start": row[1], "end": row[2], "errors": row[3], "requests": row[4]})
    return response