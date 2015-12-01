/*
	Initialization script for this project.
	Actions:
	1. Drop sequences
	2. Drop tables
	3. Create Sequences
*/

drop sequence log#_seq;
drop sequence pur#_seq;
drop sequence sup#_seq;


drop table logs;
drop table supply;
drop table suppliers;
drop table purchases;
drop table products;
drop table customers;
drop table employees;

--now that everything is dropped we will create

create sequence pur#_seq
start with 100000
increment by 1;

create sequence sup#_seq
start with 1000
increment by 1;

create sequence log#_seq
start with 10000
increment by 1;

