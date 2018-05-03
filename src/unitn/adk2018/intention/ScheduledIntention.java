package unitn.adk2018.intention;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.Agent;
import unitn.adk2018.GenericObservable;
import unitn.adk2018.MaintenanceCondition;
import unitn.adk2018.condition.TrueCondition;
import unitn.adk2018.event.Event;

public final class ScheduledIntention {
	
	private final ScheduledIntention self = this;
	
	private final Agent agent;
	private final Event event;
	public final Intention<Event> intention;
	private final Intention<Event>.IntentionInput input;
	private final MaintenanceCondition asLongAs;
	
	public final Observer asLongAsObserver;
	public final Observer waitForObserver;
	public final GenericObservable<ElaborationStatus> status;
	private Intention<Event>.Next next;
	
	private long whenToRun;
	
	
	
	@SuppressWarnings("unchecked")
	public ScheduledIntention(Agent _agent, Event _ev, Intention<? extends Event> _intention, MaintenanceCondition _asLongAs) {
		this.agent = _agent;
		this.event = _ev;
		this.intention = (Intention<Event>) _intention;
		this.input = intention.new IntentionInput(agent, event);
		this.asLongAs = ( _asLongAs==null ? new TrueCondition() : _asLongAs );
		this.next = intention.waitFor(intention::step0, 0);
		this.status = new GenericObservable<ElaborationStatus>(ElaborationStatus.CREATED);
		this.whenToRun = 0; /// ready to run now
	
		this.asLongAsObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				synchronized (self) {
					asLongAs.deleteObserver(asLongAsObserver);
					interrupt();
				}
			}
		};
		if(!asLongAs.isTrue())
			interrupt();
		this.asLongAs.addObserver(asLongAsObserver);
		
		this.waitForObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				synchronized (self) {
					if(next!=null && next.waitUntil!=null)
						next.waitUntil.deleteObserver(waitForObserver);
					delay(0);
					agent.rescheduleIntention(self);
				}
			}
		};
	}
	
	
	
	public final void execute() {
		synchronized (this) {
			if (agent.debugOn)
				System.out.println( agent.getName() + " " + intention + ": run() " );
//						+ ", step: " + (next.step!=null?next.step.getClass().getSimpleName():"null") );

			if (!asLongAs.isTrue()) {
				System.err.println( agent.getName() + " " + intention + ": asLongAs is false");
				interrupt();
				return;
			}
			
			try {
				if (intention==null)
					throw new Exception( agent.getName() + this + ": intention is null" );
				
				if (next==null)
					throw new Exception( agent.getName() + " " + intention + ": next is null" );
				
				if (status.get() == ElaborationStatus.HANDLED_WITH_SUCCESS || status.get() == ElaborationStatus.HANDLED_WITH_FAILURE)
					throw new Exception( agent.getName() + " " + intention + ": already terminated");
				
				if (!asLongAs.isTrue())
					throw new Exception( agent.getName() + " " + intention + ": asLongAs is false");
			} catch (Exception e) {
				System.err.println( agent.getName() + " " + this + " exception:" );
				e.printStackTrace();
			}
			
			if (status.get() == ElaborationStatus.CREATED) {
				if (agent.debugOn) System.out.println( agent.getName() + " " + intention + " STARTED ");
				status.set(ElaborationStatus.STARTED);
//				delay(0);
//				agent.rescheduleIntention(self);
//				return;
			}
			
			if (status.get() == ElaborationStatus.STARTED) {
				
				if (next.step == null) {
					interrupt();
					return;
				}
				
				next = next.step.apply(input);
				
				if (next == null) {
					if (agent.debugOn) System.out.println( agent.getName() + " " + intention + " TERMINATED_WITH_SUCCESS ");
					this.status.set( ElaborationStatus.HANDLED_WITH_SUCCESS );
					agent.removeFromScheduledIntentions(self);
					intention.pass(input);
					return;
				}
				
				if (next.waitFor!=null) {
					if (agent.debugOn) System.out.println( agent.getName() + " " + intention + " WAIT_FOR " + next.waitFor);
					delay(next.waitFor);
					agent.rescheduleIntention(self);
					return;
				}
				
				else if (next.waitUntil!=null) {
					if (agent.debugOn) System.out.println( agent.getName() + " " + intention + " WAIT_UNTIL " + next.waitUntil.getClass().getSimpleName());
					next.waitUntil.addObserver(waitForObserver);
					if(next.waitUntil.isTrue())
						waitForObserver.update(next.waitUntil, null);
				}
				
				else {
					delay(0);
					agent.rescheduleIntention(self);
				}
				
			}
		}
	}
	
	public final void interrupt () {
		synchronized (this) {
			if (status.get() == ElaborationStatus.CREATED || status.get() == ElaborationStatus.STARTED) {

				if (agent.debugOn) System.out.println( agent.getName() + " " +  intention + " TERMINATED_WITH_FAILURE " );
				status.set(ElaborationStatus.HANDLED_WITH_FAILURE);
				agent.removeFromScheduledIntentions(self);
				
				if (next!=null && next.waitUntil!=null)
					next.waitUntil.deleteObserver(waitForObserver);
				asLongAs.deleteObserver(asLongAsObserver);
				
				intention.fail(input);
			}
		}
	}
	
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	public void delay (long msec) {
		whenToRun = msec;
	}
	
	public void tickExpired (int tickSizeMsecs) {
		whenToRun -= tickSizeMsecs;
	}
	
	public boolean isExpired () {
		return whenToRun <= 0;
	}
	
	public long whenWillExpire () {
		return whenToRun;
	}
}
