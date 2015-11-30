--this package is used for question 3
--we want to pass in a product id and display 
--pname, each month thier were sales of this product
--each year for which the month is being checked
--the qty sold over that month
--the total money spent on tht product in tht month
--the average price spent for that product for tht month

create or replace package monthlySale as
	type ref_cursor is ref cursor;
	function report_monthly_sale
	(pid_in in products.pid%type)
	return ref_cursor;
	

end monthlySale;
/

create or replace package body monthlySale as
	function report_monthly_sale
	(pid_in in products.pid%type) 
	return ref_cursor as rc ref_cursor;
	
	begin
		open rc for
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
	
