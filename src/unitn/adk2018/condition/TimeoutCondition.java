package unitn.adk2018.condition;

import unitn.adk2018.Environment;
import unitn.adk2018.MaintenanceCondition;
import unitn.adk2018.intention.SchedulingQueue;

import java.util.Observable;
import java.util.Observer;

public class TimeoutCondition extends MaintenanceCondition implements Observer {

	TimeoutCondition parent = this;
	long expirationTime;
	boolean disabled = false;
	
	public TimeoutCondition (SchedulingQueue sq, int msec) {
		expirationTime = Environment.getSimulationTime() + msec;
		sq.offer(this, msec);
	}

	@Override
	public void update(Observable o, Object arg) {
			if (! disabled) {
				setChanged();
				notifyObservers();
				disable();
			}
	}

	@Override
	public boolean isTrue () {
		return (Environment.getSimulationTime() < expirationTime);
	}
	
	@Override
	public void disable() {
		disabled = true;
	}
	
}
