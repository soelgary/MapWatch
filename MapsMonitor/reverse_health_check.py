from gevent import monkey
monkey.patch_all()
from monitor_dao import MonitorDAO
from monitor_error_dao import MonitorErrorDAO
from monitor_change_dao import MonitorChangeDAO
import time
from threading import Thread
from flask import Flask, render_template, session, request, jsonify
from flask.ext.socketio import SocketIO, emit, join_room, leave_room

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
  print message
  monitor_error = MonitorErrorDAO()
  errors = monitor_error.select(message['time'])
  emit('ping', {'errors': errors})

@socketio.on('disconnect', namespace='/test')
def test_disconnect():
    print('Client disconnected')

@app.route('/monitor', methods=['POST'])
def insert_monitor():
  print 'incoming request'
  print request
  monitor = MonitorDAO()
  incoming = request.get_json()
  print incoming
  monitor.insert(incoming)
  socketio.emit('monitor', incoming, namespace='/test')
  return jsonify(**incoming), 201


@app.route('/<mapProvider>/change', methods=['GET'])
def get_changes(mapProvider):
  change_dao = MonitorChangeDAO()
  changes = change_dao.select_all(mapProvider)
  return jsonify(data=changes), 200

@app.route('/change', methods=['POST'])
def add_change():
  incoming = request.get_json()
  print incoming
  change_dao = MonitorChangeDAO()
  change_dao.insert(incoming)
  socketio.emit('change', incoming, namespace='/test')
  '''
  { 
    "mapProvider": mapProvider,
    "country": tldcc,
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
  print monitors
  return jsonify(data=monitors), 200

@app.route('/errors')
def fetch_all_monitor_erros():
  monitor = MonitorErrorDAO()
  monitors = monitor.select_all()
  print monitors
  return jsonify(data=monitors), 200

@app.route('/<mapProvider>/<int:start>')
def fetch_monitor(mapProvider, start):
  monitor = MonitorDAO()
  monitors = monitor.select(start)
  #print monitors
  return jsonify(data=monitors), 200

if __name__ == '__main__':
    socketio.run(app)
