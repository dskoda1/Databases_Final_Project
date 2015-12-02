package classes;
import java.lang.*;
import java.io.*;
import java.sql.*;
import oracle.jdbc.*;
import java.util.ArrayList;
/**
 * Supply(ResultSet rs) throws SQLException
 * ResultSet selectAll(Connection conn) throws SQL exception
 * ArrayList<Supply> parseResultSet(ResultSet rs) throws SQLException
 * void outputList(ArrayList<Supply>)
 */
public class Supply {
	private String supID, pid, sid, sdate;
	private int quantity;
	
	public Supply(){
		super();
	}
	/*
	*	Take in a result set and initialize.
	*/
	public Supply(ResultSet rs) throws SQLException{
		this.supID = rs.getString(1);
		this.pid = rs.getString(2);
		this.sid = rs.getString(3);
		this.sdate = rs.getString(4);
		this.quantity = rs.getInt(5);
	}
	
	/*
	*	Call the getSupply function
	*	Return the result set obtained
	*/
	public ResultSet selectAll(Connection conn) throws SQLException, Exception{
		CallableStatement cs = conn.prepareCall("begin ? := displayTable.getSupply(); end;");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		return (ResultSet)cs.getObject(1);
	}
	/*
	*	Parse a result set into an array list of objects
	*/
	public ArrayList<Supply> parseResultSet(ResultSet rs) throws SQLException{
		//Loop through the result set creating a new Supply
		//object and pushing it onto the arrays
		ArrayList<Supply> elements = new ArrayList<Supply>();
		while(rs.next()){
			elements.add(new Supply(rs));
		}
		return elements;
	}
	/*
	*	Output a list of Supply objects to std out
	*/
	public void outputList(ArrayList<Supply> pl){									
		System.out.println("SUP#\tPID\tSID\tSDATE\tQUANTITY");
		for(Supply p: pl){														
			System.out.println(p.getSupID() + "\t" + p.getPid() + "\t" + p.getSid() + "\t" + p.getSDate() + "\t" + p.getQuantity());
		}
	}
	//accessors
	public String getSupID(){
		return this.supID;
	}
	public String getPid(){
		return this.pid;
	}
	public String getSid(){
		return this.sid;
	}
	public String getSDate(){
		return this.sdate.substring(0,10);
	}
	public int getQuantity(){
		return this.quantity;
	}
}
