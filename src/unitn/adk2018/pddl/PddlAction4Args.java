package unitn.adk2018.pddl;

public abstract class PddlAction4Args extends PddlAction {
	
	abstract public boolean checkPreconditions (PddlWorld world, String a1, String a2, String a3, String a4);
	abstract public boolean effects (PddlWorld world, String a1, String a2, String a3, String a4);
	
	public boolean checkPreconditions (PddlWorld world, String[] args) {
		if (args.length!=4)
			return false;
		return checkPreconditions(world, args[0], args[1], args[2], args[3]);
	}
	
	public boolean effects (PddlWorld world, String[] args) {
		if (args.length!=4)
			return false;
		return effects(world, args[0], args[1], args[2], args[3]);
	}
	
}
