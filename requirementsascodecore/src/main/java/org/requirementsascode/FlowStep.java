package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;

import org.requirementsascode.flowposition.FlowPosition;

/**
 * @author b_muth
 */
public abstract class FlowStep extends Step{
	private Flow flow;
	private FlowPosition flowPosition;
	private FlowStep previousStepInFlow;
	private Condition reactWhile;

	FlowStep(String stepName, Flow flow, Condition condition) {
		super(stepName, flow.getUseCase(), condition);
		this.flow = flow;
	}

	public Flow getFlow() {
		return flow;
	}

	public Optional<FlowStep> getPreviousStepInFlow() {
		return Optional.ofNullable(previousStepInFlow);
	}

	void setPreviousStepInFlow(FlowStep previousStepInFlow) {
		this.previousStepInFlow = previousStepInFlow;
	}

	public FlowPosition getFlowPosition() {
		return flowPosition;
	}

	void setFlowPosition(FlowPosition flowPosition) {
		Objects.requireNonNull(flowPosition);

		this.flowPosition = flowPosition;
	}

	public void orAfter(FlowStep step) {
		FlowPosition flowPositionAfterStep = flowPosition.orAfter(step.getName(), step.getUseCase());
    setFlowPosition(flowPositionAfterStep);
	}

	public void setReactWhile(Condition reactWhileCondition) {
		this.reactWhile = reactWhileCondition;
		createLoop();
	}

	private void createLoop() {
		getFlowPosition().orAfter(getName(), getUseCase());
	}

	public Condition getReactWhile() {
		return reactWhile;
	}
}
