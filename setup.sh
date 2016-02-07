#!/usr/bin/env bash

###NOTES!!!
#Make sure postgres is installed and running
#Make this script executable
# chmod 755 setup.sh
#create a glassfish-pwd.txt file in the root of this project containing this text with pwd ofcourse and without the #.
#AS_ADMIN_PASSWORD=YourPassWordHere

# Change the following command to the location where your psql command resides.
PSQL_COMMAND="/Applications/Postgres.app/Contents/Versions/9.4/bin/psql"
#This command will retrieve the default postgres username. Change if necessary
DEFAULT_PSQL_USER=$(${PSQL_COMMAND} --help|grep username=USERNAME|awk -F'["]' '{print $2; }')

# The Glassfish domain (default: "domain1")
DOMAIN="domain1"
GLASSFISH_LIB="/usr/local/Cellar/glassfish/4.1/libexec/glassfish/domains/${DOMAIN}/lib/"


#stop Glassfish
asadmin stop-domain ${DOMAIN}

#Clean project
mvn clean

# Creates two databases: One for "PROD" and one for "TEST"
${PSQL_COMMAND} -U ${DEFAULT_PSQL_USER} -d template1 -a -f ./setup.sql
${PSQL_COMMAND} -U ${DEFAULT_PSQL_USER} -d template1 -a -f ./setup-test.sql

#remove possible existing postgres jars from glassfish domain lib
rm -fv ${GLASSFISH_LIB}/postgres*.jar

#Get the jdbc jar
mvn dependency:copy-dependencies -f ./setup-pom.xml

# Copy the postgres lib to glassfish lib so as not to have to bundle it in the war and
# enable usage of application server resources
cp ./target/dependency/postgresql*.jar /usr/local/Cellar/glassfish/4.1/libexec/glassfish/domains/domain1/lib/

#Start Glassfish again
asadmin start-domain ${DOMAIN}

# Delete the connection pool if it exists
asadmin --user admin --passwordfile ./glassfish-pwd.txt delete-jdbc-resource jdbc/tiny
asadmin --user admin --passwordfile ./glassfish-pwd.txt delete-jdbc-connection-pool tinyConnectionPool

# When Derby
#asadmin --user admin --passwordfile ./glassfish-pwd.txt create-jdbc-connection-pool --datasourceclassname javax.sql.ConnectionPoolDataSource --restype javax.sql.XADataSource --property portNumber=1527:password=APP:user=APP:serverName=localhost:databaseName=tiny:connectionAttributes=\;create\\=true tinyConnectionPool
#asadmin --user admin --passwordfile ./glassfish-pwd.txt create-jdbc-resource --connectionpoolid tinyConnectionPool jdbc/tiny

# When PostgreSQL
## Create a JDBC Connection Pool
asadmin --user admin --passwordfile ./glassfish-pwd.txt create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource --restype javax.sql.ConnectionPoolDataSource --property portNumber=5432:password=bYuzY6E=:user=tiny:serverName=localhost:databaseName=tiny tinyConnectionPool
## Create a JNDI DataSource
asadmin --user admin --passwordfile ./glassfish-pwd.txt create-jdbc-resource --connectionpoolid tinyConnectionPool jdbc/tiny

#Cleanup
mvn clean
asadmin stop-domain ${DOMAIN}
