package org.leaf.utils;

/**
 * A functional interface for collecting and processing data of a specific type.
 *
 * <p>Implementations are typically provided as lambda expressions or method references.</p>
 *
 * <p>Example:</p>
 * <pre>{@code
 * DataCollector<CommandExecutionProcess> resultCollector = process -> {
 *     if (process.isSuccessful()) {
 *         logger.info("Command executed successfully: " + process.getCommandName());
 *     } else {
 *         logger.error("Command failed: " + process.getError());
 *     }
 * };
 *
 * raidEvent.banSender(resultCollector);
 * }</pre>
 *
 * @param <T> the type of data to be collected
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface DataCollector<T> {
    /**
     * Collects and processes the provided data.
     *
     * <p>This method is invoked to handle incoming data of type {@code T}.
     * Implementations should define the specific behavior for processing
     * the collected data, such as logging, storing, transforming, or
     * forwarding it to other components.</p>
     *
     * <p><b>Implementation Note:</b> This method should not throw checked exceptions.
     * If error handling is required, use try-catch blocks within the implementation
     * or wrap checked exceptions in runtime exceptions.</p>
     *
     * @param data the data to be collected and processed; may be null depending
     *             on the implementation's contract
     */
    void collect(T data);
}
