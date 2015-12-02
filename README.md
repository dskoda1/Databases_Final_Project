##CS532 Project 2- Retail Business Management System

From the lib directory
###### To compile:

    javac -cp .:ojdbc.jar Main.java classes/*.java
###### then to run:

    java -cp .:ojdbc.jar Main classes/*.class

CS432 Database Systems

Fall 2015

Project 2 README FILE

Due Date: Tuesday, December 1, 2015

Author: Joseph Grillo and David Skoda

email: jgrillo1@binghamton.edu, dskoda1@binghamton.edu


Purpose:

The goal of this assignment was to construct a fully functional database system
through the use of PL/SQL programming as well as a java interface. The database
represents a transactional dataset which includes entity sets such as employees, 
customers, products, purchases, supplys, suppliers. The program displays all of
these tables as well as a log table which stores updates to the database. The 
program also allows the insertion of new products and purchases into the database.
A function for viewing a monthly sales report per selected product is also available.

Percent Complete:

Everything in this assignment has been completed.

Bugs:

No bugs.

Files:

Inside of the lib folder:

Main.java- Actually runs our interface
allTriggers.sql- contains all of our triggers for the database system
displayTable.sql- package which contains functions to display all of 
the different tables in our system
insertRecord.sql- package which contains procedures to add purchases 
or products to our system
monthlySale.sql- package which contains a function to get the monthly
sales report
ojdbc.jar- java compiler
Proj2data.sql- holds all our initial creations of the system
dropTables.sql- drops our entire system(tables, triggers, sequences)
initScript.sql- runs all of the scripts in the proper order

Inside of the classes folder:

Customer.java, Employee.java, Supplier.java, Supply.java, 
Product.java, Purchase.java, Log.java, monthlySale.java
All these files are used to interact with the sql code
Basically just used for displaying tables from sql functions
