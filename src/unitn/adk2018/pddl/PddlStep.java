package unitn.adk2018.pddl;

public class PddlStep {
	
	public static PddlStep act(String action, boolean parallelizableWithPrevious) {
		return new PddlStep ( action, parallelizableWithPrevious, new String [0]);
	}
	
	public static PddlStep act(String action, boolean parallelizableWithPrevious, String arg1) {
		String[] args = {arg1};
		return new PddlStep ( action, parallelizableWithPrevious, args );
	}
	
	public static PddlStep act(String action, boolean parallelizableWithPrevious, String arg1, String arg2) {
		String[] args = {arg1, arg2};
		return new PddlStep ( action, parallelizableWithPrevious, args );
	}
	
	public static PddlStep act(String action, boolean parallelizableWithPrevious, String arg1, String arg2, String arg3) {
		String[] args = {arg1, arg2, arg3};
		return new PddlStep ( action, parallelizableWithPrevious, args );
	}
	
	public static PddlStep act(String action, boolean parallelizableWithPrevious, String arg1, String arg2, String arg3, String arg4) {
		String[] args = {arg1, arg2, arg3, arg4};
		return new PddlStep ( action, parallelizableWithPrevious, args );
	}
	
	public static PddlStep act(String action, boolean parallelizableWithPrevious, String[] args) {
		return new PddlStep ( action, parallelizableWithPrevious, args );
	}
	
	

	private final String action;
	private final boolean parallelizableWithPrevious;
	private final String[] args;
	
	
	
	protected PddlStep(String _action, boolean _parallelizableWithPrevious, String[] _args) {
		action = _action;
		parallelizableWithPrevious = _parallelizableWithPrevious;
		args = _args;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return the parallelizableWithPrevious
	 */
	public boolean isParallelizableWithPrevious() {
		return parallelizableWithPrevious;
	}

	/**
	 * @return the args
	 */
	public String[] getArgs() {
		return args;
	}
	
	
}
	
