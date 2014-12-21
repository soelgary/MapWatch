from gevent import monkey
monkey.patch_all()
from monitor_dao import MonitorDAO
from monitor_error_dao import MonitorErrorDAO
import time
from threading import Thread
from flask import Flask, render_template, session, request, jsonify
from flask.ext.socketio import SocketIO, emit, join_room, leave_room

app = Flask(__name__)
app.debug = True
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

thread = None


def background_thread():
    """Example of how to send server generated events to clients."""
    count = 0
    while True:
        time.sleep(2)
        count += 1
        socketio.emit('my response',
                      {'data': 'Server generated event', 'count': count},
                      namespace='/test')


@app.route('/')
def index():
    global thread
    if thread is None:
        thread = Thread(target=background_thread)
        thread.start()
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
  print monitors
  return jsonify(data=monitors), 200


if __name__ == '__main__':
    socketio.run(app, host='0.0.0.0')
