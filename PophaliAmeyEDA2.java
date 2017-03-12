import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PophaliAmeyEDA2 {

	private static Employee[] employee = new Employee[100]; // Create the main
															// employee array
	private static PophaliAmeyEDA2 edaObj = new PophaliAmeyEDA2();
	private static int EmpCount = 0; // The global employee EmpCount
	private static JFrame UserFrame = new JFrame(); // Create user frame
	private static JTextField UserTextInput = new JTextField(); // Create JFeild for
														// taking input from the
														// user
	private static JTextArea OutputArea = new JTextArea(" ", 5, 20); // Create Text Area
																// to display
																// results or
																// errors

	/* Start the program */
	public static void main(String[] args) {

		/*
		 * Create and set properties for Label (label) on the Frame. The label
		 * guides the user on how where to input the command
		 */
		JLabel label = new JLabel("Input command and click Enter");
		label.setFont(new Font("Helvetica", Font.PLAIN, 20));
		label.setDisplayedMnemonic(KeyEvent.VK_N);
		label.setLabelFor(UserTextInput);

		/*
		 * Set properties for Text Field Add an ActionListener and Call the
		 * 'enterButtonPressed' function to run the command entered by the user
		 */
		UserTextInput.setPreferredSize(UserTextInput.getPreferredSize());
		UserTextInput.setFont(new Font("Helvetica", Font.ITALIC, 20));
		UserTextInput.requestFocus();
		UserTextInput.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edaObj.enterButtonPressed();
			}
		});

		/*
		 * Create and set properties for Save Button Add an ActionListener for
		 * the button and Call the 'SaveButtonPressed' function when the button
		 * is pressed
		 */
		JButton SaveButton = new JButton();
		SaveButton.setText("Save");
		SaveButton.setFont(new Font("Helvetica", Font.BOLD, 20));
		SaveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edaObj.SaveButtonPressed(employee);
			}
		});

		/*
		 * Create and set properties for Save Button Add an ActionListener for
		 * the button and Call the 'SaveButtonPressed' function when the button
		 * is pressed
		 */
		JButton LoadButton = new JButton();
		LoadButton.setText("Load");
		LoadButton.setFont(new Font("Helvetica", Font.BOLD, 20));
		LoadButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edaObj.LoadButtonPressed();
			}
		});

		// Set Scroll for User Text area
		JScrollPane UserScroll = new JScrollPane(OutputArea);
		OutputArea.setFont(new Font("Helvetica", Font.PLAIN, 20));
		OutputArea.setEditable(false);

		// The panel for Text label and Text Field
		JPanel UserPanel = new JPanel(new BorderLayout());
		UserPanel.add(UserTextInput, BorderLayout.CENTER);
		UserPanel.add(label, BorderLayout.WEST);

		// The panel for Save and Load buttons
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.add(SaveButton);
		ButtonPanel.add(LoadButton);

		// Set properties of Frame and add all the elements
		UserFrame.setTitle("Employee Database");
		UserFrame.setSize(800, 800);
		UserFrame.setLocationRelativeTo(null);
		UserFrame.add(UserPanel, BorderLayout.NORTH);
		UserFrame.add(ButtonPanel, BorderLayout.SOUTH);
		UserFrame.add(UserScroll);
		UserFrame.setVisible(true);
		UserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// Saves the data from employee array to file
	public void SaveButtonPressed(Employee[] employee) {

		OutputArea.setText(""); // Clear text Area before Loading
		int SaveCount; // the number represents number of employees saved
		OutputArea.append("\n" + "Saving employee database to file...");

		try {
			FileOutputStream outStream = new FileOutputStream("EDA2.dat");
			final PrintStream printStream = new PrintStream(outStream);

			for (SaveCount = 0; SaveCount <= EmpCount; SaveCount++) {
				if (employee[SaveCount] != null) {
					printStream.println(employee[SaveCount].getLastName() + " " + employee[SaveCount].getFirstName()
							+ " " + employee[SaveCount].getDep() + " " + employee[SaveCount].getJob());
				} else
					break;
			}
			printStream.close();

			// Print EmpCount of employees saved to the text area for user
			OutputArea.append("\n" + "Number of employee(s) saved to the file = " + (SaveCount));
		}

		// Raise exception if file not present or
		// the file doesn't have proper permission to write
		catch (FileNotFoundException e) {
			OutputArea.append("\n" + "**Error while saving - File Not found**" + "\n"
					+ "Please verify if the file exists at the mentioned location.");
			OutputArea.append("\n" + "Verify the user permission on the file as well.");
		} catch (NullPointerException n) {
			OutputArea.append("\n" + "**Error while saving - Null pointer exception.**");
		}
	}

	// Loads the data from file into employee array
	public void LoadButtonPressed() {

		OutputArea.setText(""); // Clear text Area before Loading
		String line; // line represents a single line from file
		String[] inputSplit = new String[500];

		// Clear the current Database before loading from file
		for (int i = 0; i < EmpCount; i++) {
			employee[i] = null;
		}

		// Set employee EmpCount to zero after clearing the database
		EmpCount = 0;

		try {
			// Create InputSream for the file
			InputStream InStream = new FileInputStream("EDA2.dat");
			InputStreamReader InStreamR = new InputStreamReader(InStream, Charset.forName("UTF-8"));
			BufferedReader InBuffer = new BufferedReader(InStreamR);

			while ((line = InBuffer.readLine()) != null) {
				inputSplit = line.split(" ");

				if (inputSplit != null) {
					edaObj.addEmployee(inputSplit[0], inputSplit[1], inputSplit[2], inputSplit[3], EmpCount);
					EmpCount++;
				} else {
					OutputArea.append("\n" + "No data present in the mentioned file" + "\n");
				}
			}
			OutputArea.append("\n" + "Total employees in database = " + EmpCount + "\n");
			InBuffer.close();
		}

		catch (FileNotFoundException e) {
			OutputArea.append("\n" + "**Error while loading - File Not found**" + "\n"
					+ "Please verify if the file exists at the mentioned location.");
			OutputArea.append("\n" + "Verify the user permission on the file as well.");
		} catch (IOException n) {
			OutputArea.append("**Error while loading - Input Output Exception**");
		}

		/*
		 * This CATCH block catches the array out of bound exception for
		 * employee array and doubles the employee array size. This will be
		 * executed if the number of employees added by the user exceeds the
		 * current employee array length
		 */
		catch (ArrayIndexOutOfBoundsException n) {
			// Show a message dialogue that array is out of bounds
			JOptionPane.showMessageDialog(UserFrame,
					"Array out of bounds while loading from file. Changing size of array");

			OutputArea.append("\n" + " Array is out of the bounds while loading from file \n ");
			OutputArea.append("Changing the size of the array");

			// Double the length(size) of the 'employee' array
			employee = Arrays.copyOf(employee, employee.length * 2);

			OutputArea.append("Please enter input again");
		}
	}

	// Executes the user command when Enter is pressed
	public void enterButtonPressed() {
		String UserInput = UserTextInput.getText();
		OutputArea.setText("");

		String[] inputSplit = new String[500];
		inputSplit = UserInput.split("\\s");

		// Raise an error if the length of the user command
		// is not correct. Same error if incorrect action requested
		if (inputSplit.length < 3 || inputSplit.length > 5
				|| (!inputSplit[0].equalsIgnoreCase("ADD") && !inputSplit[0].equalsIgnoreCase("MODIFY")
						&& !inputSplit[0].equalsIgnoreCase("READ") && !inputSplit[0].equalsIgnoreCase("DELETE"))) {
			JOptionPane.showMessageDialog(UserFrame, "\n" + "Wrong input!! Please provide correct input command.");
			return;
		}

		// Enter this only if user wants to ADD an employee
		if (inputSplit[0].equalsIgnoreCase("ADD")) {
			try {
				if (inputSplit.length == 3) {
					edaObj.addEmployee(inputSplit[1], inputSplit[2], EmpCount);
					EmpCount++;
					OutputArea.append("\n" + "Total employee(s) in database = " + EmpCount + "\n");
				}

				// If user wants to add three parameters
				else if (inputSplit.length == 4) {
					edaObj.addEmployee(inputSplit[1], inputSplit[2], inputSplit[3], EmpCount);
					EmpCount++;
					OutputArea.append("\n" + "Total employee(s) in database = " + EmpCount + "\n");
				}

				// If user wants to add all parameters
				else if (inputSplit.length == 5) {
					edaObj.addEmployee(inputSplit[1], inputSplit[2], inputSplit[3], inputSplit[4], EmpCount);
					EmpCount++;
					OutputArea.append("\n" + "Total employee(s) in database = " + EmpCount + "\n");
				}
			}

			/*
			 * This CATCH block catches the array out of bound exception for
			 * employee array and doubles the employee array size. This will be
			 * executed if the number of employees added by the user exceeds the
			 * current employee array length
			 */
			catch (ArrayIndexOutOfBoundsException n) {
				// Show a message dialogue that array is out of bounds
				JOptionPane.showMessageDialog(UserFrame, "Array out of bounds. Changing size of array");

				OutputArea.append("\n" + " Array is out of the bounds \n ");
				OutputArea.append("Changing the size of the array");

				// Double the length(size) of the 'employee' array
				employee = Arrays.copyOf(employee, employee.length * 2);

				OutputArea.append("Please enter input again");
			}
		}

		// if user inputs modification request
		else if (inputSplit[0].equalsIgnoreCase("MODIFY")) {
			try {
				edaObj.modifyEmployee(inputSplit[1], inputSplit[2], inputSplit[3]);
			} catch (ArrayIndexOutOfBoundsException e) {
				OutputArea.append("\n" + "**Error while modifying.**");
				OutputArea.append(
						"\n" + "Array Out of Bounds exception while modifying. Please provide proper input command.");
			}
		}

		// if user wants to read database or find employee(s)
		else if (inputSplit[0].equalsIgnoreCase("READ")) {
			try {
				edaObj.readEmployee(inputSplit[1], inputSplit[2]);
			} catch (ArrayIndexOutOfBoundsException e) {
				OutputArea.append("\n" + "**Error while reading.**");
				OutputArea.append("\n"
						+ "Array Out of Bounds exception while reading the database. Please provide proper input command.");
			}
		}

		// If the user wants to delete employee(s)
		else if (inputSplit[0].equalsIgnoreCase("DELETE")) {
			try {
				edaObj.deleteEmployee(inputSplit[1], inputSplit[2], inputSplit[3], inputSplit[4]);

				OutputArea.append("\n" + "Total employees in database = " + EmpCount + "\n");

			} catch (ArrayIndexOutOfBoundsException e) {

				OutputArea.append("\n" + "**Error while deleting.**");
				OutputArea.append("\n" + "Array Out of Bounds exception while reading the database.");
				OutputArea.append("Please provide proper input command.");
			}
		}
	}

	// Function to add a new employee with First and Last name
	public void addEmployee(String lastName, String firstName, int i) {

		Employee e = new Employee(lastName, firstName);
		employee[i] = e;

		printEmployee(i);
	}

	// Function to add an employee with three parameters (job gets set as
	// 'Unknown')
	public void addEmployee(String lastName, String firstName, String dept, int i) {

		Employee e = new Employee(lastName, firstName, dept);
		employee[i] = e;

		printEmployee(i);
	}

	// Function to add an employee with all parameters
	public void addEmployee(String lastName, String firstName, String dept, String jb, int i) {

		Employee e = new Employee(lastName, firstName, dept, jb);
		employee[i] = e;

		printEmployee(i);
	}

	// Modifies an employee
	public void modifyEmployee(String fieldValue, String matchValue, String replaceValue) {

		try {
			int modCount = 0;
			// If user wants to modify the first name
			if (fieldValue.equalsIgnoreCase("firstName")) {

				for (int k = 0; k < EmpCount; k++) {
					if (employee[k] != null && employee[k].getFirstName().equalsIgnoreCase(matchValue)) {
						employee[k].setFirstName(replaceValue);
						modCount++;
						printEmployee(k);
					}
				}
			}

			// If user wants to modify the last name
			else if (fieldValue.equalsIgnoreCase("lastName")) {

				for (int k = 0; k < EmpCount; k++) {
					if (employee[k] != null && employee[k].getLastName().equalsIgnoreCase(matchValue)) {
						employee[k].setLastName(replaceValue);
						modCount++;
						printEmployee(k);
					}
				}
			}

			// If user wants to modify the dep
			else if (fieldValue.equalsIgnoreCase("Department")) {

				for (int k = 0; k < EmpCount; k++) {
					if (employee[k] != null && employee[k].getDep().equalsIgnoreCase(matchValue)) {
						employee[k].setDep(replaceValue);
						modCount++;
						printEmployee(k);
					}
				}
			}

			// If user fieldValues '4' to modify the job
			else if (fieldValue.equalsIgnoreCase("job")) {

				for (int k = 0; k < EmpCount; k++) {
					if (employee[k] != null && employee[k].getJob().equalsIgnoreCase(matchValue)) {
						employee[k].setJob(replaceValue);
						modCount++;
						printEmployee(k);
					}
				}
			}
			if (modCount == 0)
				OutputArea.append("***No Records Found***");

		} catch (ArrayIndexOutOfBoundsException e) {

			OutputArea.append("\n" + "Array Out of Bounds exception. Please provide proper input.");
		}
	}

	// Function to Delete employee(s)
	public void deleteEmployee(String LN, String FN, String DP, String JB) {

		int deleteCount = 0;
		for (int i = 0; i < EmpCount; i++) // Loop over the non vacant array
											// length
		{
			if (employee[i] != null) {
				if ((LN.equalsIgnoreCase(employee[i].getLastName()) || LN.equals("*"))
						&& (FN.equalsIgnoreCase(employee[i].getFirstName()) || FN.equals("*"))
						&& (DP.equalsIgnoreCase(employee[i].getDep()) || DP.equals("*"))
						&& (JB.equalsIgnoreCase(employee[i].getJob()) || JB.equals("*"))) {
					if (EmpCount == 1) {
						employee[i] = null;
						deleteCount++;
						EmpCount--;
						break;
					} else {
						employee[i] = employee[EmpCount - 1];
						employee[EmpCount - 1] = null;
						deleteCount++;
						i--;
						EmpCount--;
					}
				}
			}
		}

		if (deleteCount == 0)
			OutputArea.append("\n" + "No employee found. None deleted.");

		if (deleteCount > 0)
			OutputArea.append("\n" + deleteCount + " employee(s) deleted");

	}

	// Finds the employee on passed values
	public void readEmployee(String fieldValue, String matchValue) {
		int recordFound = 0;

		// Change the Field to upper case for Switch Statement
		String UpperfieldValue = fieldValue.toUpperCase();

		matchValue = matchValue.replaceAll("\\*", "\\.\\*");

		OutputArea.append("\n" + "Search Results - " + "\n");

		// The loop to find the employee. Break when found and go back
		// to the main FOR Loop.
		for (int k = 0; k < EmpCount; k++) {
			if (employee[k] != null) {

				switch (UpperfieldValue) {

				case "firstName":
					if (employee[k].getFirstName() != null) {
						Pattern p = Pattern.compile(matchValue);

						// get a matcher object
						Matcher m = p.matcher(employee[k].getFirstName());

						if (m.matches()) {
							printEmployee(k);
							recordFound++;
						}
						break;
					}
				case "lastName":
					if (employee[k].getLastName() != null) {
						Pattern p = Pattern.compile(matchValue);
						
						// get a matcher object
						Matcher m = p.matcher(employee[k].getLastName());

						if (m.matches()) {
							printEmployee(k);
							recordFound++;
						}
						break;
					}
				case "DEPARTMENT":
					if (employee[k].getDep() != null) {
						Pattern p = Pattern.compile(matchValue);
						
						// get a matcher object
						Matcher m = p.matcher(employee[k].getDep());

						if (m.matches()) {
							printEmployee(k);
							recordFound++;
						}
						break;
					}
				case "job":
					if (employee[k].getJob() != null) {
						Pattern p = Pattern.compile(matchValue);
						
						// get a matcher object
						Matcher m = p.matcher(employee[k].getJob());

						if (m.matches()) {
							printEmployee(k);
							recordFound++;
						}
						break;
					}
				case "*":
					int i;
					for (i = 0; i < EmpCount; i++) // Loop over the non vacant
													// array length
					{
						if (employee[i] != null) {
							printEmployee(i);
						}
					}
					OutputArea.append("\n" + i + " employee record(s) found." + "\n");
					return;

				// Go to default if an unexpected Field Value is passed and
				// raise error
				default:
					OutputArea.append("**Invalid Field Value provided**" + "\n");
					return;
				}
			}
		}

		if (recordFound == 0) {
			OutputArea.append("***No Records Found***");
		} else
			OutputArea.append("\n" + recordFound + " employee record(s) found." + "\n");
	}

	/* function to print employee to the Text Area */
	public void printEmployee(int i) {

		OutputArea.append("\n" + employee[i].getLastName() + "\t" + employee[i].getFirstName() + "\t"
				+ employee[i].getDep() + "\t" + employee[i].getJob() + "\n");
	}

} // End of PophaliAmeyEDA2 class

class Employee {

	// Variables for employee
	private String lastName, firstName, dep, job;

	// Constructor with two parameters. Set "Unknown" if not passed
	public Employee(String ln, String fn) {
		setLastName(ln);
		setFirstName(fn);
		setDep("Unknown");
		setJob("Unknown");
	}

	// Constructor with three parameters
	public Employee(String ln, String fn, String dp) {
		setLastName(ln);
		setFirstName(fn);
		setDep(dp);
		setJob("Unknown");
	}

	// Constructor with all parameters
	public Employee(String ln, String fn, String dp, String jb) {
		setLastName(ln);
		setFirstName(fn);
		setDep(dp);
		setJob(jb);
	}

	/* Functions to return the employee data */
	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getJob() {
		return job;
	}

	public String getDep() {
		return dep;
	}

	/* Functions to set the employee data */
	public void setDep(String s) {
		dep = s;
	}

	public void setJob(String s) {
		job = s;
	}

	public void setLastName(String s) {
		lastName = s;
	}

	public void setFirstName(String s) {
		firstName = s;
	}

}