--this file is used to determine if the query in monthlySale is
--working properly
--not working properly expecting into clause
declare
	pid_in products.pid%type;
begin
	pid_in := 'p002';
	select 	pname, 
		to_char(ptime, 'MON') as Month,
		to_char(ptime, 'YYYY') as Year,
		sum(qty) as QTY_SOLD, 
		sum(total_price) as TOTAL_SPENT, 
		(sum(total_price) / sum(qty)) as AVERAGE_PRICE
	from products po, purchases pu
	where po.pid = pu.pid and po.pid = pid_in
	group by pname, to_char(ptime, 'MON'), to_char(ptime, 'YYYY');
end;
/
