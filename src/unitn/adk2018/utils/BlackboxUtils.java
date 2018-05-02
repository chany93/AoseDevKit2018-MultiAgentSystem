package unitn.adk2018.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import unitn.adk2018.Agent;
import unitn.adk2018.pddl.PddlPlan;
import unitn.adk2018.pddl.PddlStep;
import unitn.adk2018.pddl.PddlWorld;

public class BlackboxUtils {
	
	static final String blackboxDir = "blackbox";
	static final String blackbox = blackboxDir + "/blackbox";
	static final String pddlWorkDir = "./tmp";
	
	
	
	public static PddlPlan doPlan(Agent agent, String pddlDomainFile, PddlWorld world, String pddlGoal) {
		String problemFile = generatePddlProblemFile(agent.getName(), pddlDomainFile, world, pddlGoal);
		return invokePlanner(agent, pddlDomainFile+".pddl", problemFile);
	}
	
	
	
	private static String generatePddlProblemFile(String agentName, String pddlDomainFile, PddlWorld world, String pddlGoal) {
		Date d = new Date (System.currentTimeMillis());
		SimpleDateFormat f = new SimpleDateFormat ( "yyyyMMddHHmmss");
		String problemFile = pddlWorkDir + "/" + agentName + "_problem_" + f.format(d);
		
		try {
			BufferedWriter writer = new BufferedWriter ( new FileWriter (problemFile) );
			writer.write ( "(define (problem test1)\n");
			writer.write( "  (:domain " + pddlDomainFile + ")\n");
			writer.write( "  (:objects " + world.pddlObjects() + ")\n");
			writer.write( "  (:init " + world.pddlClauses() + ")\n");
			writer.write( "  (:goal " + pddlGoal + ")\n");
			writer.write ( ")\n");
			writer.close();
		} catch (Exception e) {
			System.out.println( "test case: " + e);
			e.printStackTrace();
		}
		return problemFile;
	}
	
	
	
	private static PddlPlan invokePlanner(Agent agent, String pddlDomainFile, String problemFile) {
		String fullOutput = "no plan";
		PddlPlan plan = new PddlPlan();
		try {
			if (agent.debugOn) System.out.println ( agent.getName() + " starting planning..." );
			ProcessBuilder pb = new ProcessBuilder ( blackbox, "-o", pddlDomainFile, "-f", problemFile );
			pb.redirectErrorStream(true);
			Process p = pb.start();
			p.waitFor();
			if (agent.debugOn) System.out.println ( agent.getName() + " ... planner exit code: " + p.exitValue() );
			InputStream st = p.getInputStream();
			int max = st.available();
			byte[] content = new byte [max];
			st.read (content);
			fullOutput = new String ( content );
			if ( p.exitValue() != 0) {
				if (agent.debugOn)
					System.out.println(fullOutput);
				return null;
			}
			String[] lines = fullOutput.split( "\n" );
			if (agent.debugOn) System.out.println ( agent.getName() + " number of lines: " + lines.length );
			int lnr = 0;
			while ( ! lines [lnr].equals ( "Begin plan") )
				lnr++;
			if (agent.debugOn) System.out.println (agent.getName() + " start output analysis: " + lines [lnr]);
			lnr++;
			
			int action_count = 0;
			int previous_action_nr = 0;
			PddlStep previous_step = null;
//			ScheduledAction previous_action = null;
			
			while ( ! lines [lnr].equals ( "End plan") ) {
				action_count++;
				String action = lines[lnr].trim();
				String[] tokens = action.split("[() ]+");
				// System.out.println (lines [lnr] + " nr. tokens: " + tokens.length);

				Integer it = new Integer ( tokens [0] );
				String what = tokens [1];
				int paramsNr = tokens.length - 2;
				String[] args = new String [paramsNr];
				for ( int i = 0; i < tokens.length  - 2; i++ ) {
//					if (debugOn) System.out.print( " p[" + i + "] <" + tokens [2 + i] + ">");
					args [i] = tokens [2 + i];
				}
				
				boolean parallelizableWithPrevious = false;
				if ( it != previous_action_nr ) {
					for ( int i = previous_action_nr + 1; i < it; i++ )
						if (agent.debugOn) System.out.println( agent.getName() + "  missing: " + i );
					if (agent.debugOn) System.out.print ( agent.getName() + " action nr: " + it );
					previous_action_nr = it;
				} else {
					if (agent.debugOn) System.out.print( agent.getName() + "  || " );
//					previous_action.parallelizableWithNext = true;
//					sa.parallelizableWithPrevious = true;
					parallelizableWithPrevious = true;
				}

				PddlStep sa = PddlStep.act(what, parallelizableWithPrevious, args);
				plan.addStep( PddlStep.act(what, parallelizableWithPrevious, args) );
				
//				ScheduledAction sa = new ScheduledAction ( what, args, parallelizableWithPrevious, agent );
//				outputActions.add(sa);
				
				if (agent.debugOn) System.out.print ( " do: " + what);
				if (agent.debugOn) System.out.println ( " ." );
				previous_step = sa;
//				previous_action = sa;
				lnr++;
			}
			if (agent.debugOn) System.out.println (agent.getName() + " end output analysis after " + action_count + " lines: " + lines [lnr]);
			
		} catch (Throwable t) {
			//System.err.println ( agent.getName() + " exception: " + t + "\ntrace: " );
			//t.printStackTrace();
			System.err.println( "planner output:");
			System.err.println( fullOutput );
			
			return null;
		}
		return plan;
	}
	
}
