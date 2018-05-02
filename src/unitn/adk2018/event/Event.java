package unitn.adk2018.event;

import unitn.adk2018.GenericObservable;
import unitn.adk2018.MaintenanceCondition;
import unitn.adk2018.condition.HandledCondition;
import unitn.adk2018.condition.HandledWithFailureCondition;
import unitn.adk2018.condition.HandledWithSuccessCondition;
import unitn.adk2018.condition.NotHandledCondition;
import unitn.adk2018.intention.ElaborationStatus;

public class Event {
	
	public final GenericObservable<ElaborationStatus> status = new GenericObservable<ElaborationStatus>(ElaborationStatus.CREATED);

	public MaintenanceCondition wasNotHandled() {
		return new NotHandledCondition(status);
	}
	
	public MaintenanceCondition wasHandled() {
		return new HandledCondition(status);
	}
	
	public MaintenanceCondition wasHandledWithFailure() {
		return new HandledWithFailureCondition(status);
	}
	
	public MaintenanceCondition wasHandledWithSuccess() {
		return new HandledWithSuccessCondition(status);
	}
	
	static private int idCounter = 0;
	private String id = null;
	
	public String getId() {
		if ( id == null )
			id = "" + idCounter++;
		return id;
	}
	
	public String toString() {
		return getId() + "_" + this.getClass().getSimpleName();
	}
	
}
