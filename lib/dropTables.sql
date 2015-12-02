/*
	Initialization script for this project.
	Actions:
	1. Drop sequences
	2. Drop triggers
	3. Drop tables
*/

drop sequence log#_seq;
drop sequence pur#_seq;
drop sequence sup#_seq;

drop trigger insertPurchaseTrigger;
drop trigger updateQohTrigger;
drop trigger updateVisitsMadeTrigger;
drop trigger insertSupplyTrigger;

drop table logs;
drop table supply;
drop table suppliers;
drop table purchases;
drop table products;
drop table customers;
drop table employees;

