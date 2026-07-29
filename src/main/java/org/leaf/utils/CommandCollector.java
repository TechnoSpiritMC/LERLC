package org.leaf.utils;

import org.leaf.api.internal.CommandExecutionProcess;


/**
 * An abstract class for collecting and processing {@link CommandExecutionProcess} states.
 * <br><br>
 * Subclasses of this class define the behavior for handling different lifecycle states
 * of a {@link CommandExecutionProcess}. The collector uses the {@code collect} method
 * to process a given {@link CommandExecutionProcess} and invokes state-specific handler
 * methods based on the process's current state.
 * <br><br>
 * The default implementation of the state handler methods ({@code onPending}, {@code onQueued},
 * {@code onExecuting}, {@code onFinished}, {@code onFailed}, and {@code onInternalError}) are
 * no-ops. Subclasses should override them to provide behavior tailored to their requirements.
 *
 * @see org.leaf.utils.DataCollector DataCollector
 */
public abstract class CommandCollector implements DataCollector<CommandExecutionProcess> {
    CommandExecutionProcess process;

    @Override
    public void collect(CommandExecutionProcess process) {

        this.process = process;

        switch (process.getState()) {
            case PENDING -> onPending();
            case QUEUED -> onQueued();
            case EXECUTING -> onExecuting();
            case FINISHED -> onFinished();
            case FAILED -> onFailed();
            case null, default -> onInternalError();
        }
    }

    /**
     * Handles the behavior for when the state of a {@link CommandExecutionProcess} is in the "PENDING" state.
     * <br><br>
     * This method is invoked by the {@link CommandCollector#collect(CommandExecutionProcess)} method when the
     * state of the {@link CommandExecutionProcess} is detected as "PENDING". Subclasses can override this
     * method to define specific behavior for processing or handling a pending command execution process.
     * <br><br>
     * The default implementation is empty and provides no behavior. Override this method in a subclass
     * to perform any required initialization, logging, or state management for pending processes.
     * <br><br>
     * Current {@link CommandExecutionProcess} can be found in the {@link CommandCollector#process} field.
     */
    void onPending() {};
    /**
     * Handles the behavior when a {@link CommandExecutionProcess} transitions to the "QUEUED" state.
     * <br><br>
     * This method is invoked by the {@link CommandCollector#collect(CommandExecutionProcess)} method
     * when the {@link CommandExecutionProcess} is detected to be in the "QUEUED" state. Implementations
     * can override this method to define specific behavior or actions needed when a process is queued.
     * <br><br>
     * The default implementation is empty and does not modify the state or perform any actions.
     * Subclasses should override this method to add specific logic for queued processes, such as
     * updating process metrics, logging, or scheduling tasks for execution.
     * <br><br>
     * Current {@link CommandExecutionProcess} can be found in the {@link CommandCollector#process} field.
     */
    void onQueued() {};
    /**
     * Handles the behavior for when the state of a {@link CommandExecutionProcess} is in the "EXECUTING" state.
     * <br><br>
     * This method is invoked automatically by the {@link CommandCollector#collect(CommandExecutionProcess)} method
     * when the state of the {@link CommandExecutionProcess} transitions to "EXECUTING". Subclasses can override
     * this method to provide specific behavior for handling the execution phase of the command process.
     * <br><br>
     * The default implementation is empty and performs no actions. Override this method in a subclass to
     * implement logic such as real-time monitoring, logging, or adjusting system state during command execution.
     * <br><br>
     * Current {@link CommandExecutionProcess} can be found in the {@link CommandCollector#process} field.
     */
    void onExecuting() {};
    /**
     * Handles the behavior for when the state of a {@link CommandExecutionProcess} is in the "FINISHED" state.
     * <br><br>
     * This method is invoked by the {@link CommandCollector#collect(CommandExecutionProcess)} method when the
     * state of the {@link CommandExecutionProcess} is detected as "FINISHED". Subclasses should override this
     * method to define specific behavior for processing or handling a finished command execution process.
     * <br><br>
     * The default implementation is empty and serves as a no-op. Override this method in a subclass to
     * perform any required actions, such as cleanup, finalization, logging, or state updates for finished
     * processes.
     * <br><br>
     * Current {@link CommandExecutionProcess} can be found in the {@link CommandCollector#process} field.
     */
    void onFinished() {};
    /**
     * Handles the behavior for when the state of a {@link CommandExecutionProcess} is in the "FAILED" state.
     * <br><br>
     * This method is called by the {@link CommandCollector#collect(CommandExecutionProcess)} method when the
     * state of the {@link CommandExecutionProcess} is detected as "FAILED". It provides a hook for defining
     * specific actions to perform when a command execution process transitions into a failed state.
     * <br><br>
     * Subclasses should override this method to implement custom behavior such as error handling, logging,
     * notifications, or recovery attempts. By default, the method has no implementation.
     * <br><br>
     * Current {@link CommandExecutionProcess} can be found in the {@link CommandCollector#process} field.
     */
    void onFailed() {};

    void onInternalError() {};
}
