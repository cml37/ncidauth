# ncidauth
Currently, this project is deployed in RedHat OpenShift and is backed by MariaDB.  Here is a list of environment variables used in the project:

* GOOGLE_FCM_API_KEY
* GOOGLE_FCM_URL
* MARIA_DB_PASSWORD
* MARIA_DB_USERNAME
* MARIA_DB_HOSTNAME
* MARIA_DB_NAME

And here is some SQL code that was used to create a MariaDB table that maps users to passwords and users to FCM topics:

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