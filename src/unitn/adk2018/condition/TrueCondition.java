package unitn.adk2018.condition;

import unitn.adk2018.MaintenanceCondition;

public class TrueCondition extends MaintenanceCondition {

	public TrueCondition () {
	}

	@Override
	public boolean isTrue () {
		return true;
	}
	
	@Override
	public void disable() {
	}
	
}
