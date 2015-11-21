create or replace package insertTuples as
	procedure employee
	(eid in employees.eid%type,
	name in employees.ename%type,
	phone# in employees.telephone#%type);	
end insertTuples;
/

create or replace package body insertTuples as
        procedure employee
        (eid in employees.eid%type,
        name in employees.ename%type,
        phone# in employees.telephone#%type) is
        begin
                insert into employees
                VALUES
                (eid, name, phone#);
        end;
end insertTuples;
/


