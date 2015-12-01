--this package is used for question 5
--whenever there is an insert into either purchases or supply
--we will also insert into the logs table
--whenever we update qoh or visits made
--we will also insert into the logs table


--want to insert into logs table whenever after a new purchase is made
--also need ot check qoh, if needed order supply 
create or replace trigger insertPurchaseTrigger
after insert on purchases
FOR EACH ROW
declare
ppid products.pid%type;	
qohNeeded products.qoh%type;
prod products%rowtype;
supplierId supply.sid%type;
supplierCount number(6);
begin
	ppid := :NEW.pid;
	dbms_output.put_line('Product just purchased: ' || ppid);
	--Record the purchase as a log record
	insert into logs
	VALUES
	(log#_seq.nextval, user, sysdate, 'purchases', 'insert', :NEW.pur#);

	--Get the product that was just purchased	
	select * 
	into prod
	from products p
	where p.pid = ppid;	
	
	--update the product's qoh
	update products
	set qoh = (prod.qoh - :NEW.qty)
	where pid = ppid;

	--Get the new products values
	select * 
	into prod
	from products p
	where p.pid = ppid;	


	--Check for QOH
	if(prod.qoh < prod.qoh_threshold) then
		--We want the difference between the threshold and qoh + 1, which is m
		--Then the amount to order is m + 10, so this ends up being difference + 11.
		qohNeeded := prod.qoh_threshold - prod.qoh + 11;
		dbms_output.put_line('The current qoh for ' || prod.pname || ' is too low. A supply of at least ' || to_char(qohNeeded) || ' is needed to be above the threshold.');

		--Here we find the correct supplier to order from. We want the lowest supply number
		--if multiple, so rownum = 1. if no supplier exists, raise an application error
		--Stating no supplier for this product exists. Could only happen for new products
		
		--Check how many suppliers there are first
		select count(*)
		into supplierCount
		from supply
		where supply.pid = ppid;		

		--If no suppliers have supplied this product, raise an application error
		if(supplierCount < 1) then
			raise_application_error(-20123, 'No supplier has ever supplied ' || prod.pname || '. Unable to order supply.');
		end if;

		--Otherwise find the top most supplier
		select sid
		into supplierId
		from supply
		where pid = ppid
		and rownum = 1
		order by sup# asc;

		--Now order a supply with this suppliers id, the qohNeeded 
		insert into supply
		VALUES
		(sup#_seq.nextval, ppid, supplierId, sysdate, qohNeeded);
	end if;
	
	--Check if customers last visit date is same or not same as today
	--Update if necessary	
end; 
/
show err;

--here we want to add a new entry to the log table after qoh is updated
create or replace trigger updateQohTrigger
after update of qoh on products
for each row
begin
	insert into logs
	values
	(log#_seq.nextval, user, sysdate, 'products', 'update', :NEW.pid);
end;
/

--want to insert into logs table after visits_made is updated
create or replace trigger updateVisitsMadeTrigger
after update of visits_made on customers
for each row
begin
	insert into logs
	values
	(log#_seq.nextval, user, sysdate, 'customers', 'update', :NEW.cid);
end;
/
--want to insert into logs table after a new supply is inserted
create or replace trigger insertSupplyTrigger
after insert on supply
for each row
declare
ppid products.pid%type;	
begin
	--Record the supply into the logs
	insert into logs
	values
	(log#_seq.nextval, user, sysdate, 'supply', 'insert', :NEW.sup#);
	dbms_output.put_line('New supply for item ' || :NEW.pid || ' with sup# of ' || :NEW.sup# || ' and qty of ' || :NEW.quantity);

	--update the products qoh based on the quantity of supply ordered
	update products p
	set p.qoh = (p.qoh + :NEW.quantity)
	where p.pid = :NEW.pid;

end;
/
show err
