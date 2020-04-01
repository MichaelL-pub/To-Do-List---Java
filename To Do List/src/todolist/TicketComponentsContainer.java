package todolist;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class TicketComponentsContainer {
	private ArrayList<JLabel> headers;
	private ArrayList<JTextArea> descriptions;

	public TicketComponentsContainer() {
		headers = new ArrayList<JLabel>();
		descriptions = new ArrayList<JTextArea>();
	}
	
	public TicketComponentsContainer(ArrayList<JLabel> heads, ArrayList<JTextArea> tails) {
		headers = heads;
		descriptions = tails;
	}
	
	public ArrayList<JLabel> getHeaders(){
		return headers;
	}
	
	public ArrayList<JTextArea> getDescriptions(){
		return descriptions;
	}
	
	public void setHeaders(ArrayList<JLabel> heads) {
		headers = heads;
	}
	
	public void setDescriptions(ArrayList<JTextArea> tails) {
		descriptions = tails;
	}
	
	public void addHeader(JLabel head) {
		headers.add(head);
	}
	
	public void addDescription(JTextArea tail) {
		descriptions.add(tail);
	}
}
