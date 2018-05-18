package unitn.adk2018;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unitn.adk2018.intention.Intention;

public class Logger {

	public static boolean GANTT = true;
	public static int A_MAX = 3;
	public static int I_MAX = 20;
	
	private static Logger logger;
	private static Logger getLogger() {
		if(logger==null) logger = new Logger();
		return logger;
	}
	
	
	
	private List<Agent> agents = new ArrayList<Agent>();
	private Map<Agent, List<Intention<?>>> intentions = new HashMap<Agent, List<Intention<?>>>();
	
	

	public static void println(Agent agent, String txt) {
		getLogger()._println(agent, null, txt);
	}
	public static void println(Intention<?> intention, String txt) {
		getLogger()._println(intention.agent, intention, txt);
	}
	synchronized public void _println(Agent agent, Intention<?> intention, String txt) {
		if(!intentions.containsKey(agent)) {
			agents.add(agent);
			intentions.put(agent, new ArrayList<Intention<?>>());
		}
		if(intention!=null && !intentions.get(agent).contains(intention)) {
			intentions.get(agent).add(intention);
//			if(intentions.size()>1) {
//				boolean shift = false;
//				for(Agent a : agents) {
//					for(Intention<?> i : intentions.get(a)) {
//						if(i == intention) {
//							System.out.print(" ");
//							shift = true;
//						}
//						else if(i.event==null || i.event.wasNotHandled().isTrue()) {
//							if(shift)
//								System.out.print("\\");
//							else
//								System.out.print("|");
//						}
//						else
//							System.out.print(" ");
//					}
//				}
//				System.out.println("");
//			}
		}
		
		String text = "";
		
		if (GANTT) {
			for(int _a=0; _a<A_MAX; _a++) {
				
				Agent a = null;
				if(agents.size()>_a)
					a = agents.get(_a);
				
				if(a != null)
					text += a.getName();
				
				if(a == agent)
					text += "*";
				else
					text += " ";
				
				for(int _i=0; _i<I_MAX; _i++) {
					
					Intention i = null;
					if(a!=null && intentions.get(a).size()>_i)
						i = intentions.get(a).get(_i);
					
					if(i!=null && i == intention)
						text += "o";
					else if(i!=null && i.event.wasNotHandled().isTrue())
						text += "|";
					else
						text += " ";
				}
			}
			text += " ";
		}
		
		text += agent.getName();
		if(intention!=null) text += " " + intention;
		text += ": " + txt;
		
		System.out.println(text);
		
	}
	
}
