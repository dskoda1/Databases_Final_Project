create or replace package displayTable as
type ref_cursor is ref cursor;

function getEmployees
	return ref_cursor;
end displayTable; 
/
show errors

create or replace package body displayTable as

function getEmployees
	return ref_cursor as rc ref_cursor;
	begin
		open rc for
		select * from employees;
		return rc;
	end;
end;
/
show errors
