package org.leaf.api.internal;

import org.leaf.api.command.Command;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/// Class representing a command execution process. Encapsulates the command getting executed as well as the execution status, telling where the execution is at currently. All states are described in {@link State}
public class CommandExecutionProcess {
    /**
     * Represents the state of a command execution process.
     * <ul>
     *     <li>{@link State#PENDING} - The command is not yet executed.</li>
     *     <li>{@link State#QUEUED} - The command is queued to be executed.</li>
     *     <li>{@link State#EXECUTING} - The command is currently being executed.</li>
     *     <li>{@link State#FINISHED} - The command has been executed successfully.</li>
     *     <li>{@link State#FAILED} - The command failed to execute.</li>
     * </ul>
     *
     * A command execution is considered as complete when it has reached a terminal state, either {@link State#FINISHED} or {@link State#FAILED}.
     * <br>Please note that if the queue is full, the command will be rejected with a {@link State#FAILED} state.
     */
    public enum State {
        PENDING,
        QUEUED,
        EXECUTING,
        FINISHED,
        FAILED;

        /**
         * Determines if the current state represents a completed command execution.
         * A command is considered complete if it has reached a terminal state,
         * such as {@code FINISHED} or {@code FAILED}.
         *
         * @return {@code true} if the state is {@code FINISHED} or {@code FAILED},
         *         indicating the command execution process is complete;
         *         {@code false} otherwise.
         */
        public boolean isComplete() {
            return this.ordinal() >= FINISHED.ordinal();
        }

        /**
         * Checks if the current state represents a successful command execution.
         *
         * @return {@code true} if the state is {@code FINISHED}, indicating that the
         *         command has been executed successfully; {@code false} otherwise.
         */
        public boolean isSuccessful() {
            return this == FINISHED;
        }

        /**
         * Checks if the current state represents a failed command execution.
         *
         * @return {@code true} if the state is {@code FAILED}, indicating that the
         *         command execution has failed; {@code false} otherwise.
         */
        boolean isFailed() {
            return this == FAILED;
        }
    }

    private State state;
    private final Command command;
    private final CompletableFuture<State> completionFuture = new CompletableFuture<>();

    private int queuePosition;
    private int queueSize;
    private Instant queuedAt;
    private Duration ETA;

    CommandExecutionProcess(Command command) {
        this.command = command;
        this.state = State.PENDING;
    }

    void setNewState(State state) {
        this.state = state;

        if (state.isComplete()) {
            completionFuture.complete(state);
        }
    }

    /**
     * Retrieves the current state of the command execution process.
     *
     * @return the current state of the process, represented as a {@link State}
     *         enumeration value. The state may include possible values such as
     *         {@code PENDING}, {@code QUEUED}, {@code EXECUTING}, {@code FINISHED},
     *         or {@code FAILED}, each indicating the current phase of the
     *         command execution.
     */
    public State getState() {
        return state;
    }

    /**
     * Retrieves the command associated with the current execution process.
     *
     * @return the current {@link Command} object associated with this process.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Get the position of the command in the queue.
     * @return the position of the command in the queue.
     */
    public int getQueuePosition() {
        return queuePosition;
    }

    /**
     * Get the size of the associated command queue. (Priority or regular queue depending on the queue in which this command has been queued).
     * @return the size of the associated command queue.
     */
    public int getQueueSize() {
        return queueSize;
    }

    /**
     * Get the time at which the command was queued.
     * @return the time at which the command was queued.
     */
    public Instant getQueuedAt() {
        return queuedAt;
    }

    /**
     * Returns the computed estimated execution time for this command. Please note that this is only an estimate based
     * on previous commands' execution times and average API latency. This also takes into account the queue position,
     * and should overall give a good estimate of when the command will get executed internally and when it will actually run in game.
     * This value is updated and readjusted every time a new command gets sent to take into account the latest API latencies.
     * <br><br>
     * However, this does not take into account a possible lockdown state, which would prevent the command from being executed,
     * or for any priority commands to be executed before this one (In the case of this not being a priority command itself).
     * @return the estimated execution time for this command.
     **/
    public Duration getETA() {
        return ETA;
    }

    public State awaitCompletion() throws InterruptedException, ExecutionException {
        return completionFuture.get();
    }

    void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }
    void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
    void setQueuedAt(Instant queuedAt) {
        this.queuedAt = queuedAt;
    }
    void setETA(Duration ETA) {
        this.ETA = ETA;
    }
}
