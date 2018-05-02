package unitn.adk2018.intention;

public class ElaborationStatus {
	
	public static final ElaborationStatus CREATED = new ElaborationStatus("CREATED");
	
	public static final ElaborationStatus STARTED = new ElaborationStatus("STARTED");
	
	public static final ElaborationStatus HANDLED_WITH_SUCCESS = new ElaborationStatus("HANDLED_WITH_SUCCESS");
	
	public static final ElaborationStatus HANDLED_WITH_FAILURE = new ElaborationStatus("HANDLED_WITH_FAILURE");
	
	
	
	public String string;
	
	public ElaborationStatus(String _string) {
		this.string = _string;
	}
	
	
	
	public boolean isCreated() {
		return (this == CREATED);
	}
	
	public boolean isStarted() {
		return (this == STARTED);
	}
	
	public boolean isNotHandled() {
		return (this == CREATED || this == STARTED);
	}
	
	public boolean isHandled() {
		return (this == HANDLED_WITH_SUCCESS || this == HANDLED_WITH_FAILURE);
	}
	
	public boolean isHandledWithSuccess() {
		return (this == HANDLED_WITH_SUCCESS);
	}
	
	public boolean isHandledWithFailure() {
		return (this == HANDLED_WITH_FAILURE);
	}
	
	@Override
	public String toString() {
		return string;
	}
	
}
