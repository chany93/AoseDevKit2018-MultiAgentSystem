package unitn.adk2018.pddl;

import java.util.HashMap;

public class PddlDomain {

	public final String domainFile;
	private final HashMap<String, Class<? extends PddlAction>> actionDictionary;
	
	
	
	public PddlDomain(String _domainFile) {
		domainFile = _domainFile;
		actionDictionary = new HashMap<String, Class<? extends PddlAction>> ();
	}
	
	
	
	public boolean addSupportedAction ( String _name, Class<? extends PddlAction> pddlAction) {
		actionDictionary.put (_name, pddlAction);
		return true;
	}
	
	public PddlAction generatePddlAction ( String actionName ) {
		try {
			Class<? extends PddlAction> generator = actionDictionary.get (actionName);
			if ( generator == null) {
				System.err.println ( "unknown action requested: " + actionName );
				System.err.println ( "known actions: " + actionDictionary );
				return null;
			}
			return generator.newInstance();  /// Action-specific base creator invoked
		} catch (Exception e) {
			System.err.println ( "Agent.generatePddlAction(): " + e);
			return null;
		}
	}
	
}
