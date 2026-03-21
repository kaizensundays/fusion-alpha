DROP TABLE IF EXISTS find_flight;
DROP TABLE IF EXISTS airline;

CREATE TABLE airline
(
    id bigserial NOT NULL,
    code character varying(8)  NOT NULL,
    name character varying(32) NOT NULL,
    CONSTRAINT airline_pk PRIMARY KEY (id),
    CONSTRAINT airline_code_unq UNIQUE (code)
);

CREATE TABLE find_flight
(
    uuid    character varying(36) NOT NULL,
    user_id character varying(8)  NOT NULL,
    ip      character varying(15) NOT NULL,
    init    character varying(3)  NOT NULL,
    dest    character varying(3)  NOT NULL,
    depart  timestamp             NOT NULL,
    goback  timestamp             NOT NULL,
    updated timestamp             NOT NULL,
    CONSTRAINT find_flight_pk PRIMARY KEY (uuid)
);

select * from find_flight;
select count(*) from find_flight;
