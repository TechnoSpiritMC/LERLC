package org.leaf.api.internal;

import org.leaf.Main;
import org.leaf.utils.DataCollector;
import org.leaf.utils.Stack;

import java.util.*;

public class FailedRequestStore {
    private static final Stack<Request> failedRequests = new Stack<>(10);
    private static final Map<HookScope, List<DataCollector<FailedRequest>>> hooks = new EnumMap<>(HookScope.class);

    static {
        hooks.put(HookScope.Internal, Collections.synchronizedList(new ArrayList<>()));
        hooks.put(HookScope.User, Collections.synchronizedList(new ArrayList<>()));
    }

    /// <b>WARNING! This should NOT be used to add failed requests. Please use {@link FailedRequestStore#add(Request)} instead.</b>
    static Stack<Request> getStack() {
        return failedRequests;
    }

    /// Add a failed request to the store. Calls all hooks too.
    static void add(Request request) {

        synchronized (failedRequests) {
            failedRequests.push(request);
        }

        FailedRequest req = FailedRequest.from(request);

        List<DataCollector<FailedRequest>> userHooks = hooks.get(HookScope.User);
        synchronized (userHooks) {
            for (DataCollector<FailedRequest> hook : userHooks) {
                try {
                    hook.collect(req);
                } catch (Exception e) {
                    Main.logger.severe("Failed to run user hook!");
                }
            }
        }

        List<DataCollector<FailedRequest>> internalHooks = hooks.get(HookScope.Internal);
        synchronized (internalHooks) {
            for (DataCollector<FailedRequest> hook : internalHooks) {
                try {
                    hook.collect(req);
                } catch (Exception e) {
                    Main.logger.severe("Failed to run internal hook!");
                }
            }
        }
    }

    /// Get the last failed request record. It contains all info about the failed request.
    public static Optional<FailedRequest> getLastFailedRequest() {
        Request lastRequest = failedRequests.peek();
        return lastRequest != null? Optional.of(FailedRequest.from(lastRequest)) : Optional.empty();
    }

    static void clear() {
        failedRequests.clear();
    }


    public static void addHook(DataCollector<FailedRequest> hook) {
        hooks.get(HookScope.User).add(hook);
    }
    static void addInternalHook(DataCollector<FailedRequest> hook) {
        hooks.get(HookScope.Internal).add(hook);
    }

    static List<DataCollector<FailedRequest>> getHooks(HookScope scope) {
        return List.copyOf(hooks.get(scope));
    }


    enum HookScope {
        Internal,
        User;
    }
}
