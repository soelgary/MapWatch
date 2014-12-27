Maps Monitor
============

The purpose of Maps Monitor is to monitor the crawlers for the different map providers. It works by using reverse health checks. A reverse health check is when the crawler checks in with the monitor rather than the monitor checking in with the crawler. The reason for this is so that the monitor gets data as soon as it is available and because the monitor will not be able to make a connection to the crawlers for regular health checks.

There are two main parts to maps monitor. The first is the Flask API and the second is the UI.

#######UI
The UI talks to the Flask API to get data to display. It starts by loading the last 20 minutes worth of data for each of the map providers and displays it on graphs for each map provider. If there is no data, it graphs 0 requests for each time interval. 

#######Flask API
The Flask API handles all the data. It talks to the crawlers. For every request made to a map provider, the crawler sends a POST request to the Flask API. The crawler also sends a POST request when it finds a map that was updated.
