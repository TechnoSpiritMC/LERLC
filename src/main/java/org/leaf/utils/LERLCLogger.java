package org.leaf.utils;

import java.util.logging.Logger;

public class LERLCLogger {
    public static void log(String message) {
        Logger.getLogger("LERLC").info(message);
    }
    public static void warn(String message) {
        Logger.getLogger("LERLC").warning(message);
    }
    public static void error(String message) {
        Logger.getLogger("LERLC").severe(message);
    }

    public static Logger getLogger() {
        return Logger.getLogger("LERLC");
    }
}
