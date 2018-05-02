package unitn.adk2018.pddl;

public abstract class PddlAction2Args extends PddlAction {

	abstract public boolean checkPreconditions (PddlWorld world, String a1, String a2);
	abstract public boolean effects (PddlWorld world, String a1, String a2);

	public boolean checkPreconditions (PddlWorld world, String[] args) {
		if (args.length!=2)
			return false;
		return checkPreconditions(world, args[0], args[1]);
	}
	
	public boolean effects (PddlWorld world, String[] args) {
		if (args.length!=2)
			return false;
		return effects(world, args[0], args[1]);
	}
	
}
