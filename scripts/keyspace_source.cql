CREATE KEYSPACE temp_seq
  WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 1 };

USE temp_seq;

CREATE TABLE measurements (
  location_id text,
  date text,
  time timestamp,
  temperature double,
  PRIMARY KEY ((location_id, date), time)
);

