import java.lang.*;
import java.io.*;
import java.sql.*;
import oracle.jdbc.*;
import java.util.ArrayList;


public class Purchase {

	private String eid;
	private String pid;
	private String cid;
	private int qty;
	private String date;
	private float total_price;


	public Purchase(){
		super();
	}

	public Purchase(ResultSet rs) throws SQLException{
		this.eid = rs.getString(1);
		this.pid = rs.getString(2);
		this.cid = rs.getString(3);
		this.qty = rs.getInt(4);
		this.date = rs.getString(5);
		this.total_price = rs.getFloat(6);
	}

	public ResultSet selectAll(Connection conn) throws SQLException, Exception{

		CallableStatement cs = conn.prepareCall("begin ? := dislayTable.getPurchases(); end;");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		return (ResultSet)cs.getObject(1);
	}

	public ArrayList<Purchase> parseResultSet(ResultSet rs) throws SQLException{

		//Loop through the result set creating a new Purchase
		//object and pushing it onto the arrays
		ArrayList<Purchase> elements = new ArrayList<Purchase>();
		while(rs.next()){

			elements.add(new Purchase(rs));

		}

		return elements;

	}

	public String getEid(){
		return this.eid;
	}
	public String getPid(){
		return this.pid;
	}
	public String getCid(){
		return this.cid;
	}
	public int getQty(){
		return this.qty;
	}
	public String getDate(){
		return this.date;
	}
	public float getTotalPrice(){
		return this.total_price;
	}



}
