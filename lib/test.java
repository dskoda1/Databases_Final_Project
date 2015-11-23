import java.sql.*;
import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;
import java.util.Scanner;
import java.io.Console;
public class test {


	public static void main(String args []){

		try{

			Connection conn = validateUser();			
	
			//Prepare the statement
			CallableStatement cs = conn.prepareCall("begin ? := displayTable.getEmployees(); end;");
	
			//Register the out parameter
			cs.registerOutParameter(1, OracleTypes.CURSOR);

			//Execute
			cs.execute();
			ResultSet rs = (ResultSet)cs.getObject(1);
			
			while (rs.next()) {
			
				System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));

			}

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

	public static Connection validateUser() throws SQLException{

			oracle.jdbc.pool.OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
			System.out.println("Username:");
			Scanner s = new Scanner(System.in);
			String username = s.nextLine();
			System.out.println("Password for " + username);
			Console c;
			c = System.console();
			String password = new String(c.readPassword());
			Connection conn = ds.getConnection(username, password);

			return conn;
	}
}


