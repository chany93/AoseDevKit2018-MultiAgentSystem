package unitn.adk2018.pddl;

public class PddlClause {
	
	public static PddlClause say(String predicate) {
		return new PddlClause ( predicate, new String [0]);
	}
	
	public static PddlClause say(String predicate, String arg1) {
		String[] args = {arg1};
		return new PddlClause ( predicate, args );
	}
	
	public static PddlClause say(String predicate, String arg1, String arg2) {
		String[] args = {arg1, arg2};
		return new PddlClause ( predicate, args );
	}
	
	public static PddlClause say(String predicate, String arg1, String arg2, String arg3) {
		String[] args = {arg1, arg2, arg3};
		return new PddlClause ( predicate, args );
	}
	
	public static PddlClause say(String predicate, String arg1, String arg2, String arg3, String arg4) {
		String[] args = {arg1, arg2, arg3, arg4};
		return new PddlClause ( predicate, args );
	}
	
	

	private final String predicate;
	private final String[] args;
	
	
	
	protected PddlClause(String _predicate, String[] _args) {
		predicate = _predicate;
		args = _args;
	}
	
	
	
	public void declareIn (PddlWorld w) {
		w.declare(this);
	}
	public void undeclareIn (PddlWorld w) {
		w.undeclare(this);
	}
	public boolean isDeclaredIn (PddlWorld w) {
		return w.isDeclared(this);
	}
	
	
	
	public String getPredicate() {
		return predicate;
	}

	public String[] getArgs() {
		return args;
	}
	
	@Override
	public String toString() {
		String clause = predicate;
		for (int i = 0; i < args.length; i++ )
			clause += " " + args [i];
		return clause;
	}
	
}
