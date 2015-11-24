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
	
		itemPrice products.original_price%type;
		totalPrice purchases.total_price%type;

	begin
		select (original_price * (1 - discnt_rate))
		into itemPrice
		from products p
		where p.pid = pid
		and ROWNUM = 1;
		
		totalPrice := itemPrice * qty;
	
		insert into purchases
		VALUES
		(pur#_seq.NEXTVAL, eid, pid, cid, qty, SYSDATE, totalPrice);
			
	end;


end insertRecord;
/



