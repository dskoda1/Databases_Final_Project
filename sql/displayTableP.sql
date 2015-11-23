create or replace package displayTable as
type ref_cursor is ref cursor;

function getEmployees
	return ref_cursor;
function getCustomers
	return ref_cursor;
function getProducts
	return ref_cursor;
function getSuppliers
	return ref_cursor;
function getSupply
	return ref_cursor;
function getPurchases
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

function getCustomers
	return ref_cursor as rc ref_cursor;
	begin 
		open rc for
		select * from customers;
		return rc;
	end;

function getProducts
	return ref_cursor as rc ref_cursor;
	begin
		open rc for
		select * from products;
		return rc;
	end;

function getSuppliers
	return ref_cursor as rc ref_cursor;
	begin 
		open rc for
		select * from suppliers;
		return rc;
	end;

function getSupply
	return ref_cursor as rc ref_cursor;
	begin
		open rc for
		select * from supply;
		return rc;
	end;

function getPurchases
	return ref_cursor as rc ref_cursor;
	begin
		open rc for
		select * from purchases;
		return rc;
	end;

end;
/
show errors
