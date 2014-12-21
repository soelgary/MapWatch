import sqlite3

class MonitorDAO():
  def __init__(self):
    print('Connecting to database...')
    self.conn = sqlite3.connect('monitor.db')
    print('Connected to database...')
    self.cursor = self.conn.cursor()

  def insert(self, request):
    map_provider = request['mapProvider']
    time = request['time']
    values = (map_provider, time)
    self.cursor.execute('INSERT INTO monitor VALUES (?,?)', values)
    self.conn.commit()

  def select(self, time):
    response = []
    for row in self.cursor.execute('SELECT * FROM monitor WHERE time >= ?', (time,)):
      response.append({"mapProvider": row[0], "time": row[1]})
    return response

  def select_all(self):
    response = []
    for row in self.cursor.execute('SELECT * FROM monitor'):
      response.append({"mapProvider": row[0], "time": row[1]})
    return response