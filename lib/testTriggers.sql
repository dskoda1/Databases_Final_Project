insert into logs values
(log#_seq.nextval, user, sysdate, 'purchases', 'insert', pur#_seq.nextval);

insert into purchases values(pur#_seq.nextval, 'e01', 'p003', 'c002', 
150, to_date('13-AUG-2015 10:35:30','DD-MON-YYYY HH24:MI:SS'), 143);

insert into supply values(sup#_seq.nextval, 'p002', 's1', to_date('12-SEP-1991 
10:25:25','DD-MON-YYYY HH24:MI:SS'),15);

update products set qoh = qoh + 5 where pid = 'p001';

update customers set visits_made = visits_made + 2 where cid = 'c002';
