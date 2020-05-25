DROP DATABASE IF EXISTS votedb;

CREATE DATABASE votedb;

CREATE ROLE pegauser WITH INHERIT LOGIN ENCRYPTED PASSWORD 'pegapass';

GRANT ALL PRIVILEGES ON DATABASE votedb TO pegauser;

ALTER DATABASE votedb OWNER TO pegauser;

\connect votedb

CREATE SCHEMA pega;

ALTER SCHEMA pega OWNER TO pegauser;

CREATE TABLE pega.votes
(
	countryfrom varchar(255) not null,
	votedfor varchar(255) not null,
	year smallint not null
);

alter table pega.votes owner to pegauser;

create index votes_year_index
	on pega.votes (year);

create index votes_year_votedfor_index
	on pega.votes (year, votedfor);

