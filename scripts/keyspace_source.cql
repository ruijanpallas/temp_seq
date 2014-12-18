DROP KEYSPACE temp_seq;

CREATE KEYSPACE temp_seq
  WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 1 };

USE temp_seq;

CREATE TABLE measurements (
  location_id text,
  time timestamp,
  temperature double,
  PRIMARY KEY ((location_id), time)
);

CREATE TABLE daily_averages (
  location_id text,
  date text,
  average double,
  samples int,
  PRIMARY KEY (location_id, date)
);

