import sqlite3
conn = sqlite3.connect('/home/ubuntu/monitor.db')

c = conn.cursor()

c.execute('''CREATE TABLE monitor
             (mapProvider text, time int)''')

c.execute('''CREATE TABLE monitorerror
             (mapProvider text, time int, level text)''')


conn.commit()

conn.close()
