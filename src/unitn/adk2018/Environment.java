package unitn.adk2018;

import java.util.*;
import java.util.function.BiConsumer;

import unitn.adk2018.event.Message;
import unitn.adk2018.pddl.PddlDomain;

public class Environment {

	static Environment theInstance;
	
	public static Environment getEnvironment () {
		if(theInstance==null) {
			theInstance = new Environment();
	        new GanttLiveLoggerGUI("Timeline");
	        new SimulationGui();
		}
		return theInstance;
	}
	
	public static String getEnvironmentAgentName () {
		return getEnvironment().getEnvironmentAgent().getName();
	}
	
	
	
	public final Timer theTimer;
	public final HashMap<String, Agent> agents;
	public Agent environmentAgent;
	public PddlDomain pddlDomain;
	
	
	private Environment () {
		theTimer = new Timer();
		agents = new HashMap<String, Agent> ();
	}
	
	
	
	public Timer getSystemTimer() {
		return theTimer;
	}
	
	public static long getSimulationTime () {
		if (getEnvironment().getEnvironmentAgent() != null)
			return getEnvironment().getEnvironmentAgent().getAgentTime();
		else
			return 0;
	}
	
	public static void pauseSimulationTime() {
		theInstance.agents.forEach( (aname,agent)-> {
					agent.setPauseState (true);
				});
	}

	public static void resumeSimulationTime() {
		theInstance.agents.forEach( (aname,agent)-> {
					agent.setPauseState (false);
				});
	}

	public static boolean isPaused () {
		if (getEnvironment().getEnvironmentAgent() != null)
			return getEnvironment().getEnvironmentAgent().isPaused();
		else
			return false;
	}
	


	public void addAgent ( Agent pa ) {
		if(agents.containsKey(pa.name))
			System.err.println("ERROR: agent with name " + pa.getName() + "already exists!");
		agents.put( pa.name, pa );
	}
	
	public Agent getEnvironmentAgent() {
		return environmentAgent;
	}
	
	public void setEnvironmentAgent ( Agent a ) {
		if(!agents.containsKey(a.name))
			agents.put( a.name, a );
		
		if(environmentAgent!=null)
			environmentAgent.getBeliefs().deleteObserver( environmentBeliefsObserver );
		
		environmentAgent = a;
		
		environmentAgent.getBeliefs().addObserver( environmentBeliefsObserver );
	}
	
	private final Observer environmentBeliefsObserver = new Observer() {
		@Override
		public void update(Observable o, Object arg) {
			System.out.println( environmentAgent.getName() + " World changed: " + environmentAgent.getBeliefs().pddlClauses() );
		}
	};
	
	public boolean sendMessage ( Message m ) {
		boolean result = false;
		if ( agents.containsKey (m.getTo()) ) {
			Agent pa = agents.get(m.getTo());
			pa.pushMessage( m );
			result = true;
		}
		return result;
	}
	
	///
	/// Simple utility for debugging convenience
	///
	static public void printStackTrace() {
		try {
			throw new Exception ("trace point");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
