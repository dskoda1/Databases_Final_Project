import java.sql.*;
import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;
import java.util.Scanner;
import java.io.Console;
import java.lang.*;
import java.lang.ProcessBuilder;


public class test {


	public static void main(String args []){


		try{

			Connection conn = validateUserAndInitializeDB();
			System.out.println("Connection Successful.");

			//Loop for user input
			Scanner s = new Scanner(System.in);
			int choice = 0;
			while(true){
				choice = displayMenu();
				switch(choice){
					case 1://View records from a table
						choice = displayViewRecordsMenu();
						executeDisplayTablePackage(choice, conn);
						break;
					case 2://Insert records into a table
						choice = displayInsertRecordsMenu();
						//executeInsertRecordPackage(choice, conn);
						break;
					}

				System.out.println("Continue? 1 = yes, 0 = no.");
				String cont = s.nextLine();
				if(Integer.parseInt(cont) == 0){
					break;

				}
			}

			//Close the rs, statement, and connection
			//cs.close();
			conn.close();



		}catch(SQLException se){
			se.printStackTrace();
			System.out.println(se.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	/*
	*	Main menu.
	*/
	public static int displayMenu(){

		//Display the menu
		System.out.println("Please make a selection.");
		System.out.println("\t1. View records from a table");
		System.out.println("\t2. Insert records into a table");
		//System.out.println... Add more options here

		int retVal = parseMenuInput();

		//Change these values if adding more options
		if(retVal > 0 && retVal < 3){
			return retVal;
		}
		else{
			System.out.println("Unrecognized option");
		}
		return retVal;



	}
	/*
	*	Sub menu for inserting records.
	*/
	public static int displayInsertRecordsMenu(){

		//Output menu
		System.out.println("Please select a choice below. (1 - 2)");
		System.out.println("Display records from table: ");
		System.out.println("\t1. Products");
		System.out.println("\t2. Purchases");

		int retVal = parseMenuInput();


		return retVal;
	}
	/*
	*	Sub menu for viewing records.
	*/
	public static int displayViewRecordsMenu(){

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

		//Get the users input
		int retVal = parseMenuInput();

		//Change these values if adding more options
		if(retVal > 0 && retVal < 8){
			return retVal;
		}else{
			System.out.println("Unrecognized option");
		}
		return retVal;


	}

	/*
	*	Function that parses users input
	*	to any given menu.
	*/
	public static int parseMenuInput(){

		//Get user input
		Scanner s = new Scanner(System.in);
		String choice = s.nextLine();
		int retVal = -1;
		try{
			if(choice != null && choice != ""){
				retVal = Integer.parseInt(choice);
			}
			else{
				retVal = -1;
			}
		}catch(NumberFormatException nfe){
			//Attempted to parse a string which is not an Integer
			retVal = -1;
		}catch(Exception e){
			retVal = -1;
		}
		return retVal;

	}

	/*
	*	Execute the 'insertRecord' package with the
	*	appropriate function based on users choice.
	*/
	public static void executeInsertRecordPackage(int choice, Connection conn) throws SQLException, Exception{

		return;
	}


	/*
	*	Execute the 'displayTable' package with the
	*	appropriate function based on users choice.
	*/
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
			default:
				System.out.println("Unrecognized option: " + choice);
				return;
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

		cs.close();
		return;

	}



	/*
	*	Acquire the users login and password for use in connecting.
	*	Return the connection object to be used in queries.
	*/
	public static Connection validateUserAndInitializeDB() throws SQLException, IOException, InterruptedException{

			//Get the connection all set up
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
			/*
			This code below breaks the program for some reason. It definitely runs, and it does
			run the scripts we need, but for some reason running it breaks the connection and
			none of the packages functions work after it

			I think were gonna just need to run project2script and compile any packages from
			inside sqlplus when we demo, and before we try and run our code here when we add to
			the packages.
			*/

			//Run the initialize scripts here
//			System.out.println("Connecting to sqlplus");
//			System.out.println("Running init scripts");
//			//Need to redirect to >/dev/null 2>&1 otherwise process hangs
//			String[] args = new String[] {"sqlplus", username + "/" + password + "@acad111", "@project2script" , ">/dev/null 2>&1"};
//			Process proc = new ProcessBuilder(args).redirectErrorStream(true).start();
//			System.out.println("Compiling packages");
//			args = new String[] {"sqlplus", username + "/" + password + "@acad111", "@displayTable", ">/dev/null 2>&1"};
//			proc = new ProcessBuilder(args).redirectErrorStream(true).start();


			return conn;
	}




}
