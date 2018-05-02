package unitn.adk2018.pddl;

public abstract class PddlAction {
	
	abstract public boolean checkPreconditions (PddlWorld world, String[] args);
	abstract public boolean effects (PddlWorld world, String[] args);
	
	public boolean checkPreconditionsAndApply(PddlWorld world, String[] args) {
		if(checkPreconditions(world, args)) {
			effects(world, args);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
