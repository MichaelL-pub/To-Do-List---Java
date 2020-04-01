package todolist;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.Component;
import javax.swing.Box;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Insets;
import javax.swing.SwingConstants;

public class MainWindow {

	private JFrame frame;
	private JTable table;
	private JToolBar toolBar;
	private JButton saveBtn;
	private JPanel panel;
	TicketComponentsContainer ticketComps = new TicketComponentsContainer();
	String currentFile = "";
	ArrayList<Ticket> ticks;
	private JToolBar ticketToolbar;
	private JButton newTicketBtn;
	private JButton deleteTicketBtn;
	private JPanel panel1;
	private JButton newFileBtn;
	DefaultTableModel tableModel;
	
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
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBackground(Color.GRAY);
		frame.setBounds(100, 100, 900, 644);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		tableModel = new DefaultTableModel(0,1);
		table = new JTable(tableModel);
		populateFileList();
		table.setBackground(Color.GRAY);
		frame.getContentPane().add(table, BorderLayout.WEST);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		newFileBtn = new JButton("New File");
		toolBar.add(newFileBtn);
		
		saveBtn = new JButton("save");
		toolBar.add(saveBtn);
		
		panel = new JPanel();
		frame.getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		ticketToolbar = new JToolBar();
		ticketToolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
		ticketToolbar.setFloatable(false);
		panel.add(ticketToolbar);
		
		newTicketBtn = new JButton("New Ticket");
		ticketToolbar.add(newTicketBtn);
		
		deleteTicketBtn = new JButton("Delete Ticket");
		ticketToolbar.add(deleteTicketBtn);
		
		panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel.add(new JScrollPane(panel1));
		
		//item in file list clicked
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		    	panel1.removeAll();
		    	panel1.revalidate();
		    	panel1.repaint();
		    	
		    	String fileName = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
		    	ArrayList<Ticket> tickets = getTicketList(fileName);
		    	currentFile = fileName;
		    	ticks = tickets;
		    	
		    	for(int i = 0; i < tickets.size(); i++) {
		    		
		    		Ticket ticket = tickets.get(i);
		    		JLabel head = new JLabel(ticket.getHeader());
		    		JTextArea tail = new JTextArea(ticket.getDesc());

		    		panel1.add(head);
		    		panel1.add(tail);
		    		ticketComps.addHeader(head);
		    		ticketComps.addDescription(tail);
		    	}
		    	panel1.revalidate();
	    		panel1.repaint();
		    	
		    }
		});
		
		//save button click
		saveBtn.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile(currentFile);
			} 
		});
		
		//new ticket button click
		newTicketBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newTicket();
			}
			
		});
		
		//delete ticket button
		deleteTicketBtn.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteTicket();
			} 
		});
		
		//new file button
		newFileBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newFile();
				
			}
			
		});
	}
	
	//new file button
	private void newFile() {
		String workingDir = "./WorkingDirectory/";
		String fileName = JOptionPane.showInputDialog("Please enter a title for the new file.");
		if (fileName == null) {
			System.out.println("The user canceled");
		}
	else {
			System.out.println(fileName);
			
			try {
				File file = new File(workingDir + fileName + ".tdl");
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
	
	private void populateFileList() {
		tableModel.setRowCount(0);
		File[] fileList = getTDLFiles("./WorkingDirectory");
		for (File child : fileList) {
			if(child.getName().contains(".tdl"));{
		      tableModel.addRow(new Object[] {child.getName().substring(0, child.getName().length()-4)});
			}
		}
	}
	
	
	private void deleteTicket() {
		if(currentFile != "") {
			ArrayList<String> heads = new ArrayList<String>();
			for (int i = 0; i < panel1.getComponentCount(); i++) {
				if(i%2 == 0) {
					JLabel label = (JLabel)panel1.getComponent(i);
					heads.add(label.getText());
				}
			}
			if(heads.size() != 0) {
				JComboBox c1 = new JComboBox(heads.toArray());
				String s1 = "Select a ticket to delete.";
				String[] options = {"OK","Cancel"};
				int selected = JOptionPane.showOptionDialog(null, c1, s1, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if(selected == 0) {
					int ticketIndex = c1.getSelectedIndex();
					panel1.remove((ticketIndex*2)+1);
					panel1.remove((ticketIndex*2));
					panel1.revalidate();
		    		panel1.repaint();
				}
			}
		} else {
			System.out.println("No file selected.");
		}
	}
	
	private void newTicket() {
		if(currentFile != "") {
			String ticketName = JOptionPane.showInputDialog("Please enter a title for the ticket.");
			if (ticketName == null) {
				System.out.println("The user canceled");
			}
		else {
				System.out.println(ticketName);
				panel1.add(new JLabel(ticketName));
				panel1.add(new JTextArea());
				panel1.revalidate();
	    		panel1.repaint();
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
	
	
	private ArrayList<Ticket> getTicketList(String fileName) {
		try {
        	FileReader reader = new FileReader("./WorkingDirectory/" + fileName.toString() + ".tdl");
        	BufferedReader br = new BufferedReader(reader);
        	String line = "";
        	ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        	String header ="";
        	String description = "";
        	boolean isComp = false;
        	boolean bodyFlag = false;
        	StringBuilder body = new StringBuilder();
        	
        	while((line = br.readLine()) != null) {
        		if(line.contains("<head>")) {
        			header = line.substring(6);
        			System.out.println(header);
        		}
        		if(line.contains("<complete>")) {
        			if(line.charAt(10) == 'y') {
        				isComp = true;
        			}
        			System.out.println(line.charAt(10));
        		}
        		if(bodyFlag) {
        			if(line.contains("</body>")) {
        				body.append("\n" + line.substring(0, line.length()-7));
        				bodyFlag = false;
        				tickets.add(new Ticket(header,isComp,body.toString()));
        				header ="";
        	        	isComp = false;
        			}
        			else {
        				body.append("\n" + line);
        			}
        		}
        		if(line.contains("<body>")) {
        			if(line.contains("</body>")){
        				description = line.substring(6,line.length()-7);
        				System.out.println(description);
        				tickets.add(new Ticket(header,isComp,description));
        				header ="";
        	        	description = "";
        	        	isComp = false;
        	        	
        			} else {
        				bodyFlag = true;
        				body = new StringBuilder(line.substring(6));
        			}
        		}
        	}
        	br.close();
        	return tickets;
        } catch (Exception e) {
        	System.out.println("Error 404: File not found.");
        }
		return new ArrayList<Ticket>();
	}
	
	
	private void saveFile(String filename) {
		if(filename != "") {
		try {
			FileWriter fw = new FileWriter("./WorkingDirectory/" + filename + ".tdl",false);
			for (int i = 0; i < panel1.getComponentCount(); i++) {
				if(i%2 == 1) {
					JTextArea ta1 = (JTextArea) panel1.getComponent(i);
					fw.write("<body>" + ta1.getText() + "</body>");
				}
				else {
					JLabel label = (JLabel) panel1.getComponent(i);
					fw.write("\n<head>" + label.getText() + '\n');
					fw.write("<complete>n\n");
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
