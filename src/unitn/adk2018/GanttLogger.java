package unitn.adk2018;

import java.util.*;

import unitn.adk2018.event.Event;
import unitn.adk2018.intention.ElaborationStatus;
import unitn.adk2018.intention.ScheduledIntention;

public class GanttLogger extends Observable {
	
	public static class LoggedTask {
		public final Agent agent;
		public final String taskName;
		public final GenericObservable<ElaborationStatus> status;
		public final long creationTime;
		public long startTime;
		public long terminationTime;
		public LoggedTask(Agent _agent, String _taskName, GenericObservable<ElaborationStatus> _status) {
			this.agent = _agent;
			this.taskName = _taskName;
			this.status = _status;
			this.creationTime = Environment.getSimulationTime();
		}
	}
	
	
	
	private static GanttLogger theInstance;
	
	public static GanttLogger get () {
		if(theInstance==null)
			theInstance = new GanttLogger();
		return theInstance;
	}
	
	
	
	public final Map<GenericObservable<ElaborationStatus>, LoggedTask> ganttTasks;
	public final List<LoggedTask> ganttTaskList;
	
	
	
	private GanttLogger () {
		ganttTasks = new HashMap<GenericObservable<ElaborationStatus>, LoggedTask>();
		ganttTaskList = new ArrayList<LoggedTask>();
	}
	
	
	
	public synchronized Object[] getACopyOfTasks() {
		return ganttTaskList.toArray();
	}
	
	private int j = 0;
	
	public void registerEvent(Agent agent, Event ev) {
		synchronized (this) {
			if(ganttTasks.containsKey(ev.status))
				return;
			monitor(agent, ev.toString(), ev.status);
			
		}
	}
	
	public void registerIntention(Agent agent, ScheduledIntention i) {
		synchronized (this) {
			if(ganttTasks.containsKey(i.status))
				return;
			monitor(agent, i.intention.toString(), i.status);
		}
	}
	
	public synchronized void monitor(Agent agent, String name, GenericObservable<ElaborationStatus> status) {
		LoggedTask ganttTask = new LoggedTask(agent, j++ + " " + name, status);
		
		if(status.get().isHandled())
			ganttTask.terminationTime = Environment.getSimulationTime();
		
		ganttTasks.put(status, ganttTask);
		ganttTaskList.add(ganttTask);
		
		status.addObserver( new Observer() {
			@Override
			public synchronized void update(Observable o, Object arg) {
				synchronized (this) {
					if(status.get().isStarted())
						ganttTask.startTime = Environment.getSimulationTime();
					if(status.get().isHandled())
						ganttTask.terminationTime = Environment.getSimulationTime();
					setChanged();
					notifyObservers(ganttTask);
				}
			}
		});
		
		setChanged();
		notifyObservers(ganttTask);
	}
	
}
