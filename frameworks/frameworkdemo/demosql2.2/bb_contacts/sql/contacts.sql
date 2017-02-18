CREATE SCHEMA sql2demo
  AUTHORIZATION fhir;



CREATE TABLE sql2demo.contacts
(
  id character varying(64) NOT NULL DEFAULT uuid_in((md5(((random())::text || (now())::text)))::cstring),
  email_address character varying(128),
  first_name character varying(128),
  last_name character varying(128),
  description character varying(1024),
  CONSTRAINT sd_contacts_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sql2demo.contacts
  OWNER TO fhir;
GRANT ALL ON TABLE sql2demo.contacts TO fhir;
