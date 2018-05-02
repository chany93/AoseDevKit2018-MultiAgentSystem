package unitn.adk2018.pddl;

import java.util.ArrayList;
import java.util.List;

public class PddlPlan {

	private final List<PddlStep> plan;
	
	
	
	public PddlPlan() {
		plan = new ArrayList<>();
	}
	
	
	
	public void addStep(PddlStep step) {
		plan.add(step);
	}
	
	public List<PddlStep> getSteps() {
		return plan;
	}
	
	
}
