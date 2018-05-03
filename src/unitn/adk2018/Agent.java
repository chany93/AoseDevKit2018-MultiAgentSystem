package unitn.adk2018;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observer;

import unitn.adk2018.event.Event;
import unitn.adk2018.event.Message;
import unitn.adk2018.intention.Intention;
import unitn.adk2018.intention.ScheduledIntention;
import unitn.adk2018.intention.SchedulingQueue;
import unitn.adk2018.pddl.PddlWorld;

public abstract class Agent implements Runnable {
	
	public final boolean debugOn;
	protected final String name;
	protected final PddlWorld myBeliefs;
	
	protected final HashMap<Class<? extends Event>, List<Class<? extends Intention<? extends Event>>>> eventDictionary;
	
	public final MessageQueue mqueue;
	protected final HashSet<ScheduledIntention> currentIntentions;
	protected final SchedulingQueue waitingIntention;
	
	
	
	public Agent (String _name, boolean _debugOn) {
		debugOn = _debugOn;
		name = _name;
		myBeliefs = new PddlWorld();
		eventDictionary = new HashMap<Class<? extends Event>, List<Class<? extends Intention<? extends Event>>>> ();
		mqueue = new MessageQueue();
		waitingIntention = new SchedulingQueue (Environment.getSystemTimer());
		currentIntentions = new HashSet<ScheduledIntention>();
	}
	
	public long getAgentTime () {
		return waitingIntention.currentTime();
	}
	
	public void setPauseState ( boolean doPause ) {
		if (doPause)
			waitingIntention.pause();
		else
			waitingIntention.resume();
	}
	
	public boolean isPaused () {
		return waitingIntention.isPaused();
	}
	
	public String getName () {
		return name;
	}
	
	public PddlWorld getBeliefs() {
		return myBeliefs;
	}
	
	
	
	public <E extends Event> boolean addSupportedEvent ( Class<E> event, Class<? extends Intention<E>> handler ) {
		if( !eventDictionary.containsKey(event) )
			eventDictionary.put( event, new ArrayList<Class<? extends Intention<? extends Event>>>() );
		eventDictionary.get(event).add(handler);
		return true;
	}
	
	
	
	public boolean rescheduleIntention(ScheduledIntention si) {
//		GanttLogger.get().registerIntention(this, si);
		currentIntentions.add(si);
		return waitingIntention.offer(si);
	}
	public boolean removeFromScheduledIntentions(ScheduledIntention si) {
		currentIntentions.remove(si);
		return waitingIntention.remove(si);
	}

	
	public boolean rescheduleTimer(Observer o, long when) {
//		GanttLogger.get().registerIntention(this, si);
		return waitingIntention.offer (o, when);
	}
	public boolean removeFromScheduledTimers(Observer o) {
		return waitingIntention.remove(o);
	}

	
	
	public boolean pushMessage ( Message msg ) {
		if (msg==null) {
			System.err.println ( getName() + " null: pushMessage()" );
			return false;
		}
		
		if (msg.getTo().equals(getName())) {
			System.out.println( getName() + " " + msg + ": pushMessage() ");
			mqueue.add (msg);
			//GanttLogger.get().registerEvent(this, msg);
			return true;
		}
		System.err.println( getName() + " ERROR"
				+ " Received message " + msg.getClass().getSimpleName()
				+ " supposet to be destinated to " + msg.getTo()
			);
		return false;
	}
	
	public boolean pushGoal ( Event goal, MaintenanceCondition _asLongAs ) {
		if (goal==null) {
			System.err.println ( getName() + " null: pushGoal()" );
			return false;
		}
		System.out.println( getName() + " " + goal + ": pushGoal()" );
		GanttLogger.get().registerEvent(this, goal);
		return doBDImetaReasoning( goal, _asLongAs );
	}
	
	protected abstract <E extends Event> boolean doBDImetaReasoning ( E event, MaintenanceCondition _asLongAs );
	
	
	
	public void startInSeparateThread() {
		Thread t = new Thread (this );
		t.start();
	}
	
	public void printFullState() {
		System.err.println ("# " + getName() + "-objects: " + myBeliefs.pddlObjects());
		System.err.println ("# " + getName() + "-beliefs: " + myBeliefs.pddlClauses());
		System.err.println ("# " + getName() + "-currentIntentions (" + currentIntentions.size() + "):");
		for (ScheduledIntention s: currentIntentions) {
			System.err.println( "# " + getName() + "   " + s.intention);
		}
		System.err.println ("# " + getName() + "-waitingIntentions (" + waitingIntention.size() + "):");
		for (ScheduledIntention s: waitingIntention.getQueue()) {
			System.err.println( "# " + getName() + "   " + s.intention);
		}
		System.err.println ("# " + getName() + "-pendingMessages (" + mqueue.waitingMsgs() + "): ");
		for (int i = 0; i < mqueue.waitingMsgs(); i++) {
			System.err.println( "# " + getName() + "   " + mqueue.peekNth(i));
		}
	}
}
