create or replace package insertRecord as
	procedure employee
	(eid in employees.eid%type,
	name in employees.ename%type,
	phone# in employees.telephone#%type);	

	procedure addProduct
	(pid in products.pid%type,
	pname in products.pname%type,
	qoh in products.qoh%type,
	qoh_threshold in products.qoh_threshold%type,
	original_price in products.original_price%type,
	discnt_rate in products.discnt_rate%type);
	
	procedure addPurchase
	(eid in purchases.eid%type,
	pid in purchases.pid%type,
	cid in purchases.cid%type,
	qty in purchases.qty%type);



end insertRecord;
/

create or replace package body insertRecord as

        procedure employee
        (eid in employees.eid%type,
        name in employees.ename%type,
        phone# in employees.telephone#%type) is
        begin
                insert into employees
                VALUES
                (eid, name, phone#);
        end;

	procedure addProduct
	(pid in products.pid%type,
	pname in products.pname%type,
	qoh in products.qoh%type,
	qoh_threshold in products.qoh_threshold%type,
	original_price in products.original_price%type,
	discnt_rate in products.discnt_rate%type) is
	begin
		insert into products
		VALUES	
		(pid, pname, qoh, qoh_threshold, original_price, discnt_rate);
	end;

	procedure addPurchase
	(eid in purchases.eid%type,
	pid in purchases.pid%type,
	cid in purchases.cid%type,
	qty in purchases.qty%type) is
	--Declare variable and exceptions needed
		itemPrice products.original_price%type;
		totalPrice purchases.total_price%type;
		qoh_left products.qoh%type;
		count number(3);
		val_too_large exception;
		pragma exception_init(val_too_large, -12899);
		no_parent_key exception;
		pragma exception_init(no_parent_key, -2291);
		too_many_rows exception;
		pragma exception_init(too_many_rows, -1422);
		--Need no data found exception

	begin
		--Check for null values first
		if eid is NULL then
			raise_application_error(-20625, 'Employee ID for purchase is null.');
		elsif pid is NULL then
			raise_application_error(-20625, 'Product ID for purchase is null.');
		elsif cid is NULL then
			raise_application_error(-20625, 'Customer ID for purchase is null.');
		elsif qty is NULL then
			raise_application_error(-20625, 'Quantity for purchase is null.');
		end if;

		--Check if qoh of product is greater than or equal to qty
		--This query doesnt work even if i dont use aggregate function. It's a simple select where pid = pid and yet somehow returns 'too many rows' exception.
		select count(*), qoh
		into count, qoh_left
		from products
		where products.pid like pid
		group by qoh;
	
		
		--Raise app error otherwise
		--if qoh_left < qty then
			raise_application_error(-20434, 'Insufficient quantity in stock ' || to_char(qoh_left) || ' for the requested item ' || to_char(pid) || '. Qty requested was ' || to_char(qty));
		--end if;	

		--Execute actual query, save into itemPrice
		select (original_price * (1 - discnt_rate))
		into itemPrice
		from products p
		where p.pid = pid
		--This line should be removed.. should be another check for exception
		--either ora-01403 or something about no results
		and ROWNUM = 1;
		
		--Calculate total price
		totalPrice := itemPrice * qty;
	
		--Insert into table now
		insert into purchases
		VALUES
		(pur#_seq.NEXTVAL, eid, pid, cid, qty, SYSDATE, totalPrice);
	exception
		--One of the parameters is larger than its type
		when val_too_large then
		raise_application_error(-20899, 'Parameter passed into procedure -Add Purchase- is too large. See below for which specific value was too large.', true);
		--One of the foreign keys is not valid, has no parent key in the matching table
		when no_parent_key then
		raise_application_error(-20291, 'No parent key found on a parameter passed into procedure -Add Purchase. See below for which specific value did not have a corresponding parent key.', true);
		--Too many rows found
		when too_many_rows then
		raise_application_error(-20422, 'Too many rows found by query below.', true);
	end;

end insertRecord;
/



