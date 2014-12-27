import sqlite3

class MonitorChangeDAO():
  def __init__(self):
    print('Connecting to database...')
    self.conn = sqlite3.connect('monitor.db')
    print('Connected to database...')
    self.cursor = self.conn.cursor()

  def insert(self, change):
    old = change['old']
    new = change['new']
    oldFetchJob = old['fetchJob']
    newFetchJob = new['fetchJob']
    oldPath = old['path']
    newPath = new['path']
    oldHash = old['hash']
    newHash = new['hash']
    tldcc = change['country']
    mapProvider = change['mapProvider']
    date = change['date']
    read = False
    values = (oldFetchJob, newFetchJob, oldPath, newPath, oldHash, newHash, tldcc, mapProvider, read, date)
    self.cursor.execute('INSERT INTO monitorchange(oldFetchJob, newFetchJob, oldPath, newPath, oldHash, newHash, country, mapProvider, read, time) VALUES (?,?,?,?,?,?,?,?,?,?)', values)
    self.conn.commit()

  def select_all_unread(self, mapProvider):
    response = []
    values = (mapProvider,)
    for row in self.cursor.execute('SELECT * FROM monitorchange WHERE mapProvider = ? AND READ = 0', values):
      print row
      response.append(
        {
          "date": row[10],
          "country": row[7],
          "mapProvider": row[8],
          "read": row[9],
          "id": row[0],
          "old": {
            "fetchJob": row[1], 
            "path": row[3],
            "hash": row[5]
          },
          "new": {
            "fetchJob": row[2], 
            "path": row[4], 
            "hash": row[6]
          }
        })
    return response

  def select_all(self):
    response = []
    for row in self.cursor.execute('SELECT * FROM monitorchange'):
      print row
      response.append(
        {
          "date": row[10],
          "country": row[7],
          "mapProvider": row[8],
          "read": row[9],
          "id": row[0],
          "old": {
            "fetchJob": row[1], 
            "path": row[3],
            "hash": row[5]
          },
          "new": {
            "fetchJob": row[2], 
            "path": row[4], 
            "hash": row[6]
          }
        })
    return response    

  def select(self, mapProvider):
    response = []
    values = (mapProvider,)
    for row in self.cursor.execute('SELECT * FROM monitorchange WHERE mapProvider = ?', values):
      print row
      response.append(
        {
          "date": row[10],
          "country": row[7],
          "mapProvider": row[8],
          "read": row[9],
          "id": row[0],
          "old": {
            "fetchJob": row[1], 
            "path": row[3],
            "hash": row[5]
          },
          "new": {
            "fetchJob": row[2], 
            "path": row[4], 
            "hash": row[6]
          }
        })
    return response

  def update(self, mapProvider, id, read):
    values = (read, id, mapProvider,)
    self.cursor.execute('UPDATE monitorchange SET read = ? WHERE id = ? AND mapProvider = ?', values)
    self.conn.commit()
