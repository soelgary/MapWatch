import sqlite3
conn = sqlite3.connect('/home/ubuntu/monitor.db')

c = conn.cursor()

c.execute('''CREATE TABLE IF NOT EXISTS monitor
             (mapProvider text, time int)''')

c.execute('''CREATE TABLE IF NOT EXISTS monitorerror
             (mapProvider text, time int, level text)''')

c.execute('''CREATE TABLE IF NOT EXISTS monitorchange
             (ID INTEGER PRIMARY KEY, oldFetchJob int, newFetchJob int, oldPath text, newPath text, oldHash text, newHash text, country text, mapProvider text, read boolean, time int)''')

conn.commit()

conn.close()
