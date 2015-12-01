/*
	Initialization script for this project.
	Actions:
	1. Drop sequences
	2. Drop tables
	3. Create tables
	4. Create sequences
	5. Insert tuples
	6. Create triggers- triggers not created in this file
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


create table employees
(eid char(3) primary key,
ename varchar2(15),
telephone# char(12));


create table customers
(cid char(4) primary key,
cname varchar2(15),
telephone# char(12),
visits_made number(4),
last_visit_date date);


create table products
(pid char(4) primary key,
pname varchar2(15),
qoh number(5),
qoh_threshold number(4),
original_price number(6,2),
discnt_rate number(3,2) check(discnt_rate between 0 and 0.8));


create table purchases
(pur# number(6) primary key,
eid char(3) references employees(eid),
pid char(4) references products(pid),
cid char(4) references customers(cid),
qty number(5),
ptime date,
total_price number(7,2));


create table suppliers
(sid char(2) primary key,
sname varchar2(15) not null unique,
city varchar2(15),
telephone# char(12));


create table supply
(sup# number(4) primary key,
pid char(4) references products(pid),
sid char(2) references suppliers(sid),
sdate date,
quantity number(5));


create table logs
(log# number(5) primary key,
who varchar2(12) not null,
otime date not null,
table_name varchar2(20) not null,
operation varchar2(6) not null,
key_value varchar2(6));


create sequence pur#_seq
start with 100000
increment by 1;

create sequence sup#_seq
start with 1000
increment by 1;

create sequence log#_seq
start with 10000
increment by 1;

--begin inserting into all of the tables
insert into employees values ('e01', 'Peter', '666-555-1234');

insert into employees values ('e02', 'David', '777-555-2341');

insert into employees values ('e03', 'Susan', '888-555-3412');

insert into employees values ('e04', 'Anne', '666-555-4123');

insert into employees values ('e05', 'Mike', '444-555-4231');

insert into customers values ('c001', 'Kathy', '666-555-4567', 3, '12-OCT-15');

insert into customers values ('c002', 'John', '888-555-7456', 1, '08-OCT-15');

insert into customers values ('c003', 'Chris', '666-555-6745', 3, '18-SEP-15');

insert into customers values ('c004', 'Mike', '999-555-5674', 1, '20-OCT-15');

insert into customers values ('c005', 'Mike', '777-555-4657', 2, '30-AUG-15');

insert into customers values ('c006', 'Connie', '777-555-7654', 2, '16-OCT-15');

insert into customers values ('c007', 'Katie', '888-555-6574', 1, '12-OCT-15');

insert into customers values ('c008', 'Joe', '666-555-5746', 1, '18-OCT-15');

insert into products values ('p001', 'stapler', 40, 20, 9.99, 0.1);

insert into products values ('p002', 'TV', 7, 5, 249, 0.15);

insert into products values ('p003', 'camera', 10, 5, 148, 0.2);

insert into products values ('p004', 'pencil', 26, 10, 0.99, 0.0);

insert into products values ('p005', 'chair', 10, 8, 12.98, 0.3);

insert into products values ('p006', 'lamp', 11, 6, 19.95, 0.1);

insert into products values ('p007', 'tablet', 13, 10, 149, 0.2);

insert into products values ('p008', 'computer', 6, 3, 499, 0.3);

insert into products values ('p009', 'powerbank', 8, 5, 49.95, 0.1);

insert into purchases values (pur#_seq.nextval, 'e01', 'p002', 'c001', 1, to_date('12-AUG-2015 10:34:30', 'DD-MON-YYYY HH24:MI:SS'), 211.65);

insert into purchases values (pur#_seq.nextval, 'e01', 'p003', 'c001', 1, to_date('20-SEP-2015 11:23:36', 'DD-MON-YYYY HH24:MI:SS'), 118.40);

insert into purchases values (pur#_seq.nextval, 'e02', 'p004', 'c002', 5, to_date('08-OCT-2015 09:30:50', 'DD-MON-YYYY HH24:MI:SS'), 4.95);

insert into purchases values (pur#_seq.nextval, 'e01', 'p005', 'c003', 2, to_date('23-AUG-2015 16:23:35', 'DD-MON-YYYY HH24:MI:SS'), 18.17);

insert into purchases values (pur#_seq.nextval, 'e04', 'p007', 'c004', 1, to_date('20-OCT-2015 13:38:55', 'DD-MON-YYYY HH24:MI:SS'), 119.20);

insert into purchases values (pur#_seq.nextval, 'e03', 'p008', 'c001', 1, to_date('12-OCT-2015 15:22:10', 'DD-MON-YYYY HH24:MI:SS'), 349.30);

insert into purchases values (pur#_seq.nextval, 'e03', 'p006', 'c003', 2, to_date('10-SEP-2015 17:12:20', 'DD-MON-YYYY HH24:MI:SS'), 35.91);

insert into purchases values (pur#_seq.nextval, 'e03', 'p006', 'c005', 1, to_date('16-AUG-2015 12:22:15', 'DD-MON-YYYY HH24:MI:SS'), 17.96);

insert into purchases values (pur#_seq.nextval, 'e03', 'p001', 'c007', 1, to_date('12-OCT-2015 14:44:23', 'DD-MON-YYYY HH24:MI:SS'), 8.99);

insert into purchases values (pur#_seq.nextval, 'e04', 'p002', 'c006', 1, to_date('19-SEP-2015 17:32:37', 'DD-MON-YYYY HH24:MI:SS'), 211.65);

insert into purchases values (pur#_seq.nextval, 'e02', 'p004', 'c006', 10, to_date('16-OCT-2015 16:54:40', 'DD-MON-YYYY HH24:MI:SS'), 9.90);

insert into purchases values (pur#_seq.nextval, 'e02', 'p008', 'c003', 2, to_date('18-SEP-2015 15:56:38', 'DD-MON-YYYY HH24:MI:SS'), 698.60);

insert into purchases values (pur#_seq.nextval, 'e04', 'p006', 'c005', 2, to_date('30-AUG-2015 10:38:25', 'DD-MON-YYYY HH24:MI:SS'), 35.91);

insert into purchases values (pur#_seq.nextval, 'e03', 'p009', 'c008', 3, to_date('18-OCT-2015 10:54:06', 'DD-MON-YYYY HH24:MI:SS'), 134.84);

insert into suppliers values ('s0', 'Dell', 'Chicago', '327-848-7633');

insert into suppliers values ('s1', 'Apple', 'Washington', '424-563-2198');

insert into suppliers values ('s2', 'Bic', 'Los Angeles', '543-888-9259');

insert into suppliers values ('s3', 'Panasonic', 'New York', '607-456-7890');

insert into suppliers values ('s4', 'LampsUSA', 'Jackson', '878-231-5961');

insert into supply values (sup#_seq.nextval, 'p001', 's3', to_date('02-OCT-2015 10:56:23', 'DD-MON-YYYY HH24:MI:SS'), 123);

insert into supply values (sup#_seq.nextval, 'p002', 's4', to_date('23-SEP-2015 13:15:17', 'DD-MON-YYYY HH24:MI:SS'), 10);

insert into supply values (sup#_seq.nextval, 'p003', 's3', to_date('14-MAY-2015 15:17:19', 'DD-MON-YYYY HH24:MI:SS'), 12);

insert into supply values (sup#_seq.nextval, 'p004', 's2', to_date('07-AUG-2015 17:19:21', 'DD-MON-YYYY HH24:MI:SS'), 50);

insert into supply values (sup#_seq.nextval, 'p005', 's2', to_date('11-OCT-2015 19:21:23', 'DD-MON-YYYY HH24:MI:SS'), 12);

insert into supply values (sup#_seq.nextval, 'p006', 's1', to_date('24-JUN-2015 21:23:25', 'DD-MON-YYYY HH24:MI:SS'), 15);

insert into supply values (sup#_seq.nextval, 'p007', 's1', to_date('21-SEP-2015 23:24:25', 'DD-MON-YYYY HH24:MI:SS'), 8);

insert into supply values (sup#_seq.nextval, 'p008', 's0', to_date('15-NOV-2015 11:12:13', 'DD-MON-YYYY HH24:MI:SS'), 5);

insert into supply values (sup#_seq.nextval, 'p009', 's0', to_date('12-NOV-2015 08:12:13', 'DD-MON-YYYY HH24:MI:SS'), 15);

