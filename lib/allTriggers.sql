--this package is used for question 5
--whenever there is an insert into either purchases or supply
--we will also insert into the logs table
--whenever we update qoh or visits made
--we will also insert into the logs table

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
