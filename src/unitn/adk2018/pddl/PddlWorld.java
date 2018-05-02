package unitn.adk2018.pddl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

public class PddlWorld extends Observable {
	
	private Set<String> objects;
	private Map<String, PddlClause> declaredClauses;
	
	
	
	public PddlWorld() {
		objects = new HashSet<String> ();
		declaredClauses = new HashMap<String, PddlClause> ();
	}
	
	
	
	public synchronized String pddlObjects() {
		String pddl = "";
		for(String s : objects) {
			pddl = pddl + s + " ";
		}
		return pddl;
	}
	
	public synchronized String pddlClauses() {
			String pddl = "(and ";
			for(String s : declaredClauses.keySet()) {
				pddl = pddl + "(" + s + ") ";
			}
			return pddl + ")";
	}
	
	public synchronized Map<String, PddlClause> getACopyOfDeclaredClauses() {
		Map<String, PddlClause> copy = new HashMap<String, PddlClause> ();
		for(String key : declaredClauses.keySet()) {
			copy.put(key, declaredClauses.get(key));
		}
		return copy;
	}
	
	public synchronized Set<String> getACopyOfDeclaredObjects() {
		Set<String> copy = new HashSet<String> ();
		for(String o : objects) {
			copy.add(o);
		}
		return copy;
	}
	
	
	
	public synchronized void declare(PddlClause c) {
		for (String a : c.getArgs()) {
			if (!existsObject(a)) {
				declareObject(a);
//				System.err.println ( "PddlWorld.declare: unknown object " + a + " in clause " + c);
//				return;
			}
		}
		declaredClauses.put ( c.toString(), c );
		setChanged();
		notifyObservers();
	}
	
	public synchronized void undeclare(PddlClause c ) {
		declaredClauses.remove ( c.toString() );
		setChanged();
		notifyObservers();
	}
	
	public synchronized boolean isDeclared ( PddlClause c ) {
		return declaredClauses.containsKey ( c.toString() );
	}
	
	
	
	public synchronized void declareObject ( String object ) {
		objects.add(object);
	}
	
	public synchronized boolean existsObject ( String object ) {
		return objects.contains(object);
	}
	
	
	
	/*
	 * Say clauses binded to this PddlWorld
	 */
	public static class WorldClause {

		PddlWorld world;
		PddlClause clause;
		
		private WorldClause ( PddlWorld w, PddlClause c ) {
			clause = c;
			world = w;
		}
		
		public void declare () {
			world.declare(clause);
		}
		public void undeclare () {
			world.undeclare(clause);
		}
		public boolean isDeclared () {
			return world.isDeclared(clause);
		}
	}
	
	public WorldClause say(PddlClause c) {
		return new WorldClause ( this, c );
	}
	
	public WorldClause say(String predicate) {
		return new WorldClause ( this, PddlClause.say(predicate) );
	}
	
	public WorldClause say(String predicate, String arg1) {
		return new WorldClause ( this, PddlClause.say(predicate, arg1) );
	}
	
	public WorldClause say(String predicate, String arg1, String arg2) {
		return new WorldClause ( this, PddlClause.say(predicate, arg1, arg2) );
	}
	
	public WorldClause say(String predicate, String arg1, String arg2, String arg3) {
		return new WorldClause ( this, PddlClause.say(predicate, arg1, arg2, arg3) );
	}
	
	public WorldClause say(String predicate, String arg1, String arg2, String arg3, String arg4) {
		return new WorldClause ( this, PddlClause.say(predicate, arg1, arg2, arg3, arg4) );
	}
	
}
