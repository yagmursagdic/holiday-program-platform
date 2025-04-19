-- init.sql
-- TERM SERVICE
CREATE DATABASE term_db;
CREATE USER termuser WITH PASSWORD 'termUser_pw_geheim';
GRANT ALL PRIVILEGES ON DATABASE term_db TO termuser;

-- ACCOUNTING SERVICE
CREATE DATABASE accounting_db;
CREATE USER accountinguser WITH PASSWORD 'accountingUser_pw_geheim';
GRANT ALL PRIVILEGES ON DATABASE accounting_db TO accountinguser;
