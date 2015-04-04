import java.sql.*;

public class Contact {
	
	private Connection conn = null;
	private Statement stmt = null;
	
	private String firstName;
	private String lastName;
	
	// constructors for the Contact class
	
	public Contact() {
		firstName = "";
		lastName = "";
	}
	
	public Contact(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// methods including getters and setters for Contact class
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
		
	public void save() {
		try {
		      // Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");
		      // Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection("jdbc:mysql://localhost/LogicNowInterview","root","");
		      System.out.println("Connected to database successfully");
		      System.out.println("Creating Statement...");
		      stmt=conn.createStatement();
		      System.out.println("Statement created successfully.");
		      // Executing insert query
		      System.out.println("Inserting contact into database...");
		      String sql2 = "INSERT INTO Contacts (FirstName,LastName) VALUES ('"+firstName+"','"+lastName+"')";
		      System.out.println("Contact inserted successfully.");
		      stmt.executeUpdate(sql2);
		      //closing statement and connection after executing the query
		      stmt.close();
		      conn.close();
		}
		catch(SQLException se) {
		      //Handle errors for JDBC
		      se.printStackTrace();
		}
		catch(Exception e) {
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
	}
	
	public void delete() {
		try {
		      // Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      // Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection("jdbc:mysql://localhost/LogicNowInterview","root","");
		      System.out.println("Creating Statement...");
		      stmt=conn.createStatement();
		      System.out.println("Statement created successfully");
		      // Executing delete query
		      System.out.println("Deleting contact from database...");
		      String sql = "DELETE FROM Contacts Where FirstName = '"+firstName+"' AND LastName = '"+lastName+"'";
		      System.out.println("Contact deleted successfully.");
		      stmt.executeUpdate(sql);
		      // Closing statement and connection after executing the query
		      stmt.close();
		      conn.close();
		      
		}
		catch(SQLException se) {
		      //Handle errors for JDBC
		      se.printStackTrace();
		}
		catch(Exception e) {
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
	}
	
	public int checkOccurences() {
		//check how many contacts of the same type already exist in the database
		int count = 0;
		
		String sql = "SELECT *FROM Contacts WHERE FirstName = '"+firstName+"' AND LastName = '"+lastName+"'";
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/LogicNowInterview","root","");
			System.out.println("Connected to database successfully.");
			System.out.println("Creating Statement...");
			stmt=conn.createStatement();
			System.out.println("Statement created successfully.");
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				count++;
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// print error if statement fails to execute
			e.printStackTrace();
		} catch(Exception e) {
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
		return count;
	}
	
	
	@Override
	public String toString() {
		return firstName+" "+lastName; 	
	}

}
