package unitn.adk2018.condition;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.MaintenanceCondition;

public class OrCondition extends MaintenanceCondition implements Observer {
	
	private MaintenanceCondition c1;
	private MaintenanceCondition c2;
	
	public OrCondition (MaintenanceCondition _c1, MaintenanceCondition _c2) {
		this.c1 = _c1;
		this.c2 = _c2;
		c1.addObserver(this);
		c2.addObserver(this);
	}
	
	@Override
	public boolean isTrue () {
		return c1.isTrue() || c2.isTrue();
	}
	
	@Override
	public void disable() {
		c1.deleteObserver(this);
		c2.deleteObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
		disable();
	}
	
}
