package classes;
import java.lang.*;
import java.io.*;
import java.sql.*;
import oracle.jdbc.*;
import java.util.ArrayList;
/**
 * Supplier(ResultSet rs) throws SQLException
 * ResultSet selectAll(Connection conn) throws SQL exception
 * ArrayList<Supplier> parseResultSet(ResultSet rs) throws SQLException
 * void outputList(ArrayList<Supplier>)
 */
public class Supplier {
	private String sid, sname, city, telephoneNum;
	
	public Supplier(){
		super();
	}
	/*
	*	Take in a result set and initialize.
	*/
	public Supplier(ResultSet rs) throws SQLException{
		this.sid = rs.getString(1);
		this.sname = rs.getString(2);
		this.city = rs.getString(3);
		this.telephoneNum= rs.getString(4);
	}
	
	/*
	*	Call the getSupplier function
	*	Return the result set obtained
	*/
	public ResultSet selectAll(Connection conn) throws SQLException, Exception{
		CallableStatement cs = conn.prepareCall("begin ? := displayTable.getSuppliers(); end;");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		return (ResultSet)cs.getObject(1);
	}
	/*
	*	Parse a result set into an array list of objects
	*/
	public ArrayList<Supplier> parseResultSet(ResultSet rs) throws SQLException{
		//Loop through the result set creating a new Supplier
		//object and pushing it onto the arrays
		ArrayList<Supplier> elements = new ArrayList<Supplier>();
		while(rs.next()){
			elements.add(new Supplier(rs));
		}
		return elements;
	}
	/*
	*	Output a list of Supplier objects to std out
	*/
	public void outputList(ArrayList<Supplier> pl){									
		System.out.println("SID\tSNAME\tCITY\tTELEPHONE#");
		for(Supplier p: pl){														
			System.out.println(p.getSid() + "\t" + p.getSname() + "\t" + p.getCity() + "\t" + p.getTelephoneNum());
		}
	}
	//accessors
	public String getSid(){
		return this.sid;
	}
	public String getSname(){
		return this.sname;
	}
	public String getCity(){
		return this.city;
	}
	public String getTelephoneNum(){
		return this.telephoneNum;
	}
}
