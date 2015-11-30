create or replace package monthlySale as
	procedure report_monthly_sale
	(pid_in in products.pid%type);
	

end monthlySale;
/

create or replace package body monthlySale as
	procedure report_monthly_sale
	(pid_in in products.pid%type) is
	begin
	 	select 	pname, 
			to_char(ptime, 'MON') as Month, 
			to_char(ptime, 'YYYY') as Year,
			sum(qty) as QTY_SOLD,
			sum(total_price) as TOTAL_SPENT,
			(sum(total_price) / sum(qty)) as AVERAGE_PRICE
		from products po, purchases pu
		where po.pid = pid_in, po.pid = pu.pid
		group by pname, to_char(ptime, 'MON'), to_char(ptime, 'YYYY');
	end;
end monthlySale;
/
show errors
	
