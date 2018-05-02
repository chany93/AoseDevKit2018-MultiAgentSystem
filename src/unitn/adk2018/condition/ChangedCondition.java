package unitn.adk2018.condition;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.MaintenanceCondition;

public class ChangedCondition extends MaintenanceCondition implements Observer {

	private Observable observable;
	
	public ChangedCondition (Observable observable) {
		this.observable = observable;
		observable.addObserver(this);
	}

	@Override
	public boolean isTrue () {
		return observable.hasChanged();
	}
	
	@Override
	public void disable() {
		observable.deleteObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
		disable();
	}
	
}
