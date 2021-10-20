CREATE SEQUENCE IF NOT EXISTS calculationforrestart_id
    start 1
    increment 1
    NO MAXVALUE;

CREATE TABLE IF NOT EXISTS public.calculationforrestart
(
    id integer DEFAULT nextval('calculationforrestart_id'),
    number integer,
    uid character varying(100),
    treads integer
);