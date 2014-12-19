default:
	# Monitoring system for maps personalization

monitor:
	python monitor.py

flask:
	python reverse_health_check.py

run:
	nohup python reverse_health_check.py &
	nohup python monitor.py &