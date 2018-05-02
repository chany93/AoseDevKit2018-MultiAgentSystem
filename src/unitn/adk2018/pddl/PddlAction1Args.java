package unitn.adk2018.pddl;

public abstract class PddlAction1Args extends PddlAction {

	abstract public boolean checkPreconditions (PddlWorld world, String a1);
	abstract public boolean effects (PddlWorld world, String a1);

	public boolean checkPreconditions (PddlWorld world, String[] args) {
		if (args.length!=1)
			return false;
		return checkPreconditions(world, args[0]);
	}
	
	public boolean effects (PddlWorld world, String[] args) {
		if (args.length!=1)
			return false;
		return effects(world, args[0]);
	}
	
}
