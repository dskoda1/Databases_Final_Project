--this package is used to insert into the employee, product, and purchases tables
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
	--Declare exceptions 
		dup_index_val exception;
		pragma exception_init(dup_index_val, -1);
		string_val_too_large exception;
		pragma exception_init(string_val_too_large, -12899);
		num_val_too_large exception;
		pragma exception_init(num_val_too_large, -1438);	

	begin
		--Check for null values first
		if pid is NULL then
			raise_application_error(-20625, 'Product ID is null.');
		elsif pname is NULL then
			raise_application_error(-20625, 'Product name is null.');
		elsif qoh is NULL then
			raise_application_error(-20625, 'QOH is null.');
		elsif qoh_threshold is NULL then
			raise_application_error(-20625, 'QOH threshold is null.');
		elsif original_price is NULL then
			raise_application_error(-20625, 'Customer ID for purchase is null.');
		elsif discnt_rate is NULL then
			raise_application_error(-20625, 'Quantity for purchase is null.');
		end if;
		

		insert into products
		VALUES	
		(pid, pname, qoh, qoh_threshold, original_price, discnt_rate);
	exception
		--Primary key already exists
		when dup_index_val then
			raise_application_error(-20001, 'New PID of ' || pid || ' already exists. -Add Product- failed.', true);
		--One of the parameters is larger than its type
		when string_val_too_large then
			raise_application_error(-20899, 'String parameter passed into procedure -Add Product- is too large. See below for which specific value was too large.', true);
		when num_val_too_large then
			raise_application_error(-20438, 'Number parameter passed into procedure -Add Product- is too large. See below for which specific value was too large.', true);
		--Other errors
		when others then
			raise_application_error(-20999, 'SQL Exception caught. See below for details.', true);

	end;

	procedure addPurchase
	(eid in purchases.eid%type,
	pid in purchases.pid%type,
	cid in purchases.cid%type,
	qty in purchases.qty%type) is
	--Declare variable and exceptions needed
		
		--product cursor 
		cursor prodCurs is 
			select *
			from products p;
		--declare variables
		prodRec prodCurs%rowtype;		
		itemPrice products.original_price%type;
		totalPrice purchases.total_price%type;
		
		--declare exceptions
		--Only is for string values
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

		--Find the row in products requested for purchase
		--This is the workaround to the too_many_rows error 
		open prodCurs;
		dbms_output.put_line(prodCurs%rowcount);
		loop
			fetch prodCurs into prodRec;
			exit when prodCurs%notfound;
			if(prodRec.pid = pid) then
				--Exit the loop, have the correct row
				exit;
			end if;
		end loop;
		close prodCurs;	
		--Check to make sure we have correct row
		--This might be unecessary, but not sure
		if(prodRec.pid != pid) then
			raise_application_error(-20291, 'Product with pid ' || to_char(pid) || ' not found.');
		end if;
	
		--Check if a legal purchase
		if(prodRec.qoh < qty) then
			raise_application_error(-20434, 'Insufficient quantity in stock ' || to_char(prodRec.qoh) || ' for the requested item ' || to_char(pid) || '. Qty requested was ' || to_char(qty));
		end if;

		--Calculate the price after discount
		itemPrice := prodRec.original_price * (1 - prodRec.discnt_rate);
		--Calculate total price
		totalPrice :=  itemPrice * qty;
	
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
		--Too many rows found -- think this is obsolete now that I changed it to cursor
		when too_many_rows then
			raise_application_error(-20422, 'Too many rows found by query below.', true);
		--Other errors
		when others then
			raise_application_error(-20999, 'SQL Exception caught. See below for details.', true);
	end;

end insertRecord;
/



