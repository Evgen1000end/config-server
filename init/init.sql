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
    value text,
    is_admin BOOLEAN NOT NULL,
    uri VARCHAR NOT NULL,
    label VARCHAR,
    username VARCHAR,
    groupUri VARCHAR,
    group_id INTEGER REFERENCES public.groups(id)
);

INSERT INTO public.groups (uri, label)  VALUES ('rm', 'Расчетные модули - финансовая математика');
INSERT INTO public.groups (uri, label)  VALUES ('integration', 'Интеграционная платформа');
INSERT INTO public.groups (uri, label)  VALUES ('ui', 'Графический интерфейс пользователя');
INSERT INTO public.groups (uri, label)  VALUES ('trash', 'Выжатый сахарный тростник');

INSERT INTO public.configs (is_admin, uri, label, username, groupUri, group_id, value)
VALUES (true, 'argus', 'Мониторинг УФЭС', null , 'integration', 2, '{}');

INSERT INTO public.configs (value, is_admin, uri, label, username, groupUri, group_id)
VALUES ('{"key": "overrided"}', false , 'argus', 'Мониторинг УФЭС', 'Eugene' , 'integration', 2);

INSERT INTO public.configs (is_admin, uri, label, username, groupUri, group_id, value)
VALUES (true, 'blackhole', 'ICT 2.0', null , 'integration', 2, '{}');
