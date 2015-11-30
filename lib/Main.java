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
import java.util.ArrayList;
import javax.swing.*;
import classes.*;

public class Main {
	public static boolean debug = false;

	public static void main(String args []){

		try{

			Connection conn = validateUserAndInitializeDB();
			
			
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
						executeInsertRecordPackage(choice, conn);
						break;
					}
				//Need some exception handling here to ensure input is valid- 1 or 0
				System.out.println("Continue? 1 = yes, 0 = no.");
				String cont = s.nextLine();
				if(Integer.parseInt(cont) == 0){
					break;

				}
			}

			//Close the rs, statement, and connection
			//cs.close();
			conn.close();


		//Should rarely go to these catch blocks.. want to keep exceptions contained
		//to the level below main. So that execution can continue, not end.
		}catch(SQLException se){
			System.out.println ("\n*** SQLException caught ***\n");
			System.out.println(se.getMessage());
		}catch(Exception e){
			System.out.println ("\n*** Exception caught ***\n");
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
		System.out.println("Insert a record into table: ");
		System.out.println("\t1. Products");
		System.out.println("\t2. Purchases");

		int retVal = parseMenuInput();
		if(retVal > 0 && retVal < 3)
		{
			return retVal;
		}else{
			System.out.println("Unrecognized option");
		}
		return retVal;
		
	}
	/*
	*	Sub menu for viewing records.
	*/
	public static int displayViewRecordsMenu(){

		//Output menu
		System.out.println("Please select a choice below. (1 - 7)");
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
	public static void executeInsertRecordPackage(int choice, Connection conn){
		Scanner s = new Scanner(System.in);
		String in = "", exec;
		CallableStatement cs = null;
		int val = 0;
		/*
		*	Need some input from the user here based on choice.
		*	1. insertRecord.addProduct(string pid, string pname, int qoh,
		*	int qoh_threshold, double original_price, double discnt_rate)
		*	2. insertRecord.addPurchase(string eid, string pid, string cid, int qty)
		*/
		

		try{

			System.out.println("\nPlease provide the following information: \n");
			if(choice == 1){
				String pid, pname;
				int qoh, qohThreshold;
				double originalPrice, discntRate;

				//pid
				System.out.println("\nNew product id in the form 'p003': ");
				pid = s.nextLine();
				//pname
				System.out.println("\nNew product's name: ");
				pname= s.nextLine();
			
				//qoh
				System.out.println("\nNew product's current quantity on hand: ");
				try{
				qoh = Integer.parseInt(s.nextLine());
				}catch(NumberFormatException nfe){
					System.out.println("\n*** Invalid number given for QOH value when attempting to -Insert Product Record-. Please try again with an 'integer' value.");
					return;			
				}
				//qoh threshold
				System.out.println("New product's quantity on hand threshold: ");
				try{
				qohThreshold= Integer.parseInt(s.nextLine());
				}catch(NumberFormatException nfe){
					System.out.println("\n*** Invalid number given for QOH Threshold value when attempting to -Insert Product Record-. Please try again with an 'integer' value.");
					return;			
				}

				//orig price
				System.out.println("\nNew product's price in the form '24.99' or just '24': ");
				try{
				originalPrice = Double.parseDouble(s.nextLine());
				}catch(NumberFormatException nfe){
					System.out.println("\n*** Invalid number given for original price when attempting to -Insert Product Record-. Please try again with an 'double' value in the form '24.00' or just '24'.");
					return;			
				}

				//discount rate
				System.out.println("\nNew product's discount rate in the form '0.3' or just '.3'. Must be between 0 and 0.8: ");
				try{
				discntRate = Double.parseDouble(s.nextLine());
				}catch(NumberFormatException nfe){
					System.out.println("\n*** Invalid number given for discount rate when attempting to -Insert Product Record-. Please try again with an 'double' value in the form '0.3'.");
					return;			
				}
				//Create and prepare the call
				cs = conn.prepareCall("begin insertRecord.addProduct(?, ?, ?, ?, ?, ?); end;");
				cs.setString(1, pid);
				cs.setString(2, pname);
				cs.setInt(3, qoh);
				cs.setInt(4, qohThreshold);
				cs.setDouble(5, originalPrice);
				cs.setDouble(6, discntRate);

	
			}else if (choice == 2){
				String eid, pid, cid;
				int qty;			
				
				//eid
				System.out.println("Employee ID in the form 'e03': ");
				eid = s.nextLine();
	
				//pid
				System.out.println("\nProduct ID in the form 'p006': ");
				pid = s.nextLine();
				
				//cid
				System.out.println("\nCustomer ID in the form 'c001': ");
				cid = s.nextLine();
				
				//qty
				System.out.println("\nQuantity purchased as a number: ");
				try{
				qty = Integer.parseInt(s.nextLine());
				}catch(NumberFormatException nfe){
					System.out.println("\n*** Invalid number given for quantity value when attempting to -Insert Purchase Record-. Please try again with an 'integer' value.");
					return;			
				}
				
				//Create and prepare the call
				cs = conn.prepareCall("begin insertRecord.addPurchase(?, ?, ?, ?); end;");
				cs.setString(1, eid);
				cs.setString(2, pid);
				cs.setString(3, cid);
				cs.setInt(4, qty);
					
			}

			//Execute the statement now
			cs.execute();
		
		}catch(SQLException se){
                        System.out.println ("\n*** SQLException caught when attempting to insert a record ***\n");
                        System.out.println(se.getMessage());
			return;
		}catch(NullPointerException npe){
			System.out.println("\n*** Null Pointer Exception caught when attempting to insert a record ***\n");
			return;
		}catch(Exception e){
                        System.out.println ("\n*** Exception caught when attempting to insert a record ***\n");
                        System.out.println(e.getMessage());
			return;
                }
		finally{
			try{
				if(cs != null){
					cs.close();
				}
			}catch(SQLException se){
			System.out.println("Error closing the callable statement in insert record");
			}catch(NullPointerException npe){
			System.out.println("\n*** Null Pointer Exception caught when attempting to insert a record ***\n");
			System.out.println("Error closing the callable statement in insert record");
			return;
			}
		}

		System.out.println("\nInsert executed successfully!\n");

		return;
	}
	/*
	*	Execute the 'displayTable' package with the
	*	appropriate function based on users choice.
	*/
	public static void executeDisplayTablePackage(int choice, Connection conn) throws SQLException, Exception{

		String procedure = "";

		switch(choice){
			case 1:{
				//oldy
				//procedure = "getEmployees";
				Employee e = new Employee();
				ResultSet rs = e.selectAll(conn);
				ArrayList<Employee> employees = e.parseResultSet(rs);
				e.outputList(employees);
				return;
				//break;
			}
			case 2:{
				//old line
				//procedure = "getCustomers";
				//new
				Customer c = new Customer();
				ResultSet rs = c.selectAll(conn);
				ArrayList<Customer> customers = c.parseResultSet(rs);
				c.outputList(customers);
				return;
				//break;
			}
			case 3:{
				//OLD
				//procedure = "getProducts";
				Product p = new Product();
				ResultSet rs = p.selectAll(conn);
				ArrayList<Product> products = p.parseResultSet(rs);
				p.outputList(products);
				return;
				//break;
			}

			case 4:{
				//out with the old
				//procedure = "getSuppliers";
				Supplier s = new Supplier();
				ResultSet rs = s.selectAll(conn);
				ArrayList<Supplier> suppliers = s.parseResultSet(rs);
				s.outputList(suppliers);
				return;
				//break;
			}
			case 5: {
				//old
				//procedure = "getSupply";
				Supply s = new Supply();
				ResultSet rs = s.selectAll(conn);
				ArrayList<Supply> supplies = s.parseResultSet(rs);
				s.outputList(supplies);
				return;
				//break;
			}
			case 6:{
				//old
				//procedure = "getPurchases";
				Purchase p = new Purchase();
				ResultSet rs = p.selectAll(conn);
				ArrayList<Purchase> purchases = p.parseResultSet(rs);
				p.outputList(purchases);
				return;
				//break;
			}
			case 7:{
				//procedure = "getLogs";
				Log l = new Log();
				ResultSet rs = l.selectAll(conn);
				ArrayList<Log> logs = l.parseResultSet(rs);
				l.outputList(logs);
				return;
			}
			default:
				System.out.println("Unrecognized option: " + choice);
				return;
		}
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
			System.out.println("\nConnection Successful.\n\n");
			
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
