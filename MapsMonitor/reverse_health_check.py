from flask import Flask, request, jsonify
import sqlite3
from monitor_dao import MonitorDAO
from monitor import Monitor
import json

app = Flask(__name__)

@app.route('/', methods=['POST'])
def insert_monitor():
  monitor = MonitorDAO()
  incoming = request.get_json()
  monitor.insert(incoming)
  return jsonify(**incoming), 201

if __name__ == '__main__':
  app.run()