package org.requirementsascode;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Use an instance of this class if you want to find out the details about the
 * step to be run in a custom event handler, and to trigger the system reaction.
 *
 * @see ModelRunner#handleWith(Consumer)
 * @author b_muth
 */
public class StepToBeRun implements Serializable {
    private static final long serialVersionUID = -8615677956101523359L;

    private Object event;
    private Step step;

    StepToBeRun() {
    }

    /**
     * Triggers the system reaction of this step.
     * 
     * @return the events to be published, resulting from the system reaction
     */
    @SuppressWarnings("unchecked")
    public Object[] run() {
	Function<Object, Object[]> systemReactionFunction = (Function<Object, Object[]>) step.getSystemReaction();
	return systemReactionFunction.apply(event);
    }

    /**
     * Returns the name of the step whose system reaction is performed when
     * {@link #run()} is called.
     *
     * @return the step name.
     */
    public String getStepName() {
	return step.getName();
    }

    /**
     * Returns the precondition that needs to be true to trigger the system reaction
     * when {@link #run()} is called.
     *
     * @return the condition, or an empty optional when no condition was specified.
     */
    public Optional<? extends Object> getCondition() {
	Optional<? extends Object> optionalCondition = step.getCondition();
	return optionalCondition;
    }

    /**
     * Returns the event object that will be passed to the system reaction when
     * {@link #run()} is called.
     *
     * @return the event object, or an empty optional when no event was specified.
     */
    public Optional<? extends Object> getEvent() {
	Optional<? extends Object> optionalEvent = null;
	if (event instanceof ModelRunner) {
	    optionalEvent = Optional.empty();
	} else {
	    optionalEvent = Optional.of(event);
	}
	return optionalEvent;
    }

    /**
     * Returns the system reaction to be executed when {@link #run()} is called.
     *
     * @return the system reaction object, as specified in the model.
     */
    public Object getSystemReaction() {
	Object systemReactionObject = step.getSystemReaction().getModelObject();
	return systemReactionObject;
    }
    
    void setupWith(Object event, Step useCaseStep) {
	this.event = event;
	this.step = useCaseStep;
    }
}
