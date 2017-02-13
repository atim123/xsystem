CREATE SCHEMA sql2demo
  AUTHORIZATION fhir;




CREATE TABLE sql2demo.persons
(
  id character varying(64) NOT NULL DEFAULT uuid_in((md5(((random())::text || (now())::text)))::cstring),
  firstname character varying(1024),
  surname character varying(1024),
  patname character varying(1024),
  email character varying(128),
  phone character varying(128),
  CONSTRAINT cp_patient_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sql2demo.persons
  OWNER TO fhir;
GRANT ALL ON TABLE sql2demo.persons TO fhir;
