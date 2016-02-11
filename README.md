# tiny

Tiny is a service for making external links be represented as a domain link.

This site was a small exercise to create a tinyurl like service.
It is actually much easier than I expected.

Because it is an exercise I did not put much effort in creating


## Demo
[Ivo2U.nl](http://ivo2u.nl)


## Installation


### Prerequisites
* Maven installed
* Postgres database installed and running
* Glassfish application server installed and running

### Setup
see the setup.sh file for more information on installing stuff.

### JPA
Just to make life easy and eliminating the manual creation of the tiny table do the following:
* Uncomment the line in persistence.xml containing the `drop-and-create` text. This will enable the app to create the table in the database the first time you deploy the war.
* `mvn install` to build the app. This will create a war in .\artifact
* Install the war on glassfish. To make it easy for you, you can do this by copying the war to `[...]/glassfish/domains/domain1/autodeploy` folder.
* This will deploy the app on glassfish and create the table in the database.
* Now comment out the the `drop-and-create` line again in the persistence.xml file, otherwise you will loose all information everytime the app is deployed or restarted.

### Actions 
* First create a database called `tiny`. 
* Create a database connection in glassfish as described below. You might have to translate some of the commands to your own situation

### Apache front
If you want to set this service up for realzlike you might need something like this in your apache configuration.

```xml
<VirtualHost *:80>
    ServerName ivo2u.nl

    ProxyRequests Off
    ProxyPreserveHost On
    ProxyPass / http://localhost:8888/tiny/
    ProxyPassReverse / http://localhost:8888/tiny/

    LogLevel info
    CustomLog /var/log/apache2/tiny-access.log combined
</VirtualHost>
```

# Reading material
If you want to know more about this step please read [this](http://ivo2u.nl/WU).
