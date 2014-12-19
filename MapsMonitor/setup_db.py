import sqlite3
conn = sqlite3.connect('monitor.db')

c = conn.cursor()

c.execute('''CREATE TABLE monitor
             (mapProvider text, start int, end int, errors int, requests int)''')

conn.commit()

conn.close()