from gevent import monkey
monkey.patch_all()
from monitor_dao import MonitorDAO
from monitor_error_dao import MonitorErrorDAO
from monitor_change_dao import MonitorChangeDAO
import time
from threading import Thread
from flask import Flask, render_template, session, request, jsonify
from flask.ext.socketio import SocketIO, emit, join_room, leave_room
import time

app = Flask(__name__)
app.debug = True
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

@app.route('/')
def index():
  return render_template('index.html')

@socketio.on('my event', namespace='/test')
def test_event(message):
  print message

@socketio.on('initialize', namespace='/test')
def test_initialize(message):
  print 'connecting'
  print message
  monitor = MonitorDAO()
  monitors = monitor.select(message['time'])
  emit('initialize', {'data': monitors})

@socketio.on('ping', namespace='/test')
def handle_ping(message):
  monitor_error = MonitorErrorDAO()
  errors = monitor_error.select(message['time'])
  emit('ping', {'errors': errors})

@socketio.on('disconnect', namespace='/test')
def test_disconnect():
    print('Client disconnected')

@app.route('/monitor', methods=['POST'])
def insert_monitor():
  monitor = MonitorDAO()
  incoming = request.get_json()
  monitor.insert(incoming)
  socketio.emit('monitor', incoming, namespace='/test')
  return jsonify(**incoming), 201

@app.route('/update/<mapProvider>', methods=['GET'])
def get_updates(mapProvider):
  change_dao = MonitorChangeDAO()
  if mapProvider == 'all':
    changes = change_dao.select_all()
    return jsonify(data=changes), 200
  read = request.args.get('read')
  if read == 'true':
    read = 1
  else:
    read = 0
  changes = change_dao.select(mapProvider)
  return jsonify(data=changes), 200

@app.route('/update/<mapProvider>/<id>/read/<read>', methods=['PUT'])
def set_read(mapProvider, id, read):
  change_dao = MonitorChangeDAO()
  if read == 'true':
    read = 1
  else:
    read = 0  
  change_dao.update(mapProvider, id, read)
  return '', 204


@app.route('/update', methods=['POST'])
def add_update():
  incoming = request.get_json()
  incoming['date'] = int(round(time.time() * 1000))
  change_dao = MonitorChangeDAO()
  change_dao.insert(incoming)
  socketio.emit('change', incoming, namespace='/test')
  '''
  { 
    "date", milliseconds
    "id": id,
    "mapProvider": mapProvider,
    "country": tldcc,
    "read": false,
    "old": {
      "fetchJob": fetchJob,
      "hash": hash,
      "path": path 
    },

    "new": {
      "fetchJob": fetchJob,
      "hash": hash,
      "path": path
    }
  }
  '''
  return jsonify(**incoming), 201

@app.route('/<mapProvider>')
def fetch_all_monitor(mapProvider):
  monitor = MonitorDAO()
  monitors = monitor.select_all()
  return jsonify(data=monitors), 200

@app.route('/errors')
def fetch_all_monitor_erros():
  monitor = MonitorErrorDAO()
  monitors = monitor.select_all()
  return jsonify(data=monitors), 200

@app.route('/<mapProvider>/<int:start>')
def fetch_monitor(mapProvider, start):
  monitor = MonitorDAO()
  monitors = monitor.select(start)
  return jsonify(data=monitors), 200

if __name__ == '__main__':
    socketio.run(app)
