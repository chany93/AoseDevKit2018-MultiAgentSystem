package unitn.adk2018;

import java.util.*;

import unitn.adk2018.event.Message;
import unitn.adk2018.pddl.PddlDomain;

public class Environment {

	private static Environment theInstance;
	
	private static Environment getEnvironment () {
		if(theInstance==null) {
			theInstance = new Environment();
	        new GanttLiveLoggerGUI("Timeline");
	        new SimulationGui();
		}
		return theInstance;
	}
	
	
	
	public final Timer theTimer;
	public final HashMap<String, Agent> agents;
	public Agent environmentAgent;
	public PddlDomain pddlDomain;
	
	
	
	private Environment () {
		theTimer = new Timer();
		agents = new HashMap<String, Agent> ();
	}

	

	public static PddlDomain getPddlDomain () {
		return getEnvironment().pddlDomain;
	}

	public static void setPddlDomain(PddlDomain pddlDomain) {
		getEnvironment().pddlDomain = pddlDomain;
	}
	
	public static Timer getSystemTimer() {
		return getEnvironment().theTimer;
	}
	
	public static long getSimulationTime () {
		if (getEnvironmentAgent() != null)
			return getEnvironmentAgent().getAgentTime();
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
		if (getEnvironmentAgent() != null)
			return getEnvironmentAgent().isPaused();
		else
			return false;
	}
	

	public static Map<String, Agent> getAgents () {
		return getEnvironment().agents;
	}

	public static void addAgent ( Agent pa ) {
		if(getEnvironment().agents.containsKey(pa.name))
			System.err.println("ERROR: agent with name " + pa.getName() + "already exists!");
		getEnvironment().agents.put( pa.name, pa );
	}
	
	public static Agent getEnvironmentAgent() {
		return getEnvironment().environmentAgent;
	}

	public static void setEnvironmentAgent ( Agent a ) {
		getEnvironment().setEnvAgent(a);
	}
	private void setEnvAgent ( Agent a ) {
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
	
	public static boolean sendMessage ( Message m ) {
		boolean result = false;
		if ( getEnvironment().agents.containsKey (m.getTo()) ) {
			Agent pa = getEnvironment().agents.get(m.getTo());
			pa.pushMessage( m );
			result = true;
		}
		return result;
	}
	
	///
	/// Simple utility for debugging convenience
	///
	public static void printStackTrace() {
		try {
			throw new Exception ("trace point");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
