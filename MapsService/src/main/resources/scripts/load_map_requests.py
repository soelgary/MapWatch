#!/usr/bin/python

import requests
import json

HOST = 'http://127.0.0.1:8080'

PATH = '/maps'

INPUT_FILE = 'points.txt'

X_DIMENSION = 600
Y_DIMENSION = 600
LANGUAGE = "English"
HEADERS = {'content-type': 'application/json'}

startLat = -90;
startLon = -180;
maxLat = 90;
maxLon = 180;
latMultiple = 8;
lonMultiple = 15;
zoom = 6

CC_TLD = ["ac", "ad", "ae", "af", "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "ax", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz", 
          "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cr", "cs", "cu", "cv", "cw", "cx", "cy", "cz", "dd", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "er", "es", "et", "eu", "fi", "fj",
          "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gg", "gh", "gi", "gl", "gm", "gn", "gp", "gq", "gr", "gs", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "im", "in", "io", "iq", "ir",
          "is", "it", "je", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md", "me", "mg", "mh", "mk", "ml", "mm",
          "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nu", "nz", "om", "pa", "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "ps",
          "pt", "pw", "py", "qa", "re", "ro", "rs", "ru", "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "ss", "st", "su", "sv", "sx", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj",
          "tk", "tl", "tm", "tn", "to", "tp", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "uz", "va", "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "ye", "yt", "yu", "za", "zm", "zr", "zw"]

def make_request(location, zoom, cc_tld):
  payload = {
    "location": {
      "id": location,
    },
    "zoom": zoom,
    "xDimension": X_DIMENSION,
    "yDimension": Y_DIMENSION,
    "region": cc_tld,
    "language": LANGUAGE
  }
  
  r = requests.post(HOST + PATH, data=json.dumps(payload), headers=HEADERS)

def add_location(latitude, longitude):
  payload = {
    "latitude": latitude,
      "longitude": longitude
  }

  r = requests.post(HOST + PATH + '/location', data=json.dumps(payload), headers=HEADERS)
  return r.json()['id']

def load_data():
  for line in open(INPUT_FILE):
    data = line.strip().split(" ")
    latitude = data[1]
    longitude = data[2]
    zoom = data[3]
    loc_id = add_location(latitude, longitude)
    for cc_tld in CC_TLD:
      make_request(loc_id, zoom, cc_tld)

def load_all_requests():
  lat = startLat
  while lat < maxLat:
    lon = startLon
    while lon < maxLon:
      print str(lat) + '\t' + str(lon)
      loc_id = add_location(lat, lon)
      for cc_tld in CC_TLD:
        make_request(loc_id, zoom, cc_tld)
      lon += lonMultiple
    lat += latMultiple

#load_data()
load_all_requests()