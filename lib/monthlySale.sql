--this package is used for question 3
--we want to pass in a product id and display 
--pname, each month thier were sales of this product
--each year for which the month is being checked
--the qty sold over that month
--the total money spent on tht product in tht month
--the average price spent for that product for tht month
--create the package
create or replace package monthlySale as
	--have a ref cursor to return multiple rows of data
	type ref_cursor is ref cursor;
	--this function will take in a product id and return a cursor
	function report_monthly_sale
	(pid_in in products.pid%type)
	return ref_cursor;
end monthlySale;
/
--package body
create or replace package body monthlySale as	
	function report_monthly_sale
	(pid_in in products.pid%type) 
	return ref_cursor as rc ref_cursor;
	--want to check the count to make sure we are finding the correct product
	countPid number(6);
	begin
		--def want an existing pid else raise exception
		if (pid_in is NULL) then
			raise_application_error(-20625, 'Product ID is null.');
		end if;
		--query to make sure we are dealing with an existing product
		select count(*) into countPid from products where pid = pid_in;
		if (countPid < 1) then 
			raise_application_error(-20123, 'There is no product with that pid.');
		end if;
		--open the ref cursor to be returned
		open rc for
		--query to get the products monthly sales
	 	select 	pname, 
			to_char(ptime, 'MON') as Month, 
			to_char(ptime, 'YYYY') as Year,
			sum(qty) as QTY_SOLD,
			sum(total_price) as TOTAL_SPENT,
			(sum(total_price) / sum(qty)) as AVERAGE_PRICE
		from products po, purchases pu
		where po.pid = pu.pid and po.pid = pid_in
		group by pname, to_char(ptime, 'MON'), to_char(ptime, 'YYYY');
		return rc;
	end;
end monthlySale;
/
show errors
	
