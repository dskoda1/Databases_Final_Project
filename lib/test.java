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

			//Loop for user input
			while(1){
					int choice = displayTableUserInputParser();
					executeDisplayTablePackage(coice, conn);
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

	public static void executeDisplayTablePackage(int choice, Connection conn) throws SQLException, Exception{

		String procedure = "";

		switch(choice){
			case 1: procedure = "getEmployees";
			break;
			case 2: procedure = "getCustomers";
			break;
			case 3: procedure = "getProducts";
			break;
			case 4: procedure = "getSuppliers";
			break;
			case 5: procedure = "getSupply";
			break;
			case 6: procedure = "getPurchases";
			break;
			case 7: procedure = "getLogs";
				break;
		}

		//Prepare the statement
		CallableStatement cs = conn.prepareCall("begin ? := displayTable." + procedure + "(); end;");

		//Register the out parameter
		cs.registerOutParameter(1, OracleTypes.CURSOR);

		//Execute
		cs.execute();
		ResultSet rs = (ResultSet)cs.getObject(1);


		//Need to decide how to parse the result set based on which table it is.
		//This only supports getting the first 3 columns of whatever table is queried.
		//Need to talk about design choice here, couple of options available
		while (rs.next()) {

			System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));

		}

		return;

	}

	public static int displayTableUserInputParser(){

			//Output menu
			System.out.println("Please select a choice below. (1 - 6)");
			System.out.println("Display records from table: ");
			System.out.println("\t1. Employees");
			System.out.println("\t2. Customers");
			System.out.println("\t3. Products");
			System.out.println("\t4. Suppliers");
			System.out.println("\t5. Supply");
			System.out.println("\t6. Purchases");
			System.out.println("\t7. Logs");

			//Get user input
			Scanner s = new Scanner(System.in);
			String choice = s.nextLine();
			int val = -1;
			try{
				if(choice != null && choice != ""){
					val = Integer.parseInt(choice);
				}
				else{
					//Could throw exception here to be handled higher up
					//or just return
					return -1;
				}
			}catch(NumberFormatException nfe){
				//Attempted to parse a string which is not an Integer
				return -1;
			}catch(Exception e){
				return -1;
			}

			if(val > 0 && val < 8){
				return val;
			}else{
				return -1;
			}


	}
	/*
	Acquire the users login and password for use in connecting.
	Return the connection object to be used in queries.
	*/
	public static Connection validateUser() throws SQLException{

			oracle.jdbc.pool.OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
			System.out.println("Username:");
			Scanner s = new Scanner(System.in);
			String username = s.nextLine();
			System.out.println("Password for " + username);
			//Use console object in order to read password without
			//showing characters on the command line
			Console c;
			c = System.console();
			String password = new String(c.readPassword());
			Connection conn = ds.getConnection(username, password);

			return conn;
	}





}
