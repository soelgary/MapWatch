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
    values = (oldFetchJob, newFetchJob, oldPath, newPath, oldHash, newHash, tldcc, mapProvider)
    self.cursor.execute('INSERT INTO monitorchange VALUES (?,?,?,?,?,?,?,?)', values)
    self.conn.commit()

  def select_all(self, mapProvider):
    response = []
    values = (mapProvider,)
    for row in self.cursor.execute('SELECT * FROM monitorchange WHERE mapProvider = ?', values):
      response.append(
        {
          "country": row[6],
          "mapProvider": row[7],
          "old": {
            "fetchJob": row[0], 
            "path": row[2],
            "hash": row[4]
          },
          "new": {
            "fetchJob": row[1], 
            "path": row[3], 
            "hash": row[5]
          }
        })
    return response