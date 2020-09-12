#ncidauth

## Creating database

To use NCIDAuth, you will want to create a database.  Based on the configuration example below, I suggest the following:
* MariaDB database named ncidauth_db
* MariaDB database user called ncidauth-user


## Configuring database

Here is some SQL code that was used to create a MariaDB table that maps users to passwords and users to FCM topics:

```
CREATE TABLE `users` (
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL
);
```

To insert a record into the table, you would use this syntax

```
INSERT INTO users (username,password,topic) VALUES ('myusername','mypassword','mytopic');
```

## Building Project
In Eclipse, choose export runnable jar file and then choose "extract required libraries into runnable JAR

## Testing project

### Getting a topic
```
curl --location --request GET 'https://localhost/V1/getTopic' \
--header 'Authorization: Basic <base64_username_and_password>'
```

### Sending a message
```
curl --location --request POST 'https://localhost/V1/sendMessage' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic <base64_username_and_password' \
--data-raw '{
    "message": "CID: *DATE*03012017*TIME*0919*LINE*GTALK*NMBR*1234567890*MESG*NONE*NAME*Chris Cell*"
}'
```

## Standard deployment
This project can be executed as a runnable JAR, as long as environment variables are exported before launching

Example bash script:

```
#!/bin/bash
export MARIA_DB_NAME=ncidauth_db
export MARIA_DB_HOSTNAME=localhost
export MARIA_DB_USERNAME=ncidauth-user
export MARIA_DB_PASSWORD=<password>
export GOOGLE_FCM_API_KEY=<your api key>
export GOOGLE_FCM_URL=https://fcm.googleapis.com/fcm/send
export KEYSTORE_PASSWORD=<keystore_password>
export WEB_SERVER_HOST_PORT=443
java -jar /data/ncidauth/ncidauth.jar > /var/log/ncidauth.log 2>&1 &
```

## OpenShift deployment
This project can be deployed in RedHat OpenShift and is backed by MariaDB.  Here is a list of environment variables used in the project:

* GOOGLE_FCM_API_KEY
* GOOGLE_FCM_URL
* MARIA_DB_PASSWORD
* MARIA_DB_USERNAME
* MARIA_DB_HOSTNAME
* MARIA_DB_NAME