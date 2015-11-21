create or replace package body insert as
	procedure employee
	(eid in employees.eid%type,
        name in employees.ename%type,
        phone# in employees.telephone#%type) is
	begin
		insert into employees
		VALUES
		(eid, name, phone#);
	end;
end;
/
