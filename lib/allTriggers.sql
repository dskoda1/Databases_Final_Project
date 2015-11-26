create or replace trigger insertPurchaseTrigger
after insert on purchases
FOR EACH ROW
begin
	insert into logs
	VALUES
	(log#_seq.nextval, user, sysdate, 'purchases', 'insert', :NEW.pur#);
end; 
/

create or replace trigger updateQohTrigger
after update of qoh on products
for each row
begin
	insert into logs
	values
	(log#_seq.nextval, user, sysdate, 'products', 'update', :NEW.pid);
end;
/

create or replace trigger updateVisitsMadeTrigger
after update of visits_made on customers
for each row
begin
	insert into logs
	values
	(log#_seq.nextval, user, sysdate, 'customers', 'update', :NEW.cid);
end;
/

create or replace trigger insertSupplyTrigger
after insert on supply
for each row
begin
	insert into logs
	values
	(log#_seq.nextval, user, sysdate, 'supply', 'insert', :NEW.sup#);
end;
/
