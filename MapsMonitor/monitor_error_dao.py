import sqlite3

class MonitorErrorDAO():
  def __init__(self):
    print('Connecting to database...')
    self.conn = sqlite3.connect('/home/ubuntu/monitor.db')
    print('Connected to database...')
    self.cursor = self.conn.cursor()

  def insert(self, request):
    map_provider = request['mapProvider']
    time = request['time']
    level = request['level']
    values = (map_provider, time, level)
    self.cursor.execute('INSERT INTO monitorerror VALUES (?,?,?)', values)
    self.conn.commit()

  def select(self, time):
    response = []
    for row in self.cursor.execute('SELECT * FROM monitorerror WHERE time >= ?', (time,)):
      response.append({"mapProvider": row[0], "time": row[1], "level": row[2]})
    return response

  def select_all(self):
    response = []
    for row in self.cursor.execute('SELECT * FROM monitorerror'):
      response.append({"mapProvider": row[0], "time": row[1], "level": row[2]})
    return response
