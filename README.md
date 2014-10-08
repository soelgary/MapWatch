Maps Personalization
====================

This is a research project taking place at Northeastern University. The purpose is to develop an automated system to find and detect personalization for borders on different online maps. We are starting with Google Maps, but the algorithm will apply to any map provider.

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