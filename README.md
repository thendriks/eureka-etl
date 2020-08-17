# eureka-etl
This application automates the registration of binary data into DSpace for the EUREKA project. A CSV input is used based on a EUREKA ontology to download binary objects, structure them in a folder hierarchy based on a DSpace standard, and add the metadata as XML files. Subsequently, a custom DSpace API call is made to have DSpace bulk register the objects. 

# Building EUREKA ETL
To build the application, [make sure maven is installed](https://maven.apache.org/install.html). Then run:
```
mvn clean package
```

# Configuring EUREKA ETL
Move the target folder to a desired location and update the application.yml accordingly
```
server:
  port: Changes the port for the embedded server

bitstream:
  dspace-url: The root URL the DSpace application can be reached on
  registration-path: The path to the custom DSpace registration endpoint
  collection-id: The collection all objects are uploaded to (should not be static in the future)
  asset-folder: The local path to the DSpace assetstore used to store the knowledge objects
  metadata-folder: The local path to the folder used to create the folder structure required by DSpace for registration
  dspace-metadata-folder: The path from which DSpace will reach the metadata folder in the docker container
  dspace-asset-folder: The path from which DSpace will reach the asset folder in the docker container

crawler:
  metadata-csv: Path to the CSV containing the metadata
```

# Running EUREKA ETL
Move the target folder to a desired location and update the application.yml accordingly. Either set the $DSPACE_URL environment variable to the url of your running DSpace instance or hardcode the values in the configuration. To run the application use:
```
java -jar eureka-etl-{VERSION}.jar
```
Currently the application starts a register on startup. In the future this will be changed to a configurable cron so that the registration is run on a time interval.
