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
    @Override
    public final void collect(CommandExecutionProcess process) {
        switch (process.getState()) {
            case PENDING -> onPending(process);
            case QUEUED -> onQueued(process);
            case EXECUTING -> onExecuting(process);
            case FINISHED -> onFinished(process);
            case FAILED -> onFailed(process);
            case null, default -> onInternalError(process);
        }
    }
    protected void onPending(CommandExecutionProcess process) {}
    protected void onQueued(CommandExecutionProcess process) {}
    protected void onExecuting(CommandExecutionProcess process) {}
    protected void onFinished(CommandExecutionProcess process) {}
    protected void onFailed(CommandExecutionProcess process) {}
    protected void onInternalError(CommandExecutionProcess process) {}
}
