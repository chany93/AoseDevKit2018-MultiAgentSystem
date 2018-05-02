package unitn.adk2018.intention;

import java.util.function.Function;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.MaintenanceCondition;
import unitn.adk2018.event.Event;
import unitn.adk2018.event.Message;

public abstract class Intention<E extends Event> {
	
	/**
	 * Set nextstep null to terminate the intention with failure
	 * @author Marco
	 *
	 */
	public final class Next {
		public final Function<IntentionInput, Next> step;
		public final Long waitFor;
		public final MaintenanceCondition waitUntil;
		
		public Next(Function<IntentionInput, Next> nextstep, Long waitFor, MaintenanceCondition waitUntil) {
			this.step = nextstep;
			this.waitFor = waitFor;
			this.waitUntil = waitUntil;
		}
	}
	
	public final class IntentionInput {
		public final Agent agent;
		public final E event;
		
		public IntentionInput(Agent agent, E ev) {
			this.agent = agent;
			this.event = ev;
		}
	}
	
	
	
	public final Next waitFor(final Function<IntentionInput, Next> nextstep, final long waitingTime) {
		return new Next(nextstep, waitingTime, null);
	}
	
	public final Next waitUntil(final Function<IntentionInput, Next> nextstep, final MaintenanceCondition maintenanceCondition) {
		return new Next(nextstep, null, maintenanceCondition);
	}
	
	public final boolean sendMessage (Message msg) {
		return Environment.getEnvironment().sendMessage( msg );
	}
	
	public Agent agent;
	
	public E event;
	
	
	/**
	 * Override to define context conditions
	 * @param a
	 * @param g
	 * @return
	 */
	public boolean context(Agent a, E e) {
		return true;
	}
	
	public abstract Next step0(IntentionInput in);
	
	/**
	 * Override to do something after the intention has terminated with success
	 * @param in
	 */
	public void pass(IntentionInput in) {
	}
	
	/**
	 * Override to do something after the intention has terminated with failure
	 * @param in
	 */
	public void fail(IntentionInput in) {
	}
	
	public String toString() {
		return event.getId() + "_" + this.getClass().getSimpleName();
	}
	
}
