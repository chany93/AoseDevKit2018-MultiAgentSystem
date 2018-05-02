package unitn.adk2018.condition;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.GenericObservable;
import unitn.adk2018.MaintenanceCondition;
import unitn.adk2018.intention.ElaborationStatus;

public class HandledWithSuccessCondition extends MaintenanceCondition implements Observer {

	private GenericObservable<ElaborationStatus> status;
	
	public HandledWithSuccessCondition (GenericObservable<ElaborationStatus> status) {
		this.status = status;
		status.addObserver(this);
	}

	@Override
	public boolean isTrue () {
		return (status.get().isHandledWithSuccess());
	}
	
	@Override
	public void disable() {
		status.deleteObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(status.get().isHandledWithSuccess()) {
			setChanged();
			notifyObservers();
			disable();
		}
	}
	
}
