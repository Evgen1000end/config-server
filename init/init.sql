SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE DATABASE config;

CREATE SCHEMA config;

ALTER SCHEMA config OWNER TO postgres;

SET default_tablespace = '';
SET default_table_access_method = heap;

CREATE TABLE config.configs (
    id bigint NOT NULL,
    raw text NOT NULL
);


ALTER TABLE config.configs OWNER TO postgres;

CREATE SEQUENCE config.configs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE config.configs_id_seq OWNER TO postgres;

ALTER SEQUENCE config.configs_id_seq OWNED BY config.configs.id;

ALTER TABLE ONLY config.configs ALTER COLUMN id SET DEFAULT nextval('config.configs_id_seq'::regclass);

SELECT pg_catalog.setval('config.configs_id_seq', 18, true);

ALTER TABLE ONLY config.configs
    ADD CONSTRAINT idx_16391_primary PRIMARY KEY (id);


GRANT ALL ON SCHEMA config TO PUBLIC;


