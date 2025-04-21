-- init.sql
-- TERM SERVICE
CREATE DATABASE term_db;
CREATE USER termuser WITH PASSWORD 'termUser_pw_geheim';
GRANT ALL PRIVILEGES ON DATABASE term_db TO termuser;
\c term_db
CREATE SCHEMA IF NOT EXISTS public;
GRANT ALL PRIVILEGES ON SCHEMA public to termuser;

-- ACCOUNTING SERVICE
CREATE DATABASE accounting_db;
CREATE USER accountinguser WITH PASSWORD 'accountingUser_pw_geheim';
GRANT ALL PRIVILEGES ON DATABASE accounting_db TO accountinguser;
\c accounting_db
CREATE SCHEMA IF NOT EXISTS public;
GRANT ALL PRIVILEGES ON SCHEMA public TO accountinguser;
