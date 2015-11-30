package classes;
import java.lang.*;
import java.io.*;
import java.sql.*;
import oracle.jdbc.*;
import java.util.ArrayList;
/**
 * Product(ResultSet rs) throws SQLException
 * ResultSet selectAll(Connection conn) throws SQL exception
 * ArrayList<Product> parseResultSet(ResultSet rs) throws SQLException
 * void outputList(ArrayList<Product>)
 */
public class Product {
	private String pid, pname;
	private int qoh, qoh_threshold;
	private float original_price, discnt_rate;
	
	public Product(){
		super();
	}
	/*
	*	Take in a result set and initialize.
	*/
	public Product(ResultSet rs) throws SQLException{
		this.pid = rs.getString(1);
		this.pname = rs.getString(2);
		this.qoh = rs.getInt(3);
		this.qoh_threshold = rs.getInt(4);
		this.original_price = rs.getFloat(5);
		this.discnt_rate = rs.getFloat(6);
	}
	
	/*
	*	Call the getProduct function
	*	Return the result set obtained
	*/
	public ResultSet selectAll(Connection conn) throws SQLException, Exception{
		CallableStatement cs = conn.prepareCall("begin ? := displayTable.getProducts(); end;");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		return (ResultSet)cs.getObject(1);
	}
	/*
	*	Parse a result set into an array list of objects
	*/
	public ArrayList<Product> parseResultSet(ResultSet rs) throws SQLException{
		//Loop through the result set creating a new Product
		//object and pushing it onto the arrays
		ArrayList<Product> elements = new ArrayList<Product>();
		while(rs.next()){
			elements.add(new Product(rs));
		}
		return elements;
	}
	/*
	*	Output a list of Product objects to std out
	*/
	public void outputList(ArrayList<Product> pl){									
		System.out.println("PID\tPNAME\tQOH\tQOH_THRESHOLD\tORGINIAL_PRICE\tDISCNT_RATE");
		for(Product p: pl){														
			System.out.println(p.getPid() + "\t" + p.getPname() + "\t" + p.getQoh() + "\t" + p.getQohThreshold() + "\t" + p.getOriginalPrice() + "\t" + p.getDiscntRate());
		}
	}
	//accessors
	public String getPid(){
		return this.pid;
	}
	public String getPname(){
		return this.pname;
	}
	public int getQoh(){
		return this.qoh;
	}
	public int getQohThreshold(){
		return this.qoh_threshold;
	}
	public float getOriginalPrice(){
		return this.original_price;
	}
	public float getDiscntRate(){
		return this.discnt_rate;
	}


	
}
