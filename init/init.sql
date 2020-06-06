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

CREATE TABLE public.groups (
    id SERIAL PRIMARY KEY,
    uri VARCHAR NOT NULL,
    label VARCHAR
);

CREATE TABLE public.configs (
    id SERIAL PRIMARY KEY,
    value text NOT NULL,
    is_admin BOOLEAN NOT NULL,
    uri VARCHAR NOT NULL,
    label VARCHAR,
    username VARCHAR,
    group_id INTEGER REFERENCES public.groups(id)
);



