package edu.cornell.mannlib.vitro.webapp.dynapi.scopes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Action;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.ConditionalStep;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.OperationalStep;
import edu.cornell.mannlib.vitro.webapp.dynapi.components.scopes.OperationGraph;


public class OperationGraphTest {
  
	
	@Test
	public void testOperationGraphActionEmpty() {
		Action action = new Action();
		OperationGraph graph = new OperationGraph(action);
		
		assertEquals(1, graph.getHeadsOf(action).size());
		assertEquals(null, graph.getHeadsOf(action).get(0));
		assertEquals(0, graph.getTailsOf(action).size());
	}

	
	@Test
	public void testOperationGraphOneStep() {
		Action action = new Action();
		OperationalStep step = new OperationalStep();
		action.setStep(step);
		OperationGraph graph = new OperationGraph(action);

		assertEquals(1, graph.getHeadsOf(action).size());
		assertEquals(step, graph.getHeadsOf(action).get(0));
		assertEquals(1, graph.getTailsOf(null).size());
		assertEquals(step, graph.getTailsOf(null).get(0));
	}

	@Test
	public void testOperationGraphTwoSteps() {
		Action action = new Action();
		OperationalStep step1 = new OperationalStep();
		action.setStep(step1);
		OperationalStep step2 = addNextStep(step1);
		OperationGraph graph = new OperationGraph(action);
		assertEquals(1, graph.getHeadsOf(step2).size());
		assertEquals(null, graph.getHeadsOf(step2).get(0));

	}
	 
	@Test
	public void testOperationGraphConditional() {
		Action action = new Action();
		ConditionalStep conditionalStep = new ConditionalStep();
		action.setStep(conditionalStep);

		OperationalStep operationStep1 = new OperationalStep();
		conditionalStep.setNextStepFalse(operationStep1);
		OperationalStep operationStep2 = new OperationalStep();
		conditionalStep.setNextStepTrue(operationStep2);

		OperationGraph graph = new OperationGraph(action);

		assertEquals(2, graph.getTailsOf(null).size());
		assertTrue(graph.getTailsOf(null).contains(operationStep1));
		assertTrue(graph.getTailsOf(null).contains(operationStep2));
	}
	
	
	//TODO: Such configurations are invalid and should be rejected at the validation stage
	
	@Test
	public void testOperationGraphOneStepLoop() {
		Action action = new Action();
		OperationalStep step = new OperationalStep();
		action.setStep(step);
		step.setNextStep(step);
		OperationGraph graph = new OperationGraph(action);

		assertEquals(0, graph.getTailsOf(null).size());
	}

	private OperationalStep addNextStep(OperationalStep step1) {
		OperationalStep step2 = new OperationalStep();
		step1.setNextStep(step2);
		return step2;
	}
	 
}
