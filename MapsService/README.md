Context Memo
============
This document is a set of instructions for deploying a custom tool to detect personalization and border updates for Google Maps and Bing Maps. The document is designed for members of the research group working on the project, as well as any outside researcher looking to validate our results. I decided to write the document using Markdown. Markdown is an incredibly easy to use/learn markup language that has a very clean look when displayed. It is used by many developers to create readme files for their code(much like this document). There are 3 main sections to this document. The first is just an overview saying who created it, contact info, and where to look for more background. The second section explains the capabilities of the code and the third is an in depth explanation of how to set it up and all of the necessary commands to run the experiment. Initially, the only people that will be exposed to this document are the 4 team members working on the project and for the most part, they will use it to reference the commands and HTTP endpoints. They wont need to know much about how to set it up because that work is already done. When we decide to release the results, we will release the code with it so any interested reader can use the tool on their own. At this point in time, all of the information in the document will be critical to setting up the experiments, to analyzing the results.


MapsService
===========

This is the backend to the NEU Maps Personalization Project. Background information can be found at personalization.ccs.neu.edu. The following will be a set of instructions on how to set up this service. If anything is broken or confusing, contact us at soelgary@ccs.neu.edu.

What can this code do?
======================

This code is all of the backend code for our map personalization detecting tool. It can do quite a few things.
1. Crawl Google Maps and Bing Maps
3. Create and host Amazon Mechanical Turk HIT's
4. REST api for admin interface to MTurk responses

Getting Started
===============

### Set Up Maven Repository

In order to get started you need to set up the maven repository. Most of the repos are publicly available, but the MTurk libraries are not. You need to download and extract the MTurk SDK from [here]("http://sourceforge.net/projects/mturksdk-java/"). The jar files we need to install are in the lib folder. Run the following commands to install the jar files to your local maven repository.

```
mvn install:install-file -Dfile=aws-mturk-wsdl.jar -DgroupId=com.amazonaws -DartifactId=wsdl -Dversion=1.0 -Dpackaging=jar -X
mvn install:install-file -Dfile=java-aws-mturk.jar -DgroupId=com.aws.mturk -DartifactId=wsdl -Dversion=1.0 -Dpackaging=jar -X
```

All of the other libraries will be pulled in during the build process later on.

### Set up the Config

You will need to create your own .yaml config file, mturk properties file and properties file. The .yaml config contains all of the information it needs to know about the mysql database and http server. Simply copy either of the .yaml configs and replace the machine specific values(usernames, passwords, names, ports) and you will be all set.

Next we need to create a properties file. This is used to provide instructions on config settings like where to save images, where mturk property information is located, etc. Again, simply copy achtung.properties or maps.properties(located in the src/main/resources folder) changing the settings for the computer you intend to work on.

Finally, if you decide to run the MTurk experiment, you will need to create an mturk.properties file. This should look like the following
```
access_key=your-acccess-key
secret_key=your-secret-key

#service_url=https://mechanicalturk.amazonaws.com/?Service=AWSMechanicalTurkRequester
```
If the ```service_url``` property is commented out, you will be using MTurk in sandbox mode. If you uncomment it, you will use MTurk in production. It is highly recommended to test in sandbox mode to make sure everything is working properly.

### Build

Now we are able to build the code. ```cd``` into the ```MapsService``` directory and run
```
mvn package
```

This will output an executable jar file in the ```target``` directory.

### Set up the Database

Before we can run the crawler or an experiment, we need to set up mysql. This can be done by running a database migration. Execute the following commands to run the migration.

Start by checking the status of the DB
```
java -jar MapsService-0.0.1-SNAPSHOT.jar db status ../your-config.yaml
```

Now tag the schema
```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db tag ../your-config.yaml YYYY-MM-DD-pre-user-move
```

Run the migration
```
java -jar MapsService-0.0.1-SNAPSHOT-jar-with-dependencies.jar db migrate ../your-config.yaml
```

Check the status to ensure the migration was successful
```
java -jar MapsService-0.0.1-SNAPSHOT.jar db status ../your-config.yaml
```

### Fill the MapRequest tables

Next we need to load the MapRequest tables. These are used to store the tiles metadata in order to fetch the same tiles every crawl. They contain information like latitude, longitude, zoom, height, width, and country code. Run the following command and fill in ```mapProvider``` with ```google``` or ```bing```.

```
java -jar MapsService-0.0.1-SNAPSHOT.jar -create -mp `mapProvider`
```

### Start the Crawler

We have the ability to crawl any map that has the MapRequest table completed. We only support Google and Bing, so we can only crawl Google and Bing. To start the crawler run the following command where ```mapProvider``` is ```google``` or ```bing```.

```
java -jar MapsService-0.0.1-SNAPSHOT.jar -fetch 1 -mp `mapProvider`
```

### Create MTurk HITs

At the end of each full crawl you might want to send HITs to MTurk to analyze the updated tiles. The process of doing this is
1. Create the meta-data for each HIT and add it to the queue of HITs to be approved
2. Approve HITs in the queue, sending them to MTurk

### Create HIT Metadata and Queue the HIT

These are two steps that are completed with the following command where ```n``` is the crawl number and must be greater than 1.

```
java -jar target/MapsService-0.0.1-SNAPSHOT.jar -update `n`
```

Now all of the data is exposed via a REST api. The main reason it is exposed via a REST api rather than a CLI is so it is guarenteed the HTTP server will be up when the HITs are sent to MTurk.

### Start HTTP Server

Since we will be communicating with the HTTP server rather than the CLI, we need to start the server with the following command.

```
java -jar MapsService/target/MapsService-0.0.1-SNAPSHOT.jar server MapsService/achtung.yaml
```

#### Start Supervisor

Since we need to ensure the server will always be running, even when it crashes you can choose to start it with supervisor instead. First you will need to install supervisor using pip.

```
pip install supervisor
```

Then you can use the current supervisor config(or create your own) and run

```
supervisord -c supervisor.conf
```

### Approve HITs in the Queue

In order to approve HITs in the queue, we need to send an HTTP POST request to the following endpoint with the following payload.
```
POST https://your-domain-name/maps/maps/{mapProvider}/hits/approve

```
```json
{
  "count": 10
}
```

```count``` is a variable that represents the number of HITs to approve.
```mapProvider``` is ```google``` or ```bing```

### Analyze Results
By putting the ```MapsUI``` folder behind an HTTP server on the same host as the Maps Server, you can view the admin interface at https://your-domain-name/maps/admin. This will show a search bar for the MTurk results and will allow you to override any Mturk response.
