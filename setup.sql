DROP DATABASE IF EXISTS tiny;
DROP USER IF EXISTS tiny;

CREATE DATABASE tiny;
CREATE USER tiny;
-- Don't forget to change the password here
ALTER USER tiny WITH PASSWORD 's3cr3t';
GRANT ALL PRIVILEGES ON DATABASE tiny TO tiny;
