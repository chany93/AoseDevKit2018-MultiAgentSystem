package unitn.adk2018.event;

public abstract class InformMessage extends Message {
	
	public InformMessage(String _from, String _to) {
		super(_from, _to);
	}
	
	@Override
	public String getTitle() {
		return "INFORM";
	}
	
}
