--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.14
-- Dumped by pg_dump version 9.6.13

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

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: product; Type: TABLE; Schema: public; Owner: hashlab
--

CREATE TABLE public.product (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    title character varying(255) NOT NULL,
    description text NOT NULL,
    price_in_cents integer NOT NULL,
    create_date timestamp without time zone NOT NULL,
    update_date timestamp without time zone,
    status boolean DEFAULT true NOT NULL
);


ALTER TABLE public.product OWNER TO hashlab;

--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: hashlab
--

COPY public.product (id, price_in_cents, title, description, create_date, update_date, status) FROM stdin;
19a06d17-b9a4-4e48-9e31-50b07c05f1d1	10000	Product 1	Description 1	2019-08-03 10:23:54	\N	t
2aa90a5e-8c31-4121-8529-751719c988fc	18084	Product 2	Description 2	2019-08-03 10:23:54	\N	t
0f642355-3841-4bc7-8a3c-1985383578e3	22202	Product 3	Description 3	2019-08-03 10:23:54	\N	t
c7993cb6-b182-4a3d-8186-9f8581c7c790	29788	Product 4	Description 4	2019-08-03 10:23:54	\N	t
83c2571b-d174-4e84-9b7e-d8c7806c33b9	39719	Product 5	Description 5	2019-08-03 10:23:54	\N	t
affcf8e9-e622-47e4-bc84-4b35808651dc	43423	Product 6	Description 6	2019-08-03 10:23:54	\N	t
3bce53f0-3bac-4915-ad53-411b1cd68b2a	52797	Product 7	Description 7	2019-08-03 10:23:54	\N	t
30ab73b3-541e-4381-bdf0-15575e9999cc	52984	Product 8	Description 8	2019-08-03 10:23:54	\N	t
f14a5e14-0e79-4163-b5a1-e41fe149f6d9	55879	Product 9	Description 9	2019-08-03 10:23:54	\N	t
7e3d3de6-293f-4beb-a211-b9bc3633176c	61268	Product 10	Description 10	2019-08-03 10:23:54	\N	t
56cbd64d-1508-4510-ba4f-0b4c1f8bccb9	65089	Product 11	Description 11	2019-08-03 10:23:54	\N	t
906d20ff-5fbf-4beb-accd-5bec45eba43d	72041	Product 12	Description 12	2019-08-03 10:23:54	\N	t
e51ebc62-a9ca-4ba8-bbaa-26c8a3b23bae	78492	Product 13	Description 13	2019-08-03 10:23:54	\N	t
0ef7faa0-1bac-49df-9f63-ffbda0803cbb	85560	Product 14	Description 14	2019-08-03 10:23:54	\N	t
bfbafea9-3bea-4295-ae23-a545ac055a1a	87092	Product 15	Description 15	2019-08-03 10:23:54	\N	t
3c74718a-5a1b-4135-8d3e-0eaef34203cf	95212	Product 16	Description 16	2019-08-03 10:23:54	\N	t
374fb482-d357-4b2f-b3b1-d91e0b66d0b8	101774	Product 17	Description 17	2019-08-03 10:23:54	\N	t
1e862da6-6ea4-41a9-92b5-115e963be155	109420	Product 18	Description 18	2019-08-03 10:23:54	\N	t
ed5a07af-6c71-46ee-b7ca-6a2fae26d6b1	115834	Product 19	Description 19	2019-08-03 10:23:54	\N	t
b887fd42-ff85-4c7f-9b46-5a04b1e495b6	116002	Product 20	Description 20	2019-08-03 10:23:54	\N	t
3d52a6a7-7877-4764-a2b9-b667dff43338	125669	Product 21	Description 21	2019-08-03 10:23:54	\N	t
1426c6dc-688e-4797-9f00-62118b5389e2	134539	Product 22	Description 22	2019-08-03 10:23:54	\N	t
6392c269-4a3b-421b-8286-88e71c73c5aa	135249	Product 23	Description 23	2019-08-03 10:23:54	\N	t
7ca59155-e059-4823-9084-367745c1eae3	139476	Product 24	Description 24	2019-08-03 10:23:54	\N	t
1c9e7c3a-15b7-4c91-9d4f-a5a9a09f7a44	145492	Product 25	Description 25	2019-08-03 10:23:54	\N	t
92acc5c2-daaa-4b36-bb87-487c0d11c369	153711	Product 26	Description 26	2019-08-03 10:23:54	\N	t
4c082e9b-4fef-4135-9283-6b029bf6e499	155654	Product 27	Description 27	2019-08-03 10:23:54	\N	t
c7332bc6-d9d3-4ab2-b037-c53b89f7f12d	156053	Product 28	Description 28	2019-08-03 10:23:54	\N	t
adb15ad8-952e-41fc-99c0-b973e473169d	165090	Product 29	Description 29	2019-08-03 10:23:54	\N	t
a330e2cc-d1f2-4cb9-9c33-4e0170b7c995	171024	Product 30	Description 30	2019-08-03 10:23:54	\N	t
b7938752-c01f-40a8-a471-46a29a88acd9	176060	Product 31	Description 31	2019-08-03 10:23:54	\N	t
60a86687-5cd6-4c3f-8a89-279d527b039e	177089	Product 32	Description 32	2019-08-03 10:23:54	\N	t
a00e10b4-fa49-4be3-bacc-a5ebcc0c442a	179750	Product 33	Description 33	2019-08-03 10:23:54	\N	t
aff40568-6fdc-41bb-a5f0-3163e2914811	189534	Product 34	Description 34	2019-08-03 10:23:54	\N	t
936afd02-e4e7-4ca7-add8-5e69e921dcac	193564	Product 35	Description 35	2019-08-03 10:23:54	\N	t
b68900cf-5428-44fd-a929-0b8cabc06647	203538	Product 36	Description 36	2019-08-03 10:23:54	\N	t
fa98b4d4-bcc6-410c-9c8d-3ec95de31efb	210564	Product 37	Description 37	2019-08-03 10:23:54	\N	t
895f5626-a559-4f52-ac05-9d09a35b73ea	217790	Product 38	Description 38	2019-08-03 10:23:54	\N	t
97d96bb6-9e56-4af0-b7ba-19d872e5af08	227366	Product 39	Description 39	2019-08-03 10:23:54	\N	t
91ad6b41-6da2-44cb-9754-685966b574f9	232911	Product 40	Description 40	2019-08-03 10:23:54	\N	t
929a885e-1f0c-4bb7-b84a-70db87a99100	233466	Product 41	Description 41	2019-08-03 10:23:54	\N	t
eda0a088-dd56-4a89-bcae-a9515c865810	235718	Product 42	Description 42	2019-08-03 10:23:54	\N	t
05096caa-b239-4a7a-98e3-00aad240581b	239953	Product 43	Description 43	2019-08-03 10:23:54	\N	t
8753abb0-500e-4a54-812d-ab2e5dfdbe2a	241882	Product 44	Description 44	2019-08-03 10:23:54	\N	t
1d69acdc-7e82-42a6-b82a-61952edbebb1	244841	Product 45	Description 45	2019-08-03 10:23:54	\N	t
3b5c7a42-b336-425e-8865-28375c21cfce	248253	Product 46	Description 46	2019-08-03 10:23:54	\N	t
5ed03dc3-a72a-4212-a62a-780fd703250d	255755	Product 47	Description 47	2019-08-03 10:23:54	\N	t
9c213a94-6648-4ce9-b41f-c89a84ab32df	263377	Product 48	Description 48	2019-08-03 10:23:54	\N	t
0da0567e-bee1-4bd6-abe2-e67b556c991b	272827	Product 49	Description 49	2019-08-03 10:23:54	\N	t
991a88de-b0e9-4110-a6b6-1e6815089268	273067	Product 50	Description 50	2019-08-03 10:23:54	\N	t
\.


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: hashlab
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

