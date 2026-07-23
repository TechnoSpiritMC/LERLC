package org.leaf.api.internal;

import org.leaf.api.command.Command;
import org.leaf.utils.LERLCLogger;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * CommandExecutor is responsible for managing the execution of commands
 * through a queue-based scheduling system. It maintains two separate queues:
 * a standard queue for regular commands and a priority queue for commands
 * that need expedited execution. The class ensures that commands are executed
 * within rate limits and handles retry mechanisms for failed commands.
 */
public class CommandExecutor {
    private final Queue<CommandExecutionProcess> commandQueue = new ArrayDeque<>(10);
    private final Queue<CommandExecutionProcess> priorityQueue = new ArrayDeque<>(5);

    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(2);
    private Instant previousExecution = Instant.now();
    private Instant nextRateLimitReset = Instant.now();

    public CommandExecutor() {
        scheduler.scheduleAtFixedRate(this::heartbeat, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void heartbeat() {
        if (priorityQueue.isEmpty() && commandQueue.isEmpty()) return;
        if (Instant.now().isBefore(nextRateLimitReset)) return;

        CommandExecutionProcess process;
        boolean wasPriority = true;
        do {
            if (Cache.instance.lockdown.get() && priorityQueue.isEmpty()) return;
            process = priorityQueue.poll();

            if (process == null) {
                process = commandQueue.poll();
                wasPriority = false;
            }

        } while (process == null || process.getCommand() == null);

        if (wasPriority) {
            for (CommandExecutionProcess p : priorityQueue) {
                p.setQueuePosition(p.getQueuePosition() - 1);
                p.setETA(Duration.between(previousExecution, nextRateLimitReset).multipliedBy(p.getQueuePosition()).plusSeconds(1));
                p.setQueueSize(priorityQueue.size());
            }
        } else {
            for (CommandExecutionProcess p : commandQueue) {
                p.setQueuePosition(p.getQueuePosition() - 1);
                p.setETA(Duration.between(previousExecution, nextRateLimitReset).multipliedBy(p.getQueuePosition()).plusSeconds(1));
                p.setQueueSize(commandQueue.size());
            }
        }

        Command command = process.getCommand();
        process.setNewState(CommandExecutionProcess.State.EXECUTING);

        Request request = new Request(Cache.instance.ctx, "/command", true, ConnectionMethod.POST);
        request.setCommandRequestBody(command.getRawCommand());

        try {
            request.send();
        } catch (Exception e) {
            process.setNewState(CommandExecutionProcess.State.FAILED);
            LERLCLogger.getLogger().severe("Failed to execute command: " + command.getRawCommand());
        }

        if (request.returnCode == 200) {
            process.setNewState(CommandExecutionProcess.State.FINISHED);
        } else {
            process.setNewState(CommandExecutionProcess.State.FAILED);
            LERLCLogger.getLogger().severe("Failed to execute command: " + command.getRawCommand() + " (HTTP " + request.returnCode + " (" + request.body +"))");
        }

        previousExecution = Instant.now();
        nextRateLimitReset = request.rateLimitReset;
    }

    public boolean submit(CommandExecutionProcess process) {
        boolean added = false;
        if (!Cache.instance.lockdown.get()) {
             added = commandQueue.offer(process);
        }

        if (added) {
            process.setNewState(CommandExecutionProcess.State.QUEUED);
            process.setQueuedAt(Instant.now());
        } else {
            process.setNewState(CommandExecutionProcess.State.FAILED);
        }

        return added;
    }

    public boolean submitPriority(CommandExecutionProcess process) {
        boolean added = priorityQueue.offer(process);

        if (added) {
            process.setNewState(CommandExecutionProcess.State.QUEUED);
            process.setQueuedAt(Instant.now());
        } else {
            process.setNewState(CommandExecutionProcess.State.FAILED);
        }

        return added;
    }


    public int getQueueSize() {
        return commandQueue.size();
    }
    public int getPriorityQueueSize() {
        return priorityQueue.size();
    }

    void shutdown() {
        scheduler.shutdown();
    }
}
