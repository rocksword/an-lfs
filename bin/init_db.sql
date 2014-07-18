drop database if exists lfs;
create database lfs;

drop table if exists team;
drop table if exists company;
drop table if exists matchinfo;
drop table if exists claimrate;

CREATE TABLE matchinfo
(
  id integer CONSTRAINT firstkey PRIMARY KEY,
  year varchar(4),
  index integer NOT NULL,
  time varchar(20),
  host varchar(10),
  guest varchar(10),
  score varchar(5),
  win float4,
  draw float4,
  lose float4,
  CONSTRAINT uq_matchinfo UNIQUE (year, index, host, guest)
)
WITH ( OIDS=FALSE ); 
ALTER TABLE matchinfo OWNER TO postgres;

INSERT INTO public.matchinfo(year, index, time, host, guest, score, win, draw, lose) VALUES('2999', 1, '08-20 20:00', 'Bai', '9:9', 'Meng', 1.8, 3.0, 2.8)