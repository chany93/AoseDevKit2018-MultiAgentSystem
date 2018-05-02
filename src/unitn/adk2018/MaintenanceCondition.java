package unitn.adk2018;

import java.util.Observable;

public abstract class MaintenanceCondition extends Observable {

	/*
	 * Override to check a real condition.
	 * The observer is called only on transition from false to true (thus never by default).
	 */
	public abstract boolean isTrue();
	
	/*
	 * To be called when no longer used to remove observers and such.
	 */
	public abstract void disable ();

}
