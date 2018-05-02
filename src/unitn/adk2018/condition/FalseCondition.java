package unitn.adk2018.condition;

import unitn.adk2018.MaintenanceCondition;

public class FalseCondition extends MaintenanceCondition {

	public FalseCondition () {
	}

	@Override
	public boolean isTrue () {
		return false;
	}
	
	@Override
	public void disable() {
	}
	
}
