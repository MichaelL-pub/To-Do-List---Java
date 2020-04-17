/*
 * @Michael Leszczynski
 * To Do List program that allows you to generate tickets for jobs than need to be done and mark them complete once they are finished
 * Uses Java Swing as a GUI
 */
package todolist;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainWindow {

	private JFrame frame;
	
	DefaultTableModel tableModel;
	
	private JTabbedPane tabbedPane;
	
	private JPanel panel;
	private JPanel incompletePanel;
	private JPanel completePanel;
	
	private String workingDirectory;
	private String currentFile = "";
	
	private JLabel fileNameLabel;
	
	private JTable table;
	
	private JToolBar toolBar;
	private JToolBar ticketToolbar;
	
	private JButton saveBtn;
	private JButton newTicketBtn;
	private JButton deleteTicketBtn;
	private JButton newFileBtn;
	private JButton deleteBtn;
	private JButton completeTicketBtn;
	private JButton changeDirBtn;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//initialize primary frame
		frame = new JFrame("To Do List");
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBackground(Color.GRAY);
		frame.setBounds(100, 100, 900, 644);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		//initialize table to list tdl files
		tableModel = new DefaultTableModel(0,1) {

			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells are uneditable
		       return false;
		    }
		};
		
		table = new JTable(tableModel);
		setDirectory();
		populateFileList();
		table.setBackground(Color.GRAY);
		frame.getContentPane().add(table, BorderLayout.WEST);
		
		//toolbar for file/directory actions
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		//new file button init
		ImageIcon newFileIcon = new ImageIcon("./Rescources/Images/newpage.png");
		newFileBtn = new JButton();
		newFileBtn.setToolTipText("New File");
		newFileBtn.setIcon(newFileIcon);
		toolBar.add(newFileBtn);
		
		//delete file button
		ImageIcon deleteFileIcon = new ImageIcon("./Rescources/Images/deletepage.png");
		deleteBtn = new JButton();
		deleteBtn.setToolTipText("Delete File");
		deleteBtn.setIcon(deleteFileIcon);
		toolBar.add(deleteBtn);
		
		//save current file button
		ImageIcon saveIcon = new ImageIcon("./Rescources/Images/save.png");
		saveBtn = new JButton();
		saveBtn.setToolTipText("Save Current File");
		saveBtn.setIcon(saveIcon);
		toolBar.add(saveBtn);
		
		//change working directory button
		ImageIcon changeDirIcon = new ImageIcon("./Rescources/Images/directoryChange.jpg");
		changeDirBtn = new JButton();
		changeDirBtn.setToolTipText("Change File Directory");
		changeDirBtn.setIcon(changeDirIcon);
		toolBar.add(changeDirBtn);
		
		//panel contains ticket buttons, complete/incomplete panels and the individual tickets
		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		//toolbar to contain ticket buttons
		ticketToolbar = new JToolBar();
		ticketToolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
		ticketToolbar.setFloatable(false);
		
		//panel to contain ticket toolbar, needed for alignment issues
		JPanel ticketBarPanel = new JPanel();
		ticketBarPanel.add(ticketToolbar);
		ticketBarPanel.setLayout(new BoxLayout(ticketBarPanel, BoxLayout.X_AXIS));
		panel.add(ticketBarPanel);
		
		//button for ticket creation
		ImageIcon newTicketIcon = new ImageIcon("./Rescources/Images/newTicket.png");
		newTicketBtn = new JButton();
		newTicketBtn.setToolTipText("New Ticket");
		newTicketBtn.setIcon(newTicketIcon);
		ticketToolbar.add(newTicketBtn);
		
		//button for ticket deletion
		ImageIcon deleteTicketIcon = new ImageIcon("./Rescources/Images/deleteticket.png");
		deleteTicketBtn = new JButton();
		deleteTicketBtn.setToolTipText("Delete Ticket");
		deleteTicketBtn.setIcon(deleteTicketIcon);
		ticketToolbar.add(deleteTicketBtn);
		
		ImageIcon completeTicketIcon = new ImageIcon("./Rescources/Images/completeticket.png");
		completeTicketBtn = new JButton();
		completeTicketBtn.setToolTipText("Complete Ticket");
		completeTicketBtn.setIcon(completeTicketIcon);
		ticketToolbar.add(completeTicketBtn);
		
		//set sizes to resolve format issues
		ticketToolbar.setMinimumSize(new Dimension(100,60));
		ticketBarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,500));

		//label to display the current file name
		fileNameLabel = new JLabel("Current File:");
		fileNameLabel.setToolTipText("Current File Name");
		fileNameLabel.setPreferredSize(new Dimension(800,20));
		fileNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		fileNameLabel.setMinimumSize(new Dimension(800,20));
		panel.add(fileNameLabel);
		
		//panels for complete/incomplete tickets respectively
		incompletePanel = new JPanel();
		completePanel = new JPanel();
		incompletePanel.setLayout(new BoxLayout(incompletePanel, BoxLayout.Y_AXIS));
		completePanel.setLayout(new BoxLayout(completePanel, BoxLayout.Y_AXIS));
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Incomplete", new JScrollPane(incompletePanel));
		tabbedPane.addTab("Complete", new JScrollPane(completePanel));
		panel.add(tabbedPane);
		incompletePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		completePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//change directory button click
		changeDirBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setDirectory();
				populateFileList();
			}
			
		});
		
		
		//item in file list clicked
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		    	tableClick();
		    }
		});
		
		//complete ticket button click
		completeTicketBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				completeTicket();
				
			}
			
		});
		
		//save button click
		saveBtn.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				saveFile(currentFile);
			} 
		});
		
		//new ticket button click
		newTicketBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				newTicket();
			}
			
		});
		
		//delete ticket button
		deleteTicketBtn.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				deleteTicket();
			} 
		});
		
		//new file button
		newFileBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				newFile();
				
			}
			
		});
		
		//delete file button
		deleteBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				deleteFile();
				
			}
			
		});
	}
	
	//Method for user to choose a working directory
	private void setDirectory() {
		JFileChooser workingDir = new JFileChooser();
		workingDir.setCurrentDirectory(new File(System.getProperty("user.home")));
		workingDir.setDialogTitle("Choose a working directory or use the default one");
		workingDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = workingDir.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {//if a non default working directory is chosen
		    File selectedDir = workingDir.getSelectedFile();
		    System.out.println(selectedDir.getPath());
		    workingDirectory = selectedDir.getPath();
		}
		else {//set to default directory
			System.out.println("Default Directory");
			workingDirectory = "./WorkingDirectory";
		}
	}
	
	//method for when table is clicked
	private void tableClick() {
		if(table.getSelectedRow() >= 0) {//if a populated cell is clicked
			//clear and redraw ticket panels for next clicked file
			incompletePanel.removeAll();
			incompletePanel.revalidate();
			incompletePanel.repaint();
			completePanel.removeAll();
			completePanel.revalidate();
			completePanel.repaint();
    	
			//get string from selected table cell and get ticket list from that file
    		String fileName = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
    		ArrayList<Ticket> tickets = getTicketList(fileName);
    		
    		populateFileList();// for reseting selected cell
    		//update current file variable and label
    		currentFile = fileName;
    		fileNameLabel.setText("Current File: " + fileName + ".tdl");
    	
    		//loop through tickets and add to their respective panels
    		for(int i = 0; i < tickets.size(); i++) {
    		
    			Ticket ticket = tickets.get(i);
    			JLabel head = new JLabel(ticket.getHeader());
    			JTextArea tail = new JTextArea(ticket.getDesc());
    			JScrollPane div = new JScrollPane(tail);
    			//format ticket header and bodies
    			div.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
    			tail.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
    			div.setPreferredSize(new Dimension(800,100));
    			head.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
    			
    			if(ticket.getIsComplete()) {//if ticket is completed
    				completePanel.add(head);
    				completePanel.add(div);
    			}
    			else {
    				incompletePanel.add(head);
    				incompletePanel.add(div);
    			}
    		}
    	}
	}
	
	//complete ticket button method, moves ticket from incomplete to complete panel
	private void completeTicket() {
		if(currentFile != "") {//if a valid cell in table has been clicked
			ArrayList<String> heads = new ArrayList<String>();
			//loop through incomplete panel components
			for (int i = 0; i < incompletePanel.getComponentCount(); i++) {
				if(i%2 == 0) {//if its a ticket name label add to heads
					JLabel label = (JLabel)incompletePanel.getComponent(i);
					heads.add(label.getText());
				}
			}
			if(heads.size() != 0) {//if at at least 1 incomplete ticket
				//components for dialog with combo box
				JComboBox c1 = new JComboBox(heads.toArray());
				String s1 = "Select a ticket to complete.";
				String[] options = {"OK","Cancel"};
				//show dialog with incomplete ticket names
				int selected = JOptionPane.showOptionDialog(null, c1, s1, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if(selected == 0) {//if ok is clicked
					//remove selected ticket from incomplete panel and move to complete panel
					int ticketIndex = c1.getSelectedIndex();
					JLabel head = (JLabel)incompletePanel.getComponent(ticketIndex*2);
					JTextArea tail = (JTextArea) (((JViewport) (((JScrollPane) incompletePanel.getComponent(ticketIndex*2+1)).getViewport()))).getView();
					JScrollPane div = new JScrollPane(tail);
		    		div.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
		    		div.setPreferredSize(new Dimension(800,100));
		    		tail.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
		    		head.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
					
					incompletePanel.remove((ticketIndex*2)+1);
					incompletePanel.remove((ticketIndex*2));
					completePanel.add(head);
					completePanel.add(div);
					incompletePanel.revalidate();
					incompletePanel.repaint();
				}
			}
		} else {//table has not been clicked
			System.out.println("No file selected.");
		}
	}
	
	//delete file method, deletes selected file in working dir
	private void deleteFile() {
		//get files from working directory and add to array if they are tdl extension
		File[] fileList = getTDLFiles(workingDirectory);
		ArrayList<String> fileNames = new ArrayList<String>();
		for(int i = 0; i < fileList.length; i++) {
			if(fileList[i].getName().contains(".tdl")) {
				fileNames.add(fileList[i].getName());
			}
		}
		//display list of files in a dialog via combobox
		JComboBox c1 = new JComboBox(fileNames.toArray());
		String s1 = "Select a file to delete.";
		String[] options = {"OK","Cancel"};
		int selected = JOptionPane.showOptionDialog(null, c1, s1, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if(selected == 0) {//if ok is clicked delete the file
			String fileSelected = c1.getSelectedItem().toString();
			File fileToDelete = new File(workingDirectory + "\\" +fileSelected);
			if(fileToDelete.delete()) {//if file delete success
				System.out.println("File deleted successfully.");
			}
			else {
				System.out.println("File deletion failure.");
			}
			populateFileList();
		}
		
	}
	
	//new file method, creates new file in working dir
	private void newFile() {
		//get text input from user for file name
		String fileName = JOptionPane.showInputDialog("Please enter a title for the new file.");
		if (fileName == null) {
			System.out.println("The user canceled");
		}
		else {//string has been entered
			System.out.println(fileName);
			
			try {//create file
				File file = new File(workingDirectory + "\\" + fileName + ".tdl");
				if(file.createNewFile()) {
					System.out.println("File created successfully.");
					populateFileList();
				}
				else {
					System.out.println("Error Creating file");
				}
			} catch (IOException e) {
				System.out.println("Error creating file.");
				e.printStackTrace();
			}
		}
	}
	
	//resets and reloads file list
	private void populateFileList() {
		//reset table, get files from directory then loop through and add to table
		tableModel.setRowCount(0);
		File[] fileList = getTDLFiles(workingDirectory);
		for (File child : fileList) {
			if(child.getName().contains(".tdl"));{
		      tableModel.addRow(new Object[] {child.getName().substring(0, child.getName().length()-4)});
			}
		}
	}
	
	//delete Ticket method
	private void deleteTicket() {
		if(currentFile != "") {//if an initial file has been selected
			//boolean compTicket = false;
			//create ticket names list from comp/incomplete panels
			ArrayList<String> heads = new ArrayList<String>();
			for (int i = 0; i < incompletePanel.getComponentCount(); i++) {
				if(i%2 == 0) {
					JLabel label = (JLabel)incompletePanel.getComponent(i);
					heads.add(label.getText());
				}
			}
			for (int i = 0; i < completePanel.getComponentCount(); i++) {
				if(i%2 == 0) {
					JLabel label = (JLabel)completePanel.getComponent(i);
					heads.add(label.getText());
				}
			}
			if(heads.size() != 0) {//if at least 1 ticket
				//show dialog with list of tickets for user choice
				JComboBox c1 = new JComboBox(heads.toArray());
				String s1 = "Select a ticket to delete.";
				String[] options = {"OK","Cancel"};
				int selected = JOptionPane.showOptionDialog(null, c1, s1, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if(selected == 0) {//if ok is clicked
					int ticketIndex = c1.getSelectedIndex();
					if(ticketIndex >= incompletePanel.getComponentCount()/2) {//if index is in complete panel
						ticketIndex = ticketIndex - (incompletePanel.getComponentCount()/2);
						completePanel.remove((ticketIndex*2)+1);
						completePanel.remove((ticketIndex*2));
						completePanel.revalidate();
						completePanel.repaint();
					}
					else {//index in incomplete panel
						incompletePanel.remove((ticketIndex*2)+1);
						incompletePanel.remove((ticketIndex*2));
						incompletePanel.revalidate();
						incompletePanel.repaint();
					}
				}
			}
		} else {
			System.out.println("No file selected.");
		}
	}
	
	//new ticket method, add ticket of given name in incomplete panel
	private void newTicket() {
		if(currentFile != "") {//if initial file is selected
			String ticketName = JOptionPane.showInputDialog("Please enter a title for the ticket.");
			if (ticketName == null) {//if user did not input something
				System.out.println("The user canceled");
			}
			else {//add ticket components to incomplete panel and update it
				JLabel head = new JLabel(ticketName);
	    		JTextArea tail = new JTextArea();
	    		JScrollPane div = new JScrollPane(tail);
	    		div.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
	    		tail.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
	    		div.setPreferredSize(new Dimension(800,100));
	    		head.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
	    		
	    		incompletePanel.add(head);
				incompletePanel.add(div);
				
				incompletePanel.revalidate();
				incompletePanel.repaint();
			}
		}
	}
	
	private File[] getTDLFiles(String directory) {
		File dir = new File(directory);
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    return directoryListing;
		  } else {
		    System.out.println("error opening directory!");
		    return new File[] {};
		  }
	}
	
	//will create a list of tickets from a given file
	private ArrayList<Ticket> getTicketList(String fileName) {
		try {
			//System.out.println(workingDirectory.toString() + fileName.toString() + ".tdl");
        	FileReader reader = new FileReader(workingDirectory + "\\" +fileName.toString() + ".tdl");
        	//System.out.println(workingDirectory.toString() + fileName.toString() + ".tdl");
        	BufferedReader br = new BufferedReader(reader);
        	String line = "";
        	ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        	String header ="";
        	String description = "";
        	boolean isComp = false;
        	boolean bodyFlag = false;
        	StringBuilder body = new StringBuilder();
        	
        	while((line = br.readLine()) != null) {//while there are unread lines
        		if(line.contains("<head>")) {//if line contains a head tag
        			header = line.substring(6);
        			System.out.println(header);
        		}
        		if(line.contains("<complete>")) {//if line contains a complete tag
        			if(line.charAt(10) == 'y') {
        				isComp = true;
        			}
        			System.out.println(line.charAt(10));
        		}
        		if(bodyFlag) {//if body is more than one line
        			if(line.contains("</body>")) {//if end of body
        				//add to line excluding end body tag
        				body.append("\n" + line.substring(0, line.length()-7));
        				bodyFlag = false;
        				tickets.add(new Ticket(header,isComp,body.toString()));
        				header ="";
        	        	isComp = false;
        			}
        			else {//not end of body, add to line
        				body.append("\n" + line);
        			}
        		}
        		if(line.contains("<body>")) {//if line contains open body tag
        			if(line.contains("</body>")){//if body ends on same line
        				//add to description and add ticket to list
        				description = line.substring(6,line.length()-7);
        				System.out.println(description);
        				tickets.add(new Ticket(header,isComp,description));
        				header ="";
        	        	description = "";
        	        	isComp = false;
        			} else {//body doesn't end on same line
        				//set body flag true
        				bodyFlag = true;
        				body = new StringBuilder(line.substring(6));
        			}
        		}
        	}
        	br.close();
        	return tickets;
        } catch (Exception e) {
        	System.out.println("Error 404: File not found.");
        	//e.printStackTrace();
        }
		return new ArrayList<Ticket>();
	}
	
	//method to save currently selected file
	private void saveFile(String filename) {
		if(filename != "") {//if an initial file has been chosen
		try {
			FileWriter fw = new FileWriter(workingDirectory + "\\" + filename + ".tdl",false);
			for (int i = 0; i < incompletePanel.getComponentCount(); i++) {//loop through incomplete panel components
				if(i%2 == 1) {//if component is a text area
					JTextArea ta1 = (JTextArea) (((JViewport) (((JScrollPane) incompletePanel.getComponent(i)).getViewport()))).getView();
					fw.write("<body>" + ta1.getText() + "</body>");
				}
				else {//component is a ticket name
					JLabel label = (JLabel) incompletePanel.getComponent(i);
					fw.write("\n<head>" + label.getText() + '\n');
					fw.write("<complete>n\n");
				}
			}
			
			//do the same loop for complete panel
			for (int i = 0; i < completePanel.getComponentCount(); i++) {
				if(i%2 == 1) {
					JTextArea ta1 = (JTextArea) (((JViewport) (((JScrollPane) completePanel.getComponent(i)).getViewport()))).getView();
					fw.write("<body>" + ta1.getText() + "</body>");
				}
				else {
					JLabel label = (JLabel) completePanel.getComponent(i);
					fw.write("\n<head>" + label.getText() + '\n');
					fw.write("<complete>y\n");
				}
			}
			
			fw.close();
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		}
		else {
			System.out.println("NO FILE SELECTED");
		}
	}
}
