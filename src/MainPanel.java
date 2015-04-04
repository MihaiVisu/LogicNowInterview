import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.sql.*;

public class MainPanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // serial version of MainPanel
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	static private Connection conn = null;
	static private Statement stmt = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
						
		try {
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection("jdbc:mysql://localhost/LogicNowInterview","root","");
		      System.out.println("Connected to database successfully.");
		      System.out.println("Creating Statement...");
		      stmt=conn.createStatement();
		      System.out.println("Statement created successfully.");
		}
		catch(SQLException se) {
		      //Handle errors for JDBC
		      se.printStackTrace();
		}
		catch(Exception e) {
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPanel frame = new MainPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * using to throw sql exception errors if the statements and queries fail to execute
	 * @throws SQLException
	 */
	public MainPanel() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setBounds(6, 28, 72, 16);
		contentPane.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(6, 56, 70, 16);
		contentPane.add(lblLastName);
		
		textField = new JTextField();
		textField.setBounds(87, 22, 134, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(87, 50, 134, 28);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		final DefaultListModel listModel = new DefaultListModel(); 
		
		String sql = "SELECT *FROM Contacts";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			String addition = rs.getString("FirstName")+" "+rs.getString("LastName");
			listModel.addElement(addition);
		}
		// Close statement
		stmt.close();
		
		final JList list = new JList(listModel);
		list.setBounds(233, 56, 211, 216);
		contentPane.add(list);
		
		JButton btnInsertContact = new JButton("Insert Contact");
		btnInsertContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String firstName = textField.getText();
				String lastName = textField_1.getText();
				
				if(firstName.isEmpty()) {
					System.out.println("First input is empty!");
					return;
				}
				if(lastName.isEmpty()) {
					System.out.println("Second input is empty!");
					return;
				}				
				Contact c1 = new Contact(firstName,lastName);
				if(c1.checkOccurences()>0) {
					System.out.println("Contact already exists !!!");
					return;
				}
				c1.save();
				
				//adding the new Contact to the list;
				listModel.addElement(c1.toString());
				//Rendering the new element on the visual list;
				list.setModel(listModel);
			}
		});
		btnInsertContact.setBounds(88, 90, 133, 29);
		contentPane.add(btnInsertContact);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(220, 6, 12, 266);
		contentPane.add(separator);
		
		JButton btnDeleteContact = new JButton("Delete Contact");
		btnDeleteContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String selected = (String) list.getSelectedValue();
				String[] name = new String[2];
				if(selected == null) {
					System.out.println("No contact selected !!!");
					return;
				}
				name=selected.split(" ");
				Contact c1 = new Contact(name[0],name[1]);
				int index = list.getSelectedIndex();
				if(index >= 0) {
					//remove item from database
					c1.delete();
					//remove contact from list
					listModel.removeElementAt(index);
				}
				else {
					System.out.println("No contact selected !!!");
					return;
				}
				//re-render the list after deleting the element
				list.setModel(listModel);
				
			}
		});
		btnDeleteContact.setBounds(87, 131, 134, 29);
		contentPane.add(btnDeleteContact);
		
		JLabel lblContacts = new JLabel("Contacts:");
		lblContacts.setBounds(233, 28, 60, 16);
		contentPane.add(lblContacts);
		
		JButton btnEditSelectedContact = new JButton("Edit Selected Contact");
		btnEditSelectedContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int index = list.getSelectedIndex();
				if(index >= 0) {
					String firstName = textField.getText();
					String lastName = textField_1.getText();
					Contact c1 = new Contact (firstName,lastName);
					if(c1.checkOccurences()>0) {
						System.out.println("Contact already exists !!!");
						return;
					}
					// save new contact in the database
					c1.save();
					//create contact of old data
					String[] name = new String[2];
					// get the selected contact in a string
					String selected = (String) list.getSelectedValue();
					// split to get first and last names
					name=selected.split(" ");
					//create contact from selected values
					Contact c2 = new Contact(name[0],name[1]);
					//delete old contact from database
					c2.delete();
					// edit contact data in list model
					listModel.set(index, c1.toString());
					// re-render the data in the list
					list.setModel(listModel);
					System.out.println("Contact edited successfully.");
					
				}
				else {
					System.out.println("No contact selected !!!");
					return;
				}
				
			}
		});
		btnEditSelectedContact.setBounds(44, 172, 177, 29);
		contentPane.add(btnEditSelectedContact);
		
		// Closing connection and statement at the end of the constructor for the Frame
		conn.close();
		stmt.close();
	}
}
