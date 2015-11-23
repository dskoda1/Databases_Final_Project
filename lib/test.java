import java.sql.*;
import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.oracleDataSource;
public class test {


	public static void main(String args []){

		try{
			//Set up the connection
			oracle.jdbc.pool.OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
			Connection conn = ds.getConnection("dskoda1", "David926");
	
			//Prepare the statement
			CallableStatement cs = conn.prepareCall("begin ? := displayTable.getEmployees(); end;");
	
			//Register the out parameter
			cs.registerOutParameter(1, OracleTypes.CURSOR);

			//Execute
			cs.execute();
			ResultSet rs = (ResultSet)cs.getObject(1);


			//Close the rs, statement, and connection
			cs.close();
			conn.close();



		}catch(SQLException se){
			se.printStackTrace();
			System.out.println(se.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
