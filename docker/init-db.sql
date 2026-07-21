-- Creates one database per microservice inside the single Postgres container.
-- Runs automatically the first time the container starts (docker-entrypoint-initdb.d).
CREATE DATABASE user_db;
CREATE DATABASE rescue_db;
CREATE DATABASE volunteer_db;
CREATE DATABASE shelter_db;
CREATE DATABASE notification_db;
