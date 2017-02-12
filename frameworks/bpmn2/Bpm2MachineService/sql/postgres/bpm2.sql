CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA bpmn2
  AUTHORIZATION fhir;



ALTER SCHEMA bpmn2 OWNER TO fhir;


CREATE TABLE bpmn2.definitions
(
  id character varying(512) NOT NULL,
  name character varying(4096) NOT NULL,
  created timestamp without time zone NOT NULL,
  updated timestamp without time zone NOT NULL,
  content text NOT NULL,
  CONSTRAINT bpmn2_definitions_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.definitions
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.definitions TO fhir;


CREATE TABLE bpmn2.processdefinition
(
  id character varying(512) NOT NULL,
  definitionid character varying(512) NOT NULL,
  name character varying(4096),
  CONSTRAINT bpmn2_processdefinition_pkey PRIMARY KEY (id),
  CONSTRAINT bpmn2_processdefinition_fkey1 FOREIGN KEY (definitionid)
      REFERENCES bpmn2.definitions (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.processdefinition
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.processdefinition TO fhir;


CREATE TABLE bpmn2.process
(
  id character varying(512) NOT NULL,
  processdefinition character varying(512) NOT NULL,
  created timestamp without time zone NOT NULL,
  finished timestamp without time zone,
  CONSTRAINT bpmn2_process_pkey PRIMARY KEY (id),
  CONSTRAINT bpmn2_process_fkey1 FOREIGN KEY (processdefinition)
      REFERENCES bpmn2.processdefinition (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.process
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.process TO fhir;


CREATE TABLE bpmn2.token
(
  id character varying(512) NOT NULL,
  parent character varying(512),
  isactive boolean NOT NULL,
  process character varying(512) NOT NULL,
  activity character varying(512) NOT NULL, -- FK -> BPMN model of flowelement attr ID
  loopcounter integer,
  CONSTRAINT bpmn2_token_pkey PRIMARY KEY (id),
  CONSTRAINT bpmn2_token_fkey1 FOREIGN KEY (process)
      REFERENCES bpmn2.process (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT bpmn_token_fkey2 FOREIGN KEY (parent)
      REFERENCES bpmn2.token (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.token
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.token TO fhir;
COMMENT ON COLUMN bpmn2.token.activity IS 'FK -> BPMN model of flowelement attr ID ';



CREATE TABLE bpmn2.waitingprocess
(
  id character varying(512) NOT NULL,
  activity character varying(512) NOT NULL,
  mesage character varying(512) NOT NULL,
  CONSTRAINT bpmn2_waitingprocess_fkey1 FOREIGN KEY (id)
      REFERENCES bpmn2.processdefinition (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.waitingprocess
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.waitingprocess TO fhir;


CREATE TABLE bpmn2.waitingtoken
(
  mesage character varying(512) NOT NULL,
  token character varying(512) NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT bpmn2_waitingtoken_pkey PRIMARY KEY (mesage, token),
  CONSTRAINT bpmn2_waitingtoken_fkey1 FOREIGN KEY (token)
      REFERENCES bpmn2.token (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.waitingtoken
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.waitingtoken TO fhir;



CREATE TABLE bpmn2.boundarytimerevents
(
  id character varying(512) NOT NULL DEFAULT uuid_generate_v4(),
  token character varying(512) NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  responsetime timestamp without time zone NOT NULL,
  targetactivity character varying(512),
  CONSTRAINT bpmn2_boundarytimerevents_pkey PRIMARY KEY (id),
  CONSTRAINT bpmn2_boundarytimerevents_fkey2 FOREIGN KEY (token)
      REFERENCES bpmn2.token (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.boundarytimerevents
  OWNER TO fhir;


CREATE TABLE bpmn2.cash
(
  id character varying(1024) NOT NULL,
  gid uuid NOT NULL DEFAULT uuid_generate_v4(),
  CONSTRAINT bpmn2_cash_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.cash
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.cash TO fhir;

CREATE TABLE bpmn2.datastore
(
  id character varying(1024) NOT NULL,
  sql character varying NOT NULL,
  CONSTRAINT bpmn2_datastore_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.datastore
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.datastore TO fhir;


CREATE TABLE bpmn2.process_context
(
  id character varying(512) NOT NULL,
  name character varying(1024) NOT NULL,
  value json,
  CONSTRAINT bpmn2_process_context_pkey PRIMARY KEY (id, name),
  CONSTRAINT bpmn2_process_context_fkey1 FOREIGN KEY (id)
      REFERENCES bpmn2.process (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.process_context
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.process_context TO fhir;


CREATE TABLE bpmn2.process_subscription
(
  message character varying(512) NOT NULL,
  processdefinition character varying(512) NOT NULL,
  value json,
  CONSTRAINT bpmn2_process_subscription_pkey PRIMARY KEY (message, processdefinition),
  CONSTRAINT bpmn2_process_subscription_fkey1 FOREIGN KEY (processdefinition)
      REFERENCES bpmn2.processdefinition (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.process_subscription
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.process_subscription TO fhir;


CREATE TABLE bpmn2.task
(
  id character varying(512) NOT NULL,
  process character varying(512) NOT NULL,
  token character varying(512),
  name character varying(4096),
  formkey character varying(4096),
  actualowner character varying(1024),
  created timestamp without time zone NOT NULL,
  finished timestamp without time zone,
  completiontime timestamp without time zone,
  activity character varying(512),
  CONSTRAINT bpmn2_task_pkey PRIMARY KEY (id),
  CONSTRAINT bpmn2_task_fkey1 FOREIGN KEY (process)
      REFERENCES bpmn2.process (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT bpmn2_task_fkey2 FOREIGN KEY (token)
      REFERENCES bpmn2.token (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT bpmn2_task_uq1 UNIQUE (token)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.task
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.task TO fhir;


CREATE TABLE bpmn2.task_context
(
  id character varying(512) NOT NULL,
  name character varying(1024) NOT NULL,
  value json,
  CONSTRAINT bpmn2_task_context_pkey PRIMARY KEY (id, name),
  CONSTRAINT bpmn2_task_context_fkey1 FOREIGN KEY (id)
      REFERENCES bpmn2.task (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.task_context
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.task_context TO fhir;


CREATE TABLE bpmn2.task_group
(
  id character varying(512) NOT NULL,
  name character varying(4096) NOT NULL,
  CONSTRAINT bpmn2_task_group_pkey PRIMARY KEY (id, name),
  CONSTRAINT bpmn2_task_group_fkey1 FOREIGN KEY (id)
      REFERENCES bpmn2.task (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.task_group
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.task_group TO fhir;


CREATE TABLE bpmn2.task_user
(
  id character varying(512) NOT NULL,
  name character varying(4096) NOT NULL,
  CONSTRAINT bpmn2_task_user_pkey PRIMARY KEY (id, name),
  CONSTRAINT bpmn2_task_user_fkey1 FOREIGN KEY (id)
      REFERENCES bpmn2.task (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bpmn2.task_user
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.task_user TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.fn_get_context_json(
    IN character varying,
    IN character varying[] DEFAULT NULL::character varying[])
  RETURNS TABLE(id character varying, json_value json) AS
$BODY$
-- SELECT id, parent_id, isactive, t.process_instance_id, def_activity_id, 
--        loopcounter, t2.json_value
--   FROM bpmn.token t, bpmn.fn_get_context_json(t.process_instance_id) t2
--   where t.process_instance_id = t2.process_instance_id
with t as (SELECT id id, name, value
		FROM bpmn2.process_context
		where id = $1)

select t1.id id, coalesce(t2.json_value, '{}'::json) json_value
  FROM (select distinct id from t) t1
  left join (select id, json_object_agg(name, value) json_value from t 
		where name = any ($2) or  array_length($2, 1) is null
		group by id) t2 on t1.id = t2.id 
  ;
$BODY$
  LANGUAGE sql VOLATILE SECURITY DEFINER
  COST 100
  ROWS 1000;
ALTER FUNCTION bpmn2.fn_get_context_json(character varying, character varying[])
  OWNER TO fhir;



CREATE OR REPLACE FUNCTION bpmn2.tr_boundarytimerevents_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin
        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.token = '' then
	      NEW.token = null;
	   end if;
           if NEW.targetactivity = '' then
	      NEW.targetactivity = null;
	   end if;

        end if; 
	       
	if TG_OP = 'DELETE' then
		delete from bpmn2.boundarytimerevents where id=OLD.id;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	    insert into  bpmn2.boundarytimerevents
	       (token,responsetime,targetactivity)
  	    values
	       (NEW.token,NEW.responsetime,NEW.targetactivity)
            returning id into _id;

            NEW.id:=_id;
            
	    RETURN NEW;
	elsif TG_OP = 'UPDATE' then
	    update bpmn2.boundarytimerevents set
                   targetactivity=NEW.targetactivity
            where id = OLD.id;   

            RETURN NEW;
	end if;     
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_boundarytimerevents_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_boundarytimerevents_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_boundarytimerevents_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_datastore_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin
   if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.id = '' then
	      NEW.id = null;
	   end if;
    end if;
    if TG_OP = 'DELETE' then
		delete from bpmn2.datastore where id=OLD.id;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	        insert into  bpmn2.datastore
               (id,sql)
            values
	       (NEW.id,NEW.sql);
  
	    RETURN NEW;
	elsif TG_OP = 'UPDATE' then
	    update bpmn2.datastore set
                   id=NEW.id,
                   sql=NEW.sql
            where id = OLD.id;   

            RETURN NEW;
	end if;
	       
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_datastore_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_datastore_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_datastore_iud() TO fhir;

CREATE OR REPLACE FUNCTION bpmn2.tr_definitions_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin
   if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.id = '' then
	      NEW.id = null;
	   end if;
	   if NEW.name = '' then
	      NEW.name = null;
	   end if;
	   if NEW.name is null then
	      NEW.name=NEW.id;
	   end if;
    end if;

    update bpmn2.cash
      set gid=uuid_generate_v4()
    where id='definitions';

    
    if TG_OP = 'DELETE' then
		delete from bpmn2.definitions where id=OLD.id;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	        insert into  bpmn2.definitions
               (id,name,content,created,updated)
            values
	       (NEW.id,NEW.name,NEW.content,now(),now());
  
	    RETURN NEW;
	elsif TG_OP = 'UPDATE' then
	    update bpmn2.definitions set
                   name=NEW.id,
                   updated=now(),
                   content=NEW.content
            where id = OLD.id;   

            RETURN NEW;
	end if;
	       
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_definitions_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_definitions_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_definitions_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_process_context_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin

        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.name = '' then
	      NEW.name = null;
	   end if;
        end if;       
	if TG_OP = 'DELETE' then
		delete from bpmn2.process_context where id=OLD.id and name=OLD.name;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	    insert into  bpmn2.process_context
	       (id,name,value)
	    values
	       (NEW.id,NEW.name,NEW.value);
          
	    RETURN NEW; 
        elsif TG_OP = 'UPDATE' then
               update bpmn2.process_context set
                 value=NEW.value
                where id=OLD.id and name=OLD.name; 
            
               RETURN NEW;
	end if;
        
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_process_context_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_process_context_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_process_context_iud() TO fhir;

CREATE OR REPLACE FUNCTION bpmn2.tr_process_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin
        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.id = '' then
	      NEW.id = null;
	   end if;
	   if NEW.processdefinition = '' then
	      NEW.processdefinition = null;
	   end if;
        end if; 
	       
	if TG_OP = 'DELETE' then
		delete from bpmn2.process where id=OLD.id;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	    insert into  bpmn2.process
	       (id,processdefinition,created)
	    values
	       (NEW.id,NEW.processdefinition,now())
            returning id into _id;

            NEW.id:=_id;
            
	    RETURN NEW; 
	end if;
        
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_process_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_process_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_process_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_process_subscription_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin

        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.message = '' then
	      NEW.message = null;
	   end if;
        end if;       
	if TG_OP = 'DELETE' then
		delete from bpmn2.process_subscription where message=OLD.message and processdefinition=OLD.processdefinition;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	    insert into  bpmn2.process_subscription
	       (message,processdefinition,value)
	    values
	       (NEW.message,NEW.processdefinition,NEW.value);
          
	    RETURN NEW; 
        elsif TG_OP = 'UPDATE' then
               update bpmn2.process_subscription set
                 value=NEW.value
                where message=OLD.message and processdefinition=OLD.processdefinition;
               RETURN NEW;
	end if;
        
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_process_subscription_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_process_subscription_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_process_subscription_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_processdefinition_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin
    if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.id = '' then
	      NEW.id = null;
	   end if;
	   if NEW.name = '' then
	      NEW.name = null;
	   end if;
	 
    end if; 
    if TG_OP = 'DELETE' then
		delete from bpmn2.processdefinition where id=OLD.id;
                RETURN OLD;
    elsif TG_OP = 'INSERT' then
	        insert into  bpmn2.processdefinition
               (id,definitionid,name)
            values
	       (NEW.id,NEW.definitionid,NEW.name);
       	    RETURN NEW;
    elsif TG_OP = 'UPDATE' then
            update bpmn2.processdefinition set
                 name=NEW.name
            where id=OLD.id;
            RETURN NEW;           	    
    end if;
	       
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_processdefinition_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_processdefinition_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_processdefinition_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_task_context_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin

        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.name = '' then
	      NEW.name = null;
	   end if;
        end if;       
	if TG_OP = 'DELETE' then
		delete from bpmn2.task_context where id=OLD.id and name=OLD.name;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	    insert into  bpmn2.task_context
	       (id,name,value)
	    values
	       (NEW.id,NEW.name,NEW.value);
          
	    RETURN NEW; 
        elsif TG_OP = 'UPDATE' then
               update bpmn2.task_context set
                 value=NEW.value
                where id=OLD.id and name=OLD.name; 
            
               RETURN NEW;
	end if;
        
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_task_context_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_task_context_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_task_context_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_task_group_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin

        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.id = '' then
	      NEW.id = null;
	   end if;
	   if NEW.name = '' then
	      NEW.name = null;
	   end if;
	   
        end if;       
	
	if TG_OP = 'INSERT' then
	    insert into  bpmn2.task_group
	       (id,name)
	    values
	       (NEW.id,NEW.name)
            returning id into _id;

            NEW.id:=_id;

	    RETURN NEW; 
        end if;
        
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_task_group_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_task_group_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_task_group_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_task_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
_activity varchar;
begin
        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.id = '' then
	      NEW.id = null;
	   end if;
	   if NEW.name = '' then
	      NEW.name = null;
	   end if;
	    
	   if NEW.formkey= '' then
	      NEW.formkey=null;
	   end if;
	   if NEW.actualowner= '' then
	      NEW.actualowner=null;
	   end if;
	   if NEW.name isnull then
	      NEW.name=NEW.id;
	   end if;
        end if;       
	if TG_OP = 'DELETE' then
		delete from bpmn2.task where id=OLD.id;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
             SELECT  activity into _activity 
             FROM bpmn2.v_token
             where id=  NEW.token;

	
	    insert into  bpmn2.task
	       (id,process,token,activity,name,formkey,actualowner,created) 
	    values
	       (NEW.id,NEW.process,NEW.token,_activity,NEW.name,NEW.formkey,NEW.actualowner,now())
            returning id into _id;

            NEW.id:=_id;

	    RETURN NEW; 
        elsif TG_OP = 'UPDATE' then
               update bpmn2.task set
                 token=NEW.token,
                 name=NEW.name,
                 formkey=NEW.formkey,
                 actualowner=NEW.actualowner
                where id = OLD.id;
               RETURN NEW;
	end if;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_task_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_task_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_task_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_task_user_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin

        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.id = '' then
	      NEW.id = null;
	   end if;
	   if NEW.name = '' then
	      NEW.name = null;
	   end if;
	   
        end if;       
	
	if TG_OP = 'INSERT' then
	    insert into  bpmn2.task_user
	       (id,name)
	    values
	       (NEW.id,NEW.name)
            returning id into _id;

            NEW.id:=_id;

	    RETURN NEW; 
        end if;
        
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_task_user_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_task_user_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_task_user_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_token_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin

        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.id = '' then
	      NEW.id = null;
	   end if;
	   if NEW.parent = '' then
	      NEW.parent = null;
	   end if;
	   if NEW.process= '' then
	      NEW.process=null;
	   end if; 
	   if NEW.activity= '' then
	      NEW.activity=null;
	   end if;    
	   
        end if;       
	if TG_OP = 'DELETE' then
		delete from bpmn2.token where id=OLD.id;

                if not exists(select 1 from bpmn2.token
                                   where process=OLD.process::character varying) then
                     update bpmn2.process set
                        finished=now()           
                     where id=OLD.process::character varying;              
		end if;

		update bpmn2.task set
		    finished=now()
		where token=OLD.id;        
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	    insert into  bpmn2.token
	       (id,parent,isactive,process,activity,loopcounter)
	    values
	       (NEW.id,NEW.parent,NEW.isactive,NEW.process,NEW.activity,NEW.loopcounter)
            returning id into _id;

            NEW.id:=_id;

	    RETURN NEW; 
        elsif TG_OP = 'UPDATE' then
               if NEW.activity !=OLD.activity then
                   delete from bpmn2.boundarytimerevents where token = OLD.id;
               end if;

        
               update bpmn2.token set
                 parent=NEW.parent,
                 isactive=NEW.isactive,
                 process=NEW.process,
                 activity=NEW.activity,
                 loopcounter=NEW.loopcounter
               where id = OLD.id;

               RETURN NEW;
	end if;
        
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_token_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_token_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_token_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_waitingprocess_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin
     
    if TG_OP = 'DELETE' then
		delete from bpmn2.waitingprocess where id=OLD.id and mesage=OLD.mesage and activity=OLD.activity;
                RETURN OLD;
    elsif TG_OP = 'INSERT' then
	        insert into  bpmn2.waitingprocess
               (id,mesage,activity)
            values
	       (NEW.id,NEW.mesage,NEW.activity);
       	    RETURN NEW;
               	    
    end if;
	       
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_waitingprocess_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_waitingprocess_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_waitingprocess_iud() TO fhir;


CREATE OR REPLACE FUNCTION bpmn2.tr_waitingtoken_iud()
  RETURNS trigger AS
$BODY$
declare
_id varchar;
begin

        if TG_OP = 'INSERT' or TG_OP = 'UPDATE' then
           if NEW.mesage = '' then
	      NEW.mesage = null;
	   end if;
        end if;       
	if TG_OP = 'DELETE' then

		delete from bpmn2.waitingtoken where mesage=OLD.mesage and token=OLD.token;
                RETURN OLD;
	elsif TG_OP = 'INSERT' then
	    insert into  bpmn2.waitingtoken
	       (mesage,token)
	    values
	       (NEW.mesage,NEW.token);
         RETURN NEW; 
     
	end if;
        
end;
$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
ALTER FUNCTION bpmn2.tr_waitingtoken_iud()
  OWNER TO fhir;
GRANT EXECUTE ON FUNCTION bpmn2.tr_waitingtoken_iud() TO public;
GRANT EXECUTE ON FUNCTION bpmn2.tr_waitingtoken_iud() TO fhir;



CREATE OR REPLACE VIEW bpmn2.v_boundarytimerevents AS 
 SELECT boundarytimerevents.id,
    boundarytimerevents.token,
    boundarytimerevents.created,
    boundarytimerevents.responsetime,
    boundarytimerevents.targetactivity
   FROM bpmn2.boundarytimerevents;

ALTER TABLE bpmn2.v_boundarytimerevents
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_boundarytimerevents TO fhir;


CREATE OR REPLACE VIEW bpmn2.v_cash AS 
 SELECT cash.id,
    cash.gid::text AS guid
   FROM bpmn2.cash;

ALTER TABLE bpmn2.v_cash
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_cash TO fhir;


CREATE OR REPLACE VIEW bpmn2.v_datastore AS 
 SELECT datastore.id,
    datastore.sql
   FROM bpmn2.datastore;

ALTER TABLE bpmn2.v_datastore
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_datastore TO fhir;

-- Trigger: v_tr_datastore_id on bpmn2.v_datastore

-- DROP TRIGGER v_tr_datastore_id ON bpmn2.v_datastore;

CREATE TRIGGER v_tr_datastore_id
  INSTEAD OF INSERT OR UPDATE OR DELETE
  ON bpmn2.v_datastore
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_datastore_iud();


 CREATE OR REPLACE VIEW bpmn2.v_definitions AS 
 SELECT definitions.id,
    definitions.name,
    definitions.content,
    definitions.created,
    definitions.updated
   FROM bpmn2.definitions;

ALTER TABLE bpmn2.v_definitions
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_definitions TO fhir;

-- Trigger: v_tr_definitions_iud on bpmn2.v_definitions

-- DROP TRIGGER v_tr_definitions_iud ON bpmn2.v_definitions;

CREATE TRIGGER v_tr_definitions_iud
  INSTEAD OF INSERT OR UPDATE OR DELETE
  ON bpmn2.v_definitions
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_definitions_iud();


CREATE OR REPLACE VIEW bpmn2.v_process AS 
 SELECT process.id,
    process.processdefinition,
    process.created,
    process.finished
   FROM bpmn2.process;

ALTER TABLE bpmn2.v_process
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_process TO fhir;

-- Trigger: v_tr_process_id on bpmn2.v_process

-- DROP TRIGGER v_tr_process_id ON bpmn2.v_process;

CREATE TRIGGER v_tr_process_id
  INSTEAD OF INSERT OR DELETE
  ON bpmn2.v_process
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_process_iud();


  
CREATE OR REPLACE VIEW bpmn2.v_process_context AS 
 SELECT process_context.id,
    process_context.name,
    process_context.value
   FROM bpmn2.process_context;

ALTER TABLE bpmn2.v_process_context
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_process_context TO fhir;

-- Trigger: tr_process_context_iud on bpmn2.v_process_context

-- DROP TRIGGER tr_process_context_iud ON bpmn2.v_process_context;

CREATE TRIGGER tr_process_context_iud
  INSTEAD OF INSERT OR UPDATE OR DELETE
  ON bpmn2.v_process_context
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_process_context_iud();


  CREATE OR REPLACE VIEW bpmn2.v_process_subscription AS 
 SELECT process_subscription.message,
    process_subscription.processdefinition,
    process_subscription.value
   FROM bpmn2.process_subscription;

ALTER TABLE bpmn2.v_process_subscription
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_process_subscription TO fhir;

-- Trigger: tr_process_subscription_iud on bpmn2.v_process_subscription

-- DROP TRIGGER tr_process_subscription_iud ON bpmn2.v_process_subscription;

CREATE TRIGGER tr_process_subscription_iud
  INSTEAD OF INSERT OR UPDATE OR DELETE
  ON bpmn2.v_process_subscription
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_process_subscription_iud();


  CREATE OR REPLACE VIEW bpmn2.v_processdefinition AS 
 SELECT processdefinition.id,
    processdefinition.name,
    processdefinition.definitionid
   FROM bpmn2.processdefinition;

ALTER TABLE bpmn2.v_processdefinition
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_processdefinition TO fhir;

-- Trigger: v_tr_processdefinition_iud on bpmn2.v_processdefinition

-- DROP TRIGGER v_tr_processdefinition_iud ON bpmn2.v_processdefinition;

CREATE TRIGGER v_tr_processdefinition_iud
  INSTEAD OF INSERT OR UPDATE OR DELETE
  ON bpmn2.v_processdefinition
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_processdefinition_iud();



CREATE OR REPLACE VIEW bpmn2.v_task AS 
 SELECT task.id,
    task.process,
    task.token,
    task.activity,
    task.name,
    task.formkey,
    task.actualowner,
    task.created,
    task.finished,
    task.completiontime
   FROM bpmn2.task;

ALTER TABLE bpmn2.v_task
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_task TO fhir;

-- Trigger: v_task_iud on bpmn2.v_task

-- DROP TRIGGER v_task_iud ON bpmn2.v_task;

CREATE TRIGGER v_task_iud
  INSTEAD OF INSERT OR UPDATE OR DELETE
  ON bpmn2.v_task
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_task_iud();



CREATE OR REPLACE VIEW bpmn2.v_task_context AS 
 SELECT task_context.id,
    task_context.name,
    task_context.value
   FROM bpmn2.task_context;

ALTER TABLE bpmn2.v_task_context
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_task_context TO fhir;

-- Trigger: tr_task_context_iud on bpmn2.v_task_context

-- DROP TRIGGER tr_task_context_iud ON bpmn2.v_task_context;

CREATE TRIGGER tr_task_context_iud
  INSTEAD OF INSERT OR UPDATE OR DELETE
  ON bpmn2.v_task_context
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_task_context_iud();



CREATE OR REPLACE VIEW bpmn2.v_task_group AS 
 SELECT task_group.id,
    task_group.name
   FROM bpmn2.task_group;

ALTER TABLE bpmn2.v_task_group
  OWNER TO fhir;

-- Trigger: v_tr_task_group_i on bpmn2.v_task_group

-- DROP TRIGGER v_tr_task_group_i ON bpmn2.v_task_group;

CREATE TRIGGER v_tr_task_group_i
  INSTEAD OF INSERT
  ON bpmn2.v_task_group
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_task_group_iud();



CREATE OR REPLACE VIEW bpmn2.v_task_user AS 
 SELECT task_user.id,
    task_user.name
   FROM bpmn2.task_user;

ALTER TABLE bpmn2.v_task_user
  OWNER TO fhir;

-- Trigger: v_tr_task_user_i on bpmn2.v_task_user

-- DROP TRIGGER v_tr_task_user_i ON bpmn2.v_task_user;

CREATE TRIGGER v_tr_task_user_i
  INSTEAD OF INSERT
  ON bpmn2.v_task_user
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_task_user_iud();


  CREATE OR REPLACE VIEW bpmn2.v_token AS 
 SELECT token.id,
    token.parent,
    token.isactive,
    token.process,
    token.activity,
    token.loopcounter
   FROM bpmn2.token;

ALTER TABLE bpmn2.v_token
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_token TO fhir;

-- Trigger: v_tr_token_iud on bpmn2.v_token

-- DROP TRIGGER v_tr_token_iud ON bpmn2.v_token;

CREATE TRIGGER v_tr_token_iud
  INSTEAD OF INSERT OR UPDATE OR DELETE
  ON bpmn2.v_token
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_token_iud();


  CREATE OR REPLACE VIEW bpmn2.v_waitingprocess AS 
 SELECT waitingprocess.id,
    waitingprocess.mesage,
    waitingprocess.activity
   FROM bpmn2.waitingprocess;

ALTER TABLE bpmn2.v_waitingprocess
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_waitingprocess TO fhir;

-- Trigger: v_tr_waitingprocess_id on bpmn2.v_waitingprocess

-- DROP TRIGGER v_tr_waitingprocess_id ON bpmn2.v_waitingprocess;

CREATE TRIGGER v_tr_waitingprocess_id
  INSTEAD OF INSERT OR DELETE
  ON bpmn2.v_waitingprocess
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_waitingprocess_iud();



  CREATE OR REPLACE VIEW bpmn2.v_waitingtoken AS 
 SELECT waitingtoken.mesage,
    waitingtoken.token,
    waitingtoken.created
   FROM bpmn2.waitingtoken;

ALTER TABLE bpmn2.v_waitingtoken
  OWNER TO fhir;
GRANT ALL ON TABLE bpmn2.v_waitingtoken TO fhir;

-- Trigger: tr_waitingtoken_id on bpmn2.v_waitingtoken

-- DROP TRIGGER tr_waitingtoken_id ON bpmn2.v_waitingtoken;

CREATE TRIGGER tr_waitingtoken_id
  INSTEAD OF INSERT OR DELETE
  ON bpmn2.v_waitingtoken
  FOR EACH ROW
  EXECUTE PROCEDURE bpmn2.tr_waitingtoken_iud();



insert into bpmn2.cash
(id,gid)
values
('definitions','f8799b27-f6b7-45a4-9869-d1cebee753db');
















