Maps Personalization
====================

This is a research project taking place at Northeastern University. The purpose is to develop an automated system to find and detect personalization for borders on different online maps. We are starting with Google Maps, but the algorithm will apply to any map provider.

How to Setup
============

##### Fill Database

Initially, the database must be filled with ```MapRequests```. A ```MapRequest``` is simply a set of variables that can be converted into an HTTP request. Run ```java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar -create MapNumber -mp MapProvider``` to fill the database. ```MapNumber``` is an int where it represents an id for the set of tiles. This is mainly used if you change lat/lon intervals or zoom level. ```MapProvider``` is a String representing the map provider. Right now the only valid option is ```google```. The plan is to also add ```bing``` and ```openstreet``` in the future.

##### Fetch Maps
Now you can run the fetch job as many times as necessary. The command to do so is ```java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar -fetch MapNumber -mp MapProvider```. ```MapNumber``` is the same as the ```MapNumber``` provided in the previous job and ```MapProvider``` is also the same.

##### Compare Maps
Once the fetch job is done you can run the comparison job by running ```java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar -compare FetchJob -mp MapProvider```. Here, ```FetchJob``` is the id of the ```FetchJob``` you are looking to compare and ```MapProvider``` is still the same. This will email results to mapspersonalization@gmail.com. This job does not save anything to the db, so run as often as necessary.



Database Migrations
===================

The database in use is mysql. There are a few steps needed in order to perform a migration. 

#####Create Migration

First you need to modify the ```src/main/resources/migrations.xml``` file by adding a changeSet.


#####Build Fat Jar

Next you need to build a new fat jar by running ```mvn clean compile assembly:single```.

#####Check Status

You now need to check the status of the database. It should tell you that there is 1 changeSet that has not been applied.

```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db status ../config.yaml
```

#####Tag Your Schema

Now you need to tag your schema. Please make sure to modify the command so that the date is correct

```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db tag ../config.yaml 2014-10-03-pre-user-move
```

#####Make the Migration

We are ready to actually make the migration now.

```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db migrate ../config.yaml
```

#####Check Status

Check the status again. This time it should say that the database is up to date.

```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db status ../config.yaml
```

Contribution
============

Want to add a functionality for a new map provider? Here are the steps you have to take..

1. Implement the following dao interfaces.
  1. ```FetchJobDao``` - This table describes the id of the specific crawl
  2. ```MapDao``` - This describes a tile/MapRequest that was fetched
  3. ```MapRequestDao``` - This describes a tile to be fetched over multiple crawls
2. Implement the following POJO's interfacess
  1. ```Map``` - Describes a tile/MapRequest that was fetched
  2. ```MapRequest``` - Describes a tile to be fetched over multiple crawls
3. Modify ```RequestJob.java``` to handle loading ```MapRequest```s for the new map provider starting in the ```execute``` method.
4. Modify ```FetchJob.java``` to handle configuring the fetcher to fetch your new map provider by starting in the ```execute``` method
5. Modify ```ComparisonJob.java``` to handle configuring the comparator job to handle the new map provider by starting in the ```execute``` method
