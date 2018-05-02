package unitn.adk2018.event;

public abstract class Message extends Event {
	
	private final String from;
	private final String to;
	
	
	
	public Message (String _from, String _to) {
		from = _from;
		to = _to;
	}
	
	
	
	public String toString() {
		return super.toString() + "(" + from + "->" + to + ")";
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public abstract String getTitle();
	
}
