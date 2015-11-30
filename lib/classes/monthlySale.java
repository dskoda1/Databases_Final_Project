package classes;
import java.lang.*;
import java.io.*;
import java.sql.*;
import oracle.jdbc.*;
import java.util.ArrayList;
/**
 * monthlySale(ResultSet rs) throws SQLException
 * ResultSet selectAll(Connection conn) throws SQL exception
 * ArrayList<monthlySale> parseResultSet(ResultSet rs) throws SQLException
 * void outputList(ArrayList<monthlySale>)
 */
public class monthlySale {
	private String pname, month, year;
	private int qty_sold;
	private float total_spent, average_price;
	
	public monthlySale(){
		super();
	}
	/*
	*	Take in a result set and initialize.
	*/
	public monthlySale(ResultSet rs) throws SQLException{
		this.pname = rs.getString(1);
		this.month = rs.getString(2);
		this.year = rs.getString(3);
		this.qty_sold = rs.getInt(4);
		this.total_spent = rs.getFloat(5);
		this.average_price = rs.getFloat(6);
	}
	
	/*
	*	Call the getmonthlySale function
	*	Return the result set obtained
	*/
	public ResultSet selectAll(Connection conn) throws SQLException, Exception{
		CallableStatement cs = conn.prepareCall("begin ? := displayTable.getmonthlySales(); end;");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		return (ResultSet)cs.getObject(1);
	}
	/*
	*	Parse a result set into an array list of objects
	*/
	public ArrayList<monthlySale> parseResultSet(ResultSet rs) throws SQLException{
		//Loop through the result set creating a new monthlySale
		//object and pushing it onto the arrays
		ArrayList<monthlySale> elements = new ArrayList<monthlySale>();
		while(rs.next()){
			elements.add(new monthlySale(rs));
		}
		return elements;
	}
	/*
	*	Output a list of monthlySale objects to std out
	*/
	public void outputList(ArrayList<monthlySale> pl){									
		System.out.println("PNAME\tMONTH\tYEAR\tQTY_SOLD\tTOTAL_SPENT\tAVERAGE_PRICE");
		for(monthlySale p: pl){														
			System.out.println(p.getPname() + "\t" + p.getMonth() + "\t" + p.getYear() + "\t" + p.getQtySold() + "\t" + p.getTotalSpent() + "\t" + p.getAveragePrice());
		}
	}
	//accessors
	public String getPname(){
		return this.pname;
	}
	public String getMonth(){
		return this.month;
	}
	public String getYear(){
		return this.year;
	}
	public int getQtySold(){
		return this.qty_sold;
	}
	public float getTotalSpent(){
		return this.total_spent;
	}
	public float getAveragePrice(){
		return this.average_price;
	}


	
}
