Maps Personalization
====================

This is a research project taking place at Northeastern University. The purpose is to develop an automated system to find and detect personalization for borders on different online maps. We are starting with Google Maps, but the algorithm will apply to any map provider.

How to Setup
============

##### Fill Database

Initially, the database must be filled with ```MapRequests```. A ```MapRequest``` is simply a set of variables that can be converted into a request to the Google Maps Static API endpoint. Run ```java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar -create``` to fill the database. Only do this once as it will always add new values to the db.

##### Fetch Maps
Now you can run the fetch job as many times as necessary. The command to do so is ```java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar -fetch```. Do not run this on any machine besides achtung because the API key will only work from that IP address. Also do not run this multiple times, because the current rate limit is .28 requests/second. This equates to over 24,000 requests/day. We are only allowed 25,000/day. 

##### Compare Maps
Once the fetch job is done you can run the comparison job by running ```java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar -compare```. This will email results to mapspersonalization@gmail.com. This job does not save anything to the db, so run as often as necessary.



Database Migrations
===================

The database in use is mysql. There are a few steps needed in order to perform a migration. 

Create Migration
================

First you need to modify the ```src/main/resources/migrations.xml``` file by adding a changeSet.


Build Fat Jar
=============

Next you need to build a new fat jar by running ```mvn clean compile assembly:single```.

Check Status
============

You now need to check the status of the database. It should tell you that there is 1 changeSet that has not been applied.

```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db status ../config.yaml
```

Tag Your Schema
================

Now you need to tag your schema. Please make sure to modify the command so that the date is correct

```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db tag ../config.yaml 2014-10-03-pre-user-move
```

Make the Migration
==================

We are ready to actually make the migration now.

```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db migrate ../config.yaml
```

Check Status
============

Check the status again. This time it should say that the database is up to date.

```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db status ../config.yaml
```

Wishlist
=========
1. Set up email alerts. Whenever we detect a new form of personalization, emails should be sent out to those involved in the project with details.
2. Set up an admin interface. Researchers involved should have an interface to see all of the personalization. We should be able to approve or disapprove it as well and approving it would make it public somehow.
3. Add capabilities for more map providers.
