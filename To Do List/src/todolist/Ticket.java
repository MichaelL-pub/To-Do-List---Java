package todolist;

public class Ticket {
	private String header;
	private boolean isComplete;
	private String desc;

	public Ticket() {
		header = "Default Ticket";
		isComplete = false;
		desc = "Default Ticket Description";
	}
	
	public Ticket(String head, boolean isComp, String description) {
		header = head;
		isComplete = isComp;
		desc = description;
	}
	
	public void setHeader(String head) {
		header = head;
	}
	
	public void setIsComplete(boolean complete) {
		isComplete = complete;
	}
	
	public void setDesc(String description) {
		desc = description;
	}
	
	public String getHeader() {
		return header;
	}
	
	public boolean getIsComplete() {
		return isComplete;
	}
	
	public String getDesc() {
		return desc;
	}
}
