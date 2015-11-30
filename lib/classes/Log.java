package classes;
import java.lang.*;
import java.io.*;
import java.sql.*;
import oracle.jdbc.*;
import java.util.ArrayList;
/**
 * Log(ResultSet rs) throws SQLException
 * ResultSet selectAll(Connection conn) throws SQL exception
 * ArrayList<Log> parseResultSet(ResultSet rs) throws SQLException
 * void outputList(ArrayList<Log>)
 */
public class Log {
	private String logID, who, otime, table_name, operation, key_value;
	
	public Log(){
		super();
	}
	/*
	*	Take in a result set and initialize.
	*/
	public Log(ResultSet rs) throws SQLException{
		this.logID = rs.getString(1);
		this.who = rs.getString(2);
		this.otime = rs.getString(3);
		this.table_name = rs.getString(4);
		this.operation = rs.getString(5);
		this.key_value = rs.getString(6);
	}
	
	/*
	*	Call the getLog function
	*	Return the result set obtained
	*/
	public ResultSet selectAll(Connection conn) throws SQLException, Exception{
		CallableStatement cs = conn.prepareCall("begin ? := displayTable.getLogs(); end;");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		return (ResultSet)cs.getObject(1);
	}
	/*
	*	Parse a result set into an array list of objects
	*/
	public ArrayList<Log> parseResultSet(ResultSet rs) throws SQLException{
		//Loop through the result set creating a new Log
		//object and pushing it onto the arrays
		ArrayList<Log> elements = new ArrayList<Log>();
		while(rs.next()){
			elements.add(new Log(rs));
		}
		return elements;
	}
	/*
	*	Output a list of Log objects to std out
	*/
	public void outputList(ArrayList<Log> pl){									
		System.out.println("LOG#\tWHO\tOTIME\tTABLE_NAME\tOPERATION\tKEY_VALUE");
		for(Log p: pl){														
			System.out.println(p.getLogID() + "\t" + p.getWho() + "\t" + p.getOTime() + "\t" + p.getTableName() + "\t" + p.getOperation() + "\t" + p.getKeyValue());
		}
	}
	//accessors
	public String getLogID(){
		return this.logID;
	}
	public String getWho(){
		return this.who;
	}
	public String getOTime(){
		return this.otime;
	}
	public String getTableName(){
		return this.table_name;
	}
	public String getOperation(){
		return this.operation;
	}
	public String getKeyValue(){
		return this.key_value;
	}


	
}
